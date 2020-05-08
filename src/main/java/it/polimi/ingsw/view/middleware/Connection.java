package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.view.View;

import java.net.Socket;
import java.io.*;
import java.sql.Timestamp;

public class Connection extends Messenger implements Runnable
{
    private final Socket socket;
    private final Server server;

    private View view;

    private final Object liveLock;
    private boolean clientIsLive;

    private final Object messageLock;
    private boolean newMessageReceived;

    private final int livenessRate = 5000;
    private final int invalidPongTreshold = 10000;

    public Connection(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
        this.view = null;
        this.liveLock = new Object();
        this.clientIsLive = false;
        this.messageLock = new Object();
        this.newMessageReceived = false;
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

    /**
     * Accepts the pong message from the client only if the associated timestamp is reasonably close to the current time.
     * In that case, the aliveness flag gets updated to acknowledge it.
     * @param message the "pong" message.
     */
    private void pongHandler(Message message)
    {
        //filter pongs upon timestamps
        if(message.hasArgs() && message.getArg(0) instanceof Timestamp)
        {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp ts = (Timestamp) message.getArg(0);

            if(now.getTime()-ts.getTime()<invalidPongTreshold)
            {
                synchronized(liveLock)
                {
                    clientIsLive = true;
                }
            }

        }
    }

    /**
     * Filters the messages incoming from the client.
     * Useful for the messages that do not automatically translate in a correspondent method in the virtual view and have to be handled by the middleware.
     * @param message the message that needs to be handled.
     */
    private void filterMessages(Message message)
    {
        if(message.getMethodName().equals("pong"))
        {
            pongHandler(message);
        }
        else
        {
            synchronized(liveLock)
            {
                callMethod(message);
            }

            if(message.getMethodName().equals("ackId"))
            {
                server.registerIdAck();
            }
        }
    }

    //TODO handle the client disconnecting
    /**
     * Runs in a separate thread on the server's side, handling incoming communications from the client to the server.
     * Creates and runs the thread checking periodically the client's liveness.
     */
    public void run()
    {
        new Thread(() -> {

            while(true)
            {
                try
                {
                    Thread.sleep(livenessRate);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }

                synchronized (liveLock)
                {
                    if(!clientIsLive)
                        System.out.println("Client not reachable "+this);
                    else
                        clientIsLive = false;
                }
            }
        }).start();

        ObjectInputStream ByteIn;
        while(true)
        {
            try
            {
                ByteIn = new ObjectInputStream(socket.getInputStream());
                filterMessages((Message) ByteIn.readObject());
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
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
