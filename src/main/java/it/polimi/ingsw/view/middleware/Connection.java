package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.view.View;

import java.net.Socket;
import java.io.*;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Connection extends Messenger implements Runnable
{
    private final Socket socket;
    private final Server server;
    private View view;
    private final MessageSynchronizer synchronizer;
    private AlivenessHandler alivenessHandler;

    private final Object liveLock;
    private boolean clientIsLive;

    private final int livenessRate = 5000;
    private final int invalidPongTreshold = 10000;

    private final Object messageSynchronizer;
    private final List<Message> messageQueue;

    public Connection(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
        this.view = null;
        this.liveLock = new Object();
        this.clientIsLive = false;
        this.messageSynchronizer = new Object();
        this.messageQueue = new ArrayList<>();
        this.synchronizer = new MessageSynchronizer(this);
    }

    /**
     * Called by the server when setting the relative virtual view.
     * Handles the construction of the method map, that has to happen here as the virtual view contains the observables that will send the notifies.
     * @param view the virtual view associated to this connection.
     */
    public void setView(View view)
    {
        this.view = view;
        this.alivenessHandler = new AlivenessHandler(this, view);
        alivenessHandler.startPing();
        alivenessHandler.startMonitoringLiveness();
    }

    /**
     * Always returns the controller, regardless of the methodName.
     * This happens because the communication from on object that implements the View interface to an object that implements the ControllerInterface happens directly, and not through an observable class.
     * @param methodName the useless parameter...
     * @return the controller.
     */
    protected Object getObservable(String methodName)
    {

        return view.getController();
    }

    /**
     * Delegate the actual sending process to the superclass Messenger.
     * Avoids the need for the caller to know the correct output stream.
     * @param methodName the name of the method triggered by the message.
     * @param arg the object representing the eventual arguments to be passed.
     */
    @Override
    public synchronized void sendMessage(String methodName, Object ...arg)
    {
        try
        {
            super.sendMessage(new ObjectOutputStream(socket.getOutputStream()), methodName, arg);
        }
        catch (IOException e)
        {
            if(view !=null)
            {
                alivenessHandler.registerMessageFailure();
                //view.connectionLost();
            }
        }
    }

    //TODO handle the client disconnecting
    /**
     * Runs in a separate thread on the server's side, handling incoming communications from the client to the server.
     * Creates and runs the thread checking periodically the client's aliveness and the one that actually executes the calls on
     * the the virtual view, according to the message queue.
     */
    public void run()
    {

        synchronizer.run();

        //tha main loop listening for messages on the socket
        ObjectInputStream ByteIn;
        while(true)
        {
            try
            {
                ByteIn = new ObjectInputStream(socket.getInputStream());
                Message currentMessage = (Message) ByteIn.readObject();
                if(currentMessage.getMethodName().equals("ackId"))
                    server.registerIdAck();
                if(currentMessage.getMethodName().equals("pong"))
                    alivenessHandler.pongHandler(currentMessage);
                else
                    synchronizer.enqueueMessage(currentMessage);
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

    /**
     * Closes the socket connection.
     */
    private void close()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

}
