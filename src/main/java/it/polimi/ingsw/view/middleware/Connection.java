package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.view.View;

import java.net.Socket;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Connection extends Messenger implements Runnable
{
    private final Socket socket;
    private final Server server;

    private View view;

    private final Object liveLock;
    private boolean clientIsLive;

    private final int livenessRate = 5000;
    private final int invalidPongTreshold = 10000;

    private final Object messageSynchronizer;
    private List<Message> messageQueue;

    public Connection(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
        this.view = null;
        this.liveLock = new Object();
        this.clientIsLive = false;
        this.messageSynchronizer = new Object();
        this.messageQueue = new ArrayList<>();
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
    private synchronized void filterMessages(Message message)
    {
        if(message.getMethodName().equals("pong"))
        {
            pongHandler(message);
        }
        else
        {
            enqueueMessage(message);
        }
    }

    private void enqueueMessage(Message message)
    {
        synchronized(messageSynchronizer)
        {
            messageQueue.add(message);
            messageSynchronizer.notify();
        }
    }

    //TODO handle the client disconnecting
    /**
     * Runs in a separate thread on the server's side, handling incoming communications from the client to the server.
     * Creates and runs the thread checking periodically the client's aliveness.
     */
    public void run()
    {
        //the thread checking for aliveness
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

        //the thread executing the calls on the virtual view sequentially
        new Thread(() -> {
            try
            {
                while(true)
                {
                    synchronized (messageSynchronizer)
                    {
                        while(messageQueue.size()==0)
                            messageSynchronizer.wait();

                        callMethod(messageQueue.get(0));

                        if (messageQueue.get(0).getMethodName().equals("ackId"))
                            server.registerIdAck();

                        messageQueue.remove(0);
                    }
                }
            }
            catch(InterruptedException e)
            {
                System.err.println("There is a problem in the message synchronizer");
            }

        }).start();

        //tha main loop listening for messages on the socket
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
