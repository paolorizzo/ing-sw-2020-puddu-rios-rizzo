package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    private static final int PORT= 42069;
    private ServerSocket serverSocket;
    private Controller controller;

    private final List<View> views = new ArrayList<View>();
    private final List<Connection> cons = new ArrayList<Connection>();

    private int numberOfPlayers = 1;
    private boolean numberOfPlayerIsSet = false;
    private Object numberOfPlayersLock = new Object();

    public Server() throws IOException
    {
        this.serverSocket = new ServerSocket(PORT);
    }

    // invoked by the connection of the first player when the user inputs the desired number of players
    public void setNumberOfPlayers(int n)
    {
        // notifies the waiting thread started in run()
        synchronized (numberOfPlayersLock)
        {
            this.numberOfPlayers = n;
            this.numberOfPlayerIsSet = true;
            numberOfPlayersLock.notify();
        }
    }

    public int getNumberOfPlayers()
    {
        return numberOfPlayers;
    }

    // adds the connection to the list, associating it with the relative virtual view
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
            controller.addView(view);
            views.get(0).addObserver(controller);
            //controller.setup();
        }

        // if the number of players is set before the desired number of players have connected, this awakes the waiting thread started in run()
        synchronized (numberOfPlayersLock)
        {
            if(numberOfPlayerIsSet && cons.size() == numberOfPlayers)
                numberOfPlayersLock.notify();
        }
    }

    // adds to the game only the first n connections in the list, with n being the desired number of players
    // starts the game on the controller's side
    public void startGame()
    {
        if(numberOfPlayerIsSet && cons.size() >= numberOfPlayers)
        {
            for (int i = 1; i < numberOfPlayers; i++)
            {
                controller.addView(views.get(i));
                views.get(i).addObserver(controller);
            }

            //controller.start();
            numberOfPlayerIsSet = false;
        }
    }

    public int getNumberOfConnections()
    {
        return cons.size();
    }

    // handles the incoming connection requests
    public void run()
    {
        System.out.println("Server listening on port: " + PORT);

        // handles the connections waiting for the game to start
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                synchronized (numberOfPlayersLock)
                {
                    try
                    {
                        // waits until the first player sets the desired number of players
                        while(!numberOfPlayerIsSet)
                        {
                            numberOfPlayersLock.wait();
                        }

                        // waits until all the players connect to the server
                        while(cons.size()<numberOfPlayers)
                        {
                            numberOfPlayersLock.wait();
                        }

                        startGame();
                    }
                    catch( InterruptedException e)
                    {
                        System.err.println("InterruptedException del thread di Server");
                    }

                }
            }
        }).start();

        // accepts all sockets requests and registers all the connections, and runs a separate thread for all of them
        while(true)
        {
            try
            {
                Socket socket = serverSocket.accept();

                final Connection connection = new Connection(socket, this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.run();
                    }
                }).start();

                register(connection);
            }
            catch (IOException e)
            {
                System.err.println("Connection error!");
            }
        }
    }
}
