package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.view.View;

import java.sql.Timestamp;

/**
 * Contains all the utilities related to the monitoring of aliveness on the network.
 */
public class AlivenessHandler
{
    private final Messenger messenger;
    private final View view;
    private final Object liveLock;
    private boolean clientIsLive;
    private boolean monitoring;
    private final int livenessRate = 10000;

    /**
     * Constructs an instance of the aliveness handler used by an agent operating in the middleware.
     * @param messenger a reference to the Messenger instance used by the agent.
     * @param view a reference to the view associated to the agent.
     */
    public AlivenessHandler(Messenger messenger, View view)
    {
        this.messenger = messenger;
        this.view = view;
        this.liveLock = new Object();
        this.monitoring = true;
    }

    /**
     * Accepts the pong message from the client and makes sure to acknowledge the aliveness of the sender.
     * @param message the "pong" message.
     */
    void pongHandler(Message message)
    {
        synchronized(liveLock)
        {
            clientIsLive = true;
        }
    }

    /**
     * Invoked when errors occur while sending a message via socket.
     * Starts the disconnection procedure.
     */
    void registerMessageFailure()
    {
        synchronized (liveLock)
        {
            if(monitoring)
            {
                registerDisconnection();
            }
        }
    }

    /**
     * Invoked when the agent needs to start monitoring the aliveness of the other side of the network.
     */
    protected void startMonitoringLiveness()
    {
        new Thread(() -> {

            while(monitoring)
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
                    if((!clientIsLive) && (monitoring))
                    {
                        view.connectionLost();
                        monitoring = false;
                    }
                    else
                        clientIsLive = false;
                }
            }
        }).start();
    }

    /**
     * First step of the disconnection procedure.
     * Stops the monitoring process and informs the view about the disconnection.
     */
    void registerDisconnection()
    {
        this.monitoring = false;
        view.connectionLost();
    }

    /**
     * Stops the monitoring process checking the aliveness of the other side of the connection.
     */
    void stopMonitoringLiveness()
    {

        this.monitoring = false;
    }

    /**
     * Invoked when the agent needs to start signaling its aliveness status to the other side of the network.
     */
    protected void startPing() {
        new Thread(() -> {
            while (monitoring)
            {
                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }

                messenger.sendMessage("pong", new Timestamp(System.currentTimeMillis()));
            }
        }).start();
    }
}

