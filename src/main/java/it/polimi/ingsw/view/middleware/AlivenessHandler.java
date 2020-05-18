package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.view.View;

import java.sql.Timestamp;

public class AlivenessHandler
{
    private final Messenger messenger;
    private final View view;
    private final Object liveLock;
    private boolean clientIsLive;
    private boolean alive;


    private final int livenessRate = 5000;
    private final int invalidPongTreshold = 10000;


    public AlivenessHandler(Messenger messenger, View view)
    {
        this.messenger = messenger;
        this.view = view;
        this.liveLock = new Object();
        this.alive = true;
    }

    /**
     * Accepts the pong message from the client only if the associated timestamp is reasonably close to the current time.
     * In that case, the aliveness flag gets updated to acknowledge it.
     * @param message the "pong" message.
     */
    void pongHandler(Message message)
    {
        //filter pongs upon timestamps
        //System.out.println("received pong!");
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

    protected void startMonitoringLiveness()
    {
        new Thread(() -> {

            while(alive)
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
                    if(!clientIsLive && alive)
                    {
                        view.connectionLost();
                        alive = false;
                    }
                    else
                        clientIsLive = false;
                }
            }
        }).start();
    }

    void registerDisconnection()
    {
        this.alive = false;
    }

    protected void startPing() {
        new Thread(() -> {
            while (alive) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                messenger.sendMessage("pong", new Timestamp(System.currentTimeMillis()));
            }
        }).start();
    }
}

