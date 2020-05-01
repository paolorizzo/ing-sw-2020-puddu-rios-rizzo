package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.SetupAction;
import it.polimi.ingsw.model.Sex;
import it.polimi.ingsw.observation.*;
import it.polimi.ingsw.observation.Observable;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class Client extends Messenger implements ControllerInterface, Runnable {
    private Socket socket;
    private final String ip;
    private final int port;

    private ClientView cw;

    private final FeedObservable virtualFeed;

    private boolean alive;

    public Client(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        virtualFeed = new FeedObservable();
        alive = true;
    }
    public void setClientView(ClientView cw){
        this.cw = cw;
        addObserver(cw);
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
    protected Object getObservable(String methodName) {
        return virtualFeed;
    }

    /**
     * Handles the messages coming from the stream (the server), de-serializing and calling callMethod.
     */
    public void run()
    {
        boolean waitingForServer = true;
        while(waitingForServer)
        {
            try
            {
                socket = new Socket(ip, port);
                //System.out.println("Connected to the server");
                waitingForServer = false;

                //starts the clientView therefore initiating the message exchange
                cw.start();

                ObjectInputStream ByteIn;

                while(alive)
                {
                    try {
                        ByteIn = new ObjectInputStream(socket.getInputStream());
                        Message message = (Message) ByteIn.readObject();
                        //System.out.println("Messaggio in arrivo sul client! Message method: "+message);
                        //System.out.println(message.getMethodName());
                        callMethod(message);
                    }
                    catch (ClassNotFoundException e)
                    {
                        System.err.println("Error in reading the object");
                    }
                }
            }
            catch(IOException e)
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

    public void sendMessage(String methodName, Object ...arg)
    {
        try{
            super.sendMessage(new ObjectOutputStream(socket.getOutputStream()), methodName, arg);
        }
        catch (IOException e){
            System.err.println("Error in creating output socket");
        }
    }

    //TODO test updates inside Client

    //Connection phase methods used for communication to the controller

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
    public  void requestAllPlayersConnected() {sendMessage("requestAllPlayersConnected");};
    @Override
    public void setName(int id, String name)
    {
        sendMessage("setName", id, name);
    }

    @Override
    public void requestDeck() {
        sendMessage("requestDeck");
    }

    @Override
    public void publishCards(int id, List<Integer> numCards) {
        sendMessage("publishCards", id, numCards);
    }

    @Override
    public void requestCards(int id) {
        sendMessage("requestCards", id);
    }

    @Override
    public void setCard(int id, int numCard) {
        sendMessage("setCard", id, numCard);
    }
    @Override
    public void requestToSetupWorker(int id) {
        sendMessage("requestToSetupWorker", id);
    }
    @Override
    public void setupWorker(int id, SetupAction setupAction) {
        sendMessage("setupWorker", id, setupAction);
    }
    @Override
    public void requestActions(int id) { sendMessage("requestActions", id);}
    @Override
    public void publishAction(int id, Action action) {sendMessage("publishAction", id, action);}
    @Override
    public void publishVoluntaryEndOfTurn(int id) {sendMessage("publishVoluntaryEndOfTurn", id);}
    @Override
    public void kill(){
        alive = false;
    }

    @Override
    public void deleteId(int id){
        sendMessage("deleteId", id);
    }
}