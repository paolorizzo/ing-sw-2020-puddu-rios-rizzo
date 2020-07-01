package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.SetupAction;
import it.polimi.ingsw.observation.*;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The client node in the client-server network architecture of the game.
 */
public class Client extends Messenger implements ControllerInterface, Runnable, NetworkInterface
{
    private Socket socket;
    private String ip = null;
    private int port = 0;
    private final MessageSynchronizer synchronizer;
    private AlivenessHandler alivenessHandler;
    private ClientView cw;
    private final FeedObservable virtualFeed;
    private boolean alive;
    private final Object netLock = new Object();
    private boolean netPass = false;
    private final Object playersLock = new Object();
    private boolean notifyingWaitingForPlayers = false;
    private boolean reading;

    /**
     * Constructs an instance of a client node in the network.
     * @param ip the IPv4 address of the server to connect to.
     * @param port the port of the server to connect to.
     */
    public Client(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        virtualFeed = new FeedObservable();
        alive = true;
        this.reading = true;
        this.synchronizer = new MessageSynchronizer(this);
        this.netPass = true;
    }

    /**
     * Constructs an instance of a client node in the network.
     */
    public Client()
    {
        virtualFeed = new FeedObservable();
        alive = true;
        this.reading = true;
        this.synchronizer = new MessageSynchronizer(this);
    }

    /**
     * Retrieves the IPv4 address of the connected server.
     * @return the IP address as String.
     */
    @Override
    public String getIp()
    {

        return ip;
    }

    /**
     * Retrieves the port number of the connected server.
     * @return the port number.
     */
    @Override
    public int getPort()
    {

        return port;
    }

    /**
     * Sets the IPv4 address of the server.
     * @param ip the string storing the ip address.
     */
    @Override
    public void setIp(String ip)
    {

        this.ip = ip;
    }

    /**
     * Sets the port number od the server.
     * @param port the number of the port.
     */
    @Override
    public void setPort(int port)
    {
        this.port = port;
        synchronized (netLock)
        {
            netPass = true;
            netLock.notify();
        }
    }

    /**
     * Associates a client view to the client.
     * @param cw the reference to the ClientView instance.
     */
    public void setClientView(ClientView cw)
    {
        this.cw = cw;
        addObserver(cw);
        this.alivenessHandler = new AlivenessHandler(this, cw);
    }

    /**
     * Starts the closing connection procedure.
     */
    @Override
    public void closeConnection()
    {

        alivenessHandler.stopMonitoringLiveness();
    }

    /**
     * Retrieves the client view instance associated to the client.
     * @return the ClientView reference.
     */
    public ClientView getClientView()
    {

        return cw;
    }

    /**
     * Adds a view as observer, taking the place of the model in the client's side MVC architecture.
     * @param view the view as FeedObserver.
     */
    public void addObserver(FeedObserver view)
    {

        virtualFeed.addObserver(view);
    }

    /**
     * Always returns the reference to the client itself.
     * @param methodName the triggered method name.
     * @return the client itself.
     */
    protected Object getObservable(String methodName)
    {

        return virtualFeed;
    }

