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
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Client extends Messenger implements ControllerInterface, Runnable
{
    private Socket socket;
    private final String ip;
    private final int port;

    private final MessageSynchronizer synchronizer;
    private AlivenessHandler alivenessHandler;

    private ClientView cw;

    private final FeedObservable virtualFeed;

    private boolean alive;

    public Client(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        virtualFeed = new FeedObservable();
        alive = true;
        this.synchronizer = new MessageSynchronizer(this);
    }

    public void setClientView(ClientView cw)
    {
        this.cw = cw;
        addObserver(cw);
        this.alivenessHandler = new AlivenessHandler(this, cw);
    }

    public ClientView getClientView()
    {

        return cw;
    }

    public void addObserver(FeedObserver view)
    {

        virtualFeed.addObserver(view);
    }

    //always returns the client itself
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
        boolean waitingForServer = true;
        while(waitingForServer)
        {
            try
            {
                socket = new Socket(ip, port);

                synchronizer.run();

                waitingForServer = false;

                //starts the clientView therefore initiating the message exchange
                cw.start();
                alivenessHandler.startPing();
                alivenessHandler.startMonitoringLiveness();
                ObjectInputStream ByteIn;

                while(true)
                {
                    try
                    {
                        ByteIn = new ObjectInputStream(socket.getInputStream());
                        Message message = (Message) ByteIn.readObject();
                        if(message.getMethodName().equals("pong"))
                            alivenessHandler.pongHandler(message);
                        else
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
                        System.out.print("Waiting for the server to start");
                        TimeUnit.MILLISECONDS.sleep(500);
                        System.out.print(" .");
                        TimeUnit.MILLISECONDS.sleep(500);
                        System.out.print(".");
                        TimeUnit.MILLISECONDS.sleep(500);
                        System.out.print(".\n");
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
                //getClientView().connectionLost();
            }
        }
    }

    //connection phase methods
    @Override
    public void generateId()
    {

        sendMessage("generateId");
    }

    @Override
    public void ackId(int id)
    {

        sendMessage("ackId", id);
    }

    @Override
    public void setNumPlayers(int id, int numPlayers)
    {

        sendMessage("setNumPlayers", cw.getId(), numPlayers);
    }

    @Override
    public void getNumPlayers()
    {

        sendMessage("getNumPlayers");
    }

    @Override
    public  void requestAllPlayersConnected()
    {

        sendMessage("requestAllPlayersConnected");
    }

    @Override
    public void setName(int id, String name)
    {

        sendMessage("setName", id, name);
    }

    //Restore phase methods

    /**
     * forwards the method call to the controller through the socket
     */
    @Override
    public void isGameAvailable(){
        sendMessage("isGameAvailable");
    }

    /**
     * forwards the method call to the controller through the socket
     */
    @Override
    public void restore(int id, boolean intentToRestore){
        sendMessage("restore", id, intentToRestore);
    }

    /**
     * forwards the method call to the controller through the socket
     */
    @Override
    public void willRestore(){
        sendMessage("willRestore");
    }

    //Setup phase methods

    @Override
    public void requestDeck()
    {

        sendMessage("requestDeck");
    }

    @Override
    public void publishCards(int id, List<Integer> numCards)
    {

        sendMessage("publishCards", id, numCards);
    }

    @Override
    public void requestCards(int id)
    {

        sendMessage("requestCards", id);
    }

    @Override
    public void setCard(int id, int numCard)
    {

        sendMessage("setCard", id, numCard);
    }

    @Override
    public void requestToSetupWorker(int id)
    {

        sendMessage("requestToSetupWorker", id);
    }

    @Override
    public void setupWorker(int id, SetupAction setupAction)
    {

        sendMessage("setupWorker", id, setupAction);
    }

    @Override
    public void requestActions(int id)
    {

        sendMessage("requestActions", id);
    }

    @Override
    public void publishAction(int id, Action action)
    {

        sendMessage("publishAction", id, action);
    }

    @Override
    public void publishVoluntaryEndOfTurn(int id)
    {

        sendMessage("publishVoluntaryEndOfTurn", id);
    }

    @Override
    public void kill()
    {

        alive = false;
    }

    @Override
    public void deleteId(int id)
    {

        sendMessage("deleteId", id);
    }
}