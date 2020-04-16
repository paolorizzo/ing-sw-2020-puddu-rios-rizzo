package it.polimi.ingsw.view.middleware;

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

//TODO this client does not correctly implement observer/observable pattern
public class Client extends Messenger implements ViewObserver, Runnable {
    private Socket socket;
    private final String ip;
    private final int port;

    private final ClientView cw;

    private final GameObservable virtualGameFeed;
    private final PlayersObservable virtualPlayersFeed;

    public Client(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        this.cw = new ClientView();
        this.cw.addObserver(this);
        virtualGameFeed = new GameObservable();
        virtualPlayersFeed = new PlayersObservable();
        addObserver(cw);
        methodMap = constructMethodMap();
    }

    public ClientView getClientView()
    {

        return cw;
    }

    public void addObserver(ModelObserver view)
    {
        virtualGameFeed.addObserver(view);
        virtualPlayersFeed.addObserver(view);
    }

    protected Map<String, Observable> constructMethodMap()
    {

        Map<String, Observable> methodMap = new HashMap<String, Observable>();

        Method[] gameMethods = virtualGameFeed.getClass().getMethods();
        Method[] playersMethods = virtualPlayersFeed.getClass().getMethods();

        for(Method method:gameMethods) {
            methodMap.put(method.getName(), virtualGameFeed);
            methodMap.put(toNotify(method.getName()), virtualGameFeed);
        }
        for(Method method:playersMethods) {
            methodMap.put(method.getName(), virtualPlayersFeed);
            methodMap.put(toNotify(method.getName()), virtualPlayersFeed);
        }

        return methodMap;
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
                System.out.println("Connected to the server");
                waitingForServer = false;

                //starts the clientView therefore initiating the message exchange
                cw.start();

                ObjectInputStream ByteIn;

                while(true)
                {
                    try {
                        ByteIn = new ObjectInputStream(socket.getInputStream());
                        Message message = (Message) ByteIn.readObject();
                        //System.out.println("Messaggio in arrivo sul client! Message method: "+message);
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
    @Override
    public void updateRequestID()
    {
        sendMessage("updateRequestID");
    }

    @Override
    public void updateAckID(int id)
    {

        sendMessage("updateAckID", id);
    }

    @Override
    public void updateRequestNumPlayers()
    {
        sendMessage("updateRequestNumPlayers");
    }

    @Override
    public void updateNumPlayers(int numPlayers)
    {
        sendMessage("updateNumPlayers", numPlayers);
    }

    @Override
    public void updateID(int id)
    {
        sendMessage("updateID");
    }

    @Override
    public void updateName(int id, String name)
    {
        sendMessage("updateName");
    }
}