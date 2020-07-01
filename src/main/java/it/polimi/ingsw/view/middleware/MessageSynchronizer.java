package it.polimi.ingsw.view.middleware;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the queue of messages to be executed on a node of the architecture.
 */
public class MessageSynchronizer
{
    private final Object messageSynchronizer;
    private final List<Message> messageQueue;
    private final Messenger messenger;

    /**
     * Constructs the synchronizer.
     * @param messenger the Messenger instance used by the agent.
     */
    public MessageSynchronizer(Messenger messenger)
    {
        this.messenger = messenger;
        this.messageQueue = new ArrayList<>();
        this.messageSynchronizer = new Object();
    }

    /**
     * Adds a message to the FIFO waiting queue. Each message will be later executed in a separate thread.
     * @param message the newly received message to be added to the queue.
     */
    void enqueueMessage(Message message)
    {
        synchronized(messageSynchronizer)
        {
            messageQueue.add(message);
            messageSynchronizer.notify();
        }
    }

    /**
     * Runs the main thread handling the enqueued messages.
     */
    public void run()
    {
        new Thread(() -> {
            try
            {
                while(true)
                {
                    Message currentMessage;
                    synchronized (messageSynchronizer)
                    {
                        while(messageQueue.size()==0)
                            messageSynchronizer.wait();
                    }

                    currentMessage = messageQueue.get(0);
                    messenger.callMethod(currentMessage);

                    synchronized (messageSynchronizer)
                    {
                        messageQueue.remove(0);
                    }

                }
            }
            catch(InterruptedException e)
            {
                System.err.println("There is a problem in the message synchronizer");
            }

        }).start();
    }
}
