package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable
{
    private static int PORT;
    private final ServerSocket serverSocket;
    private Controller controller;

    private final List<View> views = new ArrayList<View>();
    private final List<Connection> cons = new ArrayList<Connection>();

    private boolean idSet = false;
    private final Object idSetLock = new Object();

    private boolean nextClientIsReady = false;
    private final Object clientReadyLock = new Object();

    private int nextClientIn = 0;

    public Server(int port) throws IOException
    {
        PORT = port;
        this.serverSocket = new ServerSocket(PORT);
    }

    /**
     * Invoked by the connection of a player sending an ACK of his newly-received ID.
     * The main purpose of this method is awakening the waiting thread in run() that handles the sequential access to the game.
     */
    public void registerIdAck()
    {
        synchronized (idSetLock)
        {
            idSet = true;
            idSetLock.notify();
        }
    }

    /**
     * Adds the connection to the list, associating it with the relative virtual view.
     * @param c the connection just created between a new client and the server.
     */
    public synchronized void register(Connection c)
    {
        cons.add(c);
        final View view = new VirtualView(c);
        views.add(view);
        c.setView(view);

        // in the case of the first player, runs the setup on the controller's side (to ask the number of players)
        if(cons.size()==1)
        {
            controller = new Controller();
            letClientIn();
        }
        else
        {
            synchronized (clientReadyLock)
            {
                nextClientIsReady = true;
                clientReadyLock.notify();
            }
        }
    }

    /**
     * Method used to force the sequential access to the game, in order to assign the IDs properly.
     * While the connections and views are collected by the server as the socket is accepted, the controller receives the views only when this method is invoked.
     */
    public void letClientIn()
    {
        controller.addView(views.get(nextClientIn));
        views.get(nextClientIn).setController(controller);
        nextClientIn++;
    }

    /**
     * Handles the incoming connection requests and runs the thread that manage the access of clients to the game.
     */
    public void run()
    {
        System.out.println("Server listening on port: " + PORT);

        new Thread(() -> {
            synchronized (idSetLock)
            {
                try
                {
                    while(true)
                    {
                        while(!idSet)
                        {
                            idSetLock.wait();
                        }

                        synchronized (clientReadyLock)
                        {
                            while(!nextClientIsReady)
                            {
                                clientReadyLock.wait();
                            }
                        }

                        letClientIn();
                        idSet = false;

                        if(views.size() == nextClientIn )
                            nextClientIsReady = false;
                    }
                }
                catch( InterruptedException e)
                {
                    System.err.println("InterruptedException del thread di Server");
                }

            }
        }).start();

        // accepts all connections runs a separate thread for all of them
        while(true)
        {
            try
            {
                synchronized (cons)
                {
                    Socket socket = serverSocket.accept();

                    final Connection connection = new Connection(socket, this);
                    new Thread(() -> connection.run()).start();

                    register(connection);
                }
            }
            catch (IOException e)
            {
                System.err.println("Connection error!");
            }
        }
    }
}
