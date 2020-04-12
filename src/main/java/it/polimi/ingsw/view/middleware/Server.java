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
    private ServerSocket serverSocket;
    private Controller controller;

    private final List<View> views = new ArrayList<View>();
    private final List<Connection> cons = new ArrayList<Connection>();

    private int numberOfPlayers = 1;
    private boolean numberOfPlayerIsSet = false;
    private Object numberOfPlayersLock = new Object();

    private boolean idSet = false;
    private final Object idSetLock = new Object();

    private boolean nextClientIsReady = false;
    private final Object clientReadyLock = new Object();

    private int nextClientIn = 0;

    private boolean gameIsOn = false;

    public Server(int port) throws IOException
    {
        PORT = port;
        this.serverSocket = new ServerSocket(PORT);
    }

    /**
     * Invoked by the connection of the first player when the user inputs the desired number of players
     * @param n The desired number of players for the upcoming match
     */
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

    public void registerIdAck()
    {
        synchronized (idSetLock)
        {
            idSet = true;
            idSetLock.notify();
        }
    }

    public int getNumberOfPlayers()
    {
        return numberOfPlayers;
    }

    public boolean isGameOn()
    {
        return gameIsOn;
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
            controller = Controller.instance();
            /*
            //queste due righe di codice sono alternative a letClientIn()
            controller.addView(view);
            views.get(0).addObserver(controller);
             */
            letClientIn();
            //controller.setup();
        }
        else
        {
            synchronized (clientReadyLock)
            {
                nextClientIsReady = true;
                clientReadyLock.notify();
            }
        }

        // if the number of players is set before the desired number of players have connected, this awakes the waiting thread started in run()
        synchronized (numberOfPlayersLock)
        {
            if(numberOfPlayerIsSet && cons.size() == numberOfPlayers)
                numberOfPlayersLock.notify();
        }

        System.out.println("Registered connection n. "+(views.size()));
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

            if(numberOfPlayers < views.size())
            {
                //TODO handle this communication inside new paradigm
                //this will require some thought given that all the views receive the same
                //notifies, and given that a new view might not have an ID
                //views.get(2).startOutOfGameView();
            }

            //controller.start();
            numberOfPlayerIsSet = false; // why?
            gameIsOn = true;
        }
    }

    public void letClientIn()
    {
        System.out.println("Lascio entrare il prossimo client");
        controller.addView(views.get(nextClientIn));
        views.get(nextClientIn).addObserver(controller);
        nextClientIn++;
    }

    public int getNumberOfConnections()
    {
        return cons.size();
    }

    // handles the incoming connection requests
    public void run()
    {
        System.out.println("Server listening on port: " + PORT);

        /*
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

         */

        new Thread(new Runnable()
        {
            @Override
            public void run() {
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
            }
        }).start();


        // accepts and registers the first 3 connections, runs a separate thread for all of them
        while(true)
        {
            try
            {
                synchronized (cons)
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

                    //how to block connections
                    /*
                    if(cons.size() < 3)
                    {

                    }
                    else
                    {
                        serverSocket.close();
                    }

                     */
                }
            }
            catch (IOException e)
            {
                System.err.println("Connection error!");
            }
        }
    }
}
