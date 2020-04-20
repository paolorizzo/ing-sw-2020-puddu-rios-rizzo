package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.observation.Observable;
import it.polimi.ingsw.view.View;

import java.lang.reflect.Method;

import java.net.Socket;
import java.io.*;
import java.util.*;

public class Connection extends Messenger implements Runnable
{
    private final Socket socket;
    private final Server server;

    private View view;

    public Connection(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
        this.view = null;
    }

    /**
     * Called by the server when setting the relative virtual view.
     * Handles the construction of the method map, that has to happen here as the virtual view contains the observables that will send the notifies.
     * @param view the virtual view associated to this connection.
     */
    public void setView(View view)
    {
        this.view = view;
    }

    //always returns the controller, regardless of the methodName
    //this happens because the communication from on object that implements
    //the View interface to an object that implements the ControllerInterface
    //happens directly, and not through an observable class
    protected Object getObservable(String methodName) {
        return view.getController();
    }

    /**
     * Delegate the actual sending process to the superclass Messenger.
     * Avoids the need for the caller to know the correct output stream.
     * @param methodName the name of the method triggered by the message.
     * @param arg the object representing the eventual arguments to be passed.
     */
    public void sendMessage(String methodName, Object ...arg)
    {
        try
        {
            super.sendMessage(new ObjectOutputStream(socket.getOutputStream()), methodName, arg);
        }
        catch (IOException e)
        {
            System.err.println("Error in creating output socket");
            e.printStackTrace();
        }
    }

    // TODO test
    /**
     * Runs in a separate thread on the server's side, handling incoming communications from the client to the server.
     */
    public void run()
    {
        ObjectInputStream ByteIn;

        while(true)
        {
            try
            {
                ByteIn = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ByteIn.readObject();
                callMethod(message);
                if(message.getMethodName().equals("ackId"))
                {
                    server.registerIdAck();
                }
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
