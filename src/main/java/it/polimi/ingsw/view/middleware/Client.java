package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.observation.*;
import it.polimi.ingsw.observation.Observable;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.*;
import java.lang.reflect.Method;

//TODO this client does not correctly implement observer/observable pattern
public class Client extends Messenger implements ViewObserver, Runnable {
    private Socket socket;
    private String ip;
    private int port;

    private ClientView cw;

    private ObjectInputStream ByteIn;
    private ObjectOutputStream outByte;

    private GameObservable virtualGameFeed;
    private PlayersObservable virtualPlayersFeed;

    private Map<String, Observable> methodMap;

    public Client(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        this.cw = ClientView.instance();
        this.cw.addObserver(this);
        methodMap = constructMethodMap();
    }



    /* called by run()
    // gets the method name and a list or arguments, invokes the corrisponding method on the relative client view
    private void callMethod(String name, List<Object> args)
    {
        Method[] viewMethods = cw.getClass().getMethods();

        for(Method m : viewMethods)
        {
            if(m.getName().equals(name)) {

                //TODO: check the correspondence of arguments

                Object[] methodArgs = new Object[args.size()];
                for (int i = 0; i < arg.size(); i++)
                    methodArgs[i] = arg.get(i);

                try
                {
                    m.invoke(cw, methodArgs);
                }
                catch(IllegalAccessException e)
                {
                    System.err.println("Error in invoking the method, IllegalAccessException");
                }
                catch(InvocationTargetException e)
                {
                    System.err.println("Error in invoking the method, IllegalTargetException");
                }
                catch(NullPointerException e)
                {
                    System.err.println("Error in invoking the method, NullPointerException");
                }
            }
        }
    }

    // client gets the updates coming from client view and directed to the controller
    // un update is represented by a Message calling the update method on the controller
    // the message object is serialized and sent through the socket stream
    public void sendMessage(String methodName, Object o)
    {
        // moved the declaration of outByte from here. problems?

        Message m = new Message(methodName);
        m.addArg(o);
        try
        {
            outByte = new ObjectOutputStream(socket.getOutputStream());
            outByte.writeObject(m);
            outByte.flush();
        }
        catch(IOException e)
        {
            System.err.println("Errore IOE nell'update del Client");
        }
    }

    //this method overrides sendMessage to handle messages without arguments
    public void sendMessage(String methodName)
    {
        // moved the declaration of outByte from here. problems?

        Message m = new Message(methodName);
        try
        {
            outByte = new ObjectOutputStream(socket.getOutputStream());
            outByte.writeObject(m);
            outByte.flush();
        }
        catch(IOException e)
        {
            System.err.println("Errore IOE nell'update del Client");
        }
    }

     */

    protected Map<String, Observable> constructMethodMap(){

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

    // handles the messages coming from the stream, de-serializing and calling callMethod
    public void run()
    {
        try
        {
            socket = new Socket(ip, port);
            System.out.println("Connected to the server");

            //starts the clientView therefore initiating the message exchange
            cw.start();

            // moved the declaration of byteIn from here. Problems?

            while(true)
            {
                try {
                    ByteIn = new ObjectInputStream(socket.getInputStream());
                    Message message = (Message) ByteIn.readObject();
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
            System.err.println("Error in creating the client socket");
        }
    }

    public void sendMessage(String methodName, Object ...arg){
        try{
            super.sendMessage(new ObjectOutputStream(socket.getOutputStream()), methodName, arg);
        }
        catch (IOException e){
            System.err.println("Error in creating output socket");
        }
    }

    //TODO test updates inside Client
    @Override
    public void updateRequestID(){
        sendMessage("updateRequestID");
    }

    @Override
    public void updateRequestNumPlayers(){
        sendMessage("updateRequestNumPlayers");
    }

    @Override
    public void updateNumPlayers(int numPlayers){
        sendMessage("updateNumPlayers");
    }

    @Override
    public void updateID(int id){
        sendMessage("updateID");
    }
    @Override
    public void updateName(int id, String name){
        sendMessage("updateName");
    }
}