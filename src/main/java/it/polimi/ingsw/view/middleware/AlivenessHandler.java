package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.view.View;

import java.sql.Timestamp;

public class AlivenessHandler
{
    private final Messenger messenger;
    private final View view;
    private final Object liveLock;
    private boolean clientIsLive;
    private boolean monitoring;
    private int failures;


    private final int livenessRate = 10000;


    public AlivenessHandler(Messenger messenger, View view)
    {
        this.messenger = messenger;
        this.view = view;
        this.liveLock = new Object();
        this.monitoring = true;
        this.failures = 0;
    }

    /**
     * Accepts the pong message from the client only if the associated timestamp is reasonably close to the current time.
     * In that case, the aliveness flag gets updated to acknowledge it.
     * @param message the "pong" message.
     */
    void pongHandler(Message message)
    {
        synchronized(liveLock)
        {
            clientIsLive = true;
        }
    }

    void registerMessageFailure()
    {
        synchronized (liveLock)
        {
            if(monitoring)
            {
                //System.err.println("Registering an aliveness failure.");
                failures++;

                if(failures == 1)
                {
                    registerDisconnection();
                }
            }
        }
    }

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
                        //System.err.println("The other side of the network is not responding.");
                        view.connectionLost();
                        monitoring = false;
                    }
                    else
                        clientIsLive = false;
                }
            }
        }).start();
    }

    void registerDisconnection()
    {
        this.monitoring = false;
        view.connectionLost();
    }

    void stopMonitoringLiveness()
    {
        this.monitoring = false;
    }

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