    /**
     * Handles the messages coming from the stream (the server), de-serializing and calling callMethod.
     * Creates and runs the thread responsible to send "pong" messages to the server, to notify the client aliveness.
     */
    public void run()
    {
        synchronized(netLock)
        {
            if (ip == null && port == 0)
            {
                cw.askIpAndPort();
                try
                {
                    while (!netPass)
                    {
                        netLock.wait();
                    }
                }
                catch (InterruptedException e)
                {
                    System.err.println("Problems with setting the ip");
                }
            }
        }

        boolean waitingForServer = true;


        while(waitingForServer)
        {
            try
            {
                socket = new Socket(ip, port);

                synchronizer.run();

                waitingForServer = false;

                new Thread(() -> {

                    while(true)
                    {
                        try
                        {
                            TimeUnit.MILLISECONDS.sleep(1500);
                        }
                        catch(InterruptedException t)
                        {
                            System.err.println(t.getMessage());
                        }

                        synchronized(playersLock)
                        {
                            if(notifyingWaitingForPlayers)
                                showUIError("Waiting for the other players to join");
                        }
                    }

                }).start();

                //starts the clientView therefore initiating the message exchange
                cw.start();
                alivenessHandler.startPing();
                alivenessHandler.startMonitoringLiveness();
                ObjectInputStream ByteIn;

                while(reading)
                {
                    try
                    {
                        ByteIn = new ObjectInputStream(socket.getInputStream());
                        Message message = (Message) ByteIn.readObject();

                        if(message.getMethodName().equals("pong"))
                            alivenessHandler.pongHandler(message);
                        else
                            if(message.getMethodName().equals("notifyAllPlayersConnected"))
                            {
                                synchronized(playersLock)
                                {
                                    notifyingWaitingForPlayers = false;
                                }
                            }
                            if(message.getMethodName().equals("notifyDisconnection"))
                            {
                                reading = false;
                            }
                            synchronizer.enqueueMessage(message);
                    }
                    catch (ClassNotFoundException e)
                    {
                        System.err.println("Error in reading the object");
                    }
                }
            }
            catch(IOException e)
            {
                if(waitingForServer)
                {
                    try
                    {
                        showUIError("Waiting for the server on "+ip+" / "+port+" to start");
                        TimeUnit.MILLISECONDS.sleep(1500);
                    }
                    catch(InterruptedException t)
                    {
                        System.err.println(t.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Delegate the sending of messages to the Messenger superclass, handling the socket output stream.
     * @param methodName the name of the method to be called via reflection on the server.
     * @param arg the args of the target method.
     */
    public synchronized void sendMessage(String methodName, Object ...arg)
    {
        try{
            super.sendMessage(new ObjectOutputStream(socket.getOutputStream()), methodName, arg);
        }
        catch (IOException e)
        {
            if(getClientView() !=null)
            {
                alivenessHandler.registerMessageFailure();
            }
        }
    }

    /**
     * High level interface to display an error on the user interface.
     * @param error the text to display.
     */
    public void showUIError(String error)
    {

        getClientView().getUi().showError(error);
    }

    /**
     * This method does nothing on the client.
     * It is here only because it is useful on the controller,
     * and is therefore present on the ControllerInterface, which this class implements
     */
    @Override
    public void handleDisconnection(){}

    /**
     * Asks for the generation of a new identification number (id), mandatory to communicate on the network.
     */
    @Override
    public void generateId()
    {

        sendMessage("generateId");
    }

    /**
     * Acknowledge the reception of the id.
     * @param id the client's id.
     */
    @Override
    public void ackId(int id)
    {

        sendMessage("ackId", id);
        if(id > 0)
        {
            synchronized(playersLock)
            {
                notifyingWaitingForPlayers = true;
            }
        }
    }

    /**
     * Communicates the requested number of players for the upcming game.
     * @param id the client's id.
     * @param numPlayers the requested number of players.
     */
    @Override
    public void setNumPlayers(int id, int numPlayers)
    {
        sendMessage("setNumPlayers", cw.getId(), numPlayers);
        if(id == 0)
        {
            synchronized(playersLock)
            {
                notifyingWaitingForPlayers = true;
            }
        }
    }

    /**
     * Asks for the number of players set for the game.
     */
    @Override
    public void getNumPlayers()
    {

        sendMessage("getNumPlayers");
    }

    /**
     * Asks if all players are connected.
     */
    @Override
    public  void requestAllPlayersConnected()
    {

        sendMessage("requestAllPlayersConnected");
    }

    /**
     * Communicates a name proposal to the model.
     * @param id the client's id.
     * @param name the proposed name.
     */
    @Override
    public void setName(int id, String name)
    {

        sendMessage("setName", id, name);
    }

    /**
     * Forwards the method call to the controller through the socket.
     */
    @Override
    public void isGameAvailable()
    {

        sendMessage("isGameAvailable");
    }

    /**
     * Forwards the method call to the controller through the socket.
     */
    @Override
    public void restore(int id, boolean intentToRestore)
    {

        sendMessage("restore", id, intentToRestore);
    }

    /**
     * Forwards the method call to the controller through the socket.
     */
    @Override
    public void willRestore(){
        sendMessage("willRestore");
    }

    /**
     * Asks the model for the cards deck.
     */
    @Override
    public void requestDeck()
    {

        sendMessage("requestDeck");
    }

    /**
     * Communicated a list of selected cards.
     * @param id the client's id.
     * @param numCards a list of cards' identification numbers.
     */
    @Override
    public void publishCards(int id, List<Integer> numCards)
    {

        sendMessage("publishCards", id, numCards);
    }

    /**
     * Asks the model for the set of cards to choose between.
     * @param id the client's id.
     */
    @Override
    public void requestCards(int id)
    {

        sendMessage("requestCards", id);
    }

    /**
     * Communicates the selected card.
     * @param id the client's id.
     * @param numCard the card identification number.
     */
    @Override
    public void setCard(int id, int numCard)
    {

        sendMessage("setCard", id, numCard);
    }

    /**
     * Communicates to the model the intention of setting up a worker.
     * @param id the client's id.
     */
    @Override
    public void requestToSetupWorker(int id)
    {

        sendMessage("requestToSetupWorker", id);
    }

    /**
     * Informs the model about the setup of a worker.
     * @param id the client's id.
     * @param setupAction the setup action object.
     */
    @Override
    public void setupWorker(int id, SetupAction setupAction)
    {

        sendMessage("setupWorker", id, setupAction);
    }

    /**
     * Asks the model for the possible actions.
     * @param id the client's id.
     */
    @Override
    public void requestActions(int id)
    {

        sendMessage("requestActions", id);
    }

    /**
     * Informs the model about the chosen action.
     * @param id the client's id.
     * @param action the action object to send.
     */
    @Override
    public void publishAction(int id, Action action)
    {

        sendMessage("publishAction", id, action);
    }

    /**
     * Informs the model about the player's will to end his turn.
     * @param id the client's id.
     */
    @Override
    public void publishVoluntaryEndOfTurn(int id)
    {

        sendMessage("publishVoluntaryEndOfTurn", id);
    }

    /**
     * Legacy method that used to stop the client.
     */
    @Override
    public void kill()
    {

        alive = false;
    }

    /**
     * Asks the model to delete the client's id and therefore to end every communication with it.
     * @param id the client's id.
     */
    @Override
    public void deleteId(int id)
    {

        sendMessage("deleteId", id);
    }
}