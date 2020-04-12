package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.observation.Observable;
import it.polimi.ingsw.observation.RequestsObserver;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.net.Socket;
import java.io.*;
import java.util.*;

public class Connection extends Messenger implements Runnable
{
    private Socket socket;

    private Server server;
    private View view;

    private ObjectOutputStream outByte;
    private ObjectInputStream ByteIn;


    public Connection(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;

        this.view = null;
        methodMap = null;
    }

    // called by the server, when setting the relative virtual view
    //constructs the methodMap
    //this construction requires the virtualView because it contains the observables that will
    //call the notifies
    public void setView(View view)
    {
        this.view = view;
        methodMap = constructMethodMap();
    }

    //TODO construction of the map could be handled in a better way?
    //constructs the map between method names and observable objects in the view
    //called when the view is set, because it needs a reference to the observable objects
    public Map<String, Observable> constructMethodMap(){

        Map<String, Observable> methodMap = new HashMap<String, Observable>();

        Method[] requestMethods = view.getViewRequestsFeed().getClass().getMethods();
        Method[] gameMethods = view.getViewGameFeed().getClass().getMethods();
        Method[] playersMethods = view.getViewPlayersFeed().getClass().getMethods();

        for(Method method:requestMethods) {
            methodMap.put(method.getName(), view.getViewRequestsFeed());
            methodMap.put(toNotify(method.getName()), view.getViewRequestsFeed());
        }
        for(Method method:gameMethods) {
            methodMap.put(method.getName(), view.getViewGameFeed());
            methodMap.put(toNotify(method.getName()), view.getViewGameFeed());
        }
        for(Method method:playersMethods) {
            methodMap.put(method.getName(), view.getViewPlayersFeed());
            methodMap.put(toNotify(method.getName()), view.getViewPlayersFeed());
        }

        return methodMap;
    }

    //avoids the need for the caller to know the correct output stream
    public void sendMessage(String methodName, Object ...arg){
        try{
            super.sendMessage(new ObjectOutputStream(socket.getOutputStream()), methodName, arg);
        }
        catch (IOException e){
            System.err.println("Error in creating output socket");
        }
    }


    // TODO test
    // this runs in a separate thread on the server's side
    // handles communications from the client to the server
    public void run()
    {
        // moved the declaration of ByteIn from here. Problems?

        // intercepts the updates coming from client view and mirrors a notify call on the relative virtual view
        // in the case of the setup phase, sets the desired number of players on server's side
        while(true)
        {
            try
            {
                ByteIn = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ByteIn.readObject();

                System.out.println("Messaggio in arrivo! Message method: "+message);
                callMethod(message);
                //TODO intercept message to discover numPlayers
                /*
                if(message.getMethodName().equals("update"))
                {
                    view.notify(message.getArg(0));
                    if (message.getArg(0).equals("2Players"))
                    {
                        server.setNumberOfPlayers(2);
                    }
                    else if (message.getArg(0).equals("3Players"))
                    {
                        server.setNumberOfPlayers(3);
                    }
                }
                */

            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Error in reading the object");
            }
            catch (IOException e)
            {
                close();
            }

        }
    }

    private void close()
    {
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
