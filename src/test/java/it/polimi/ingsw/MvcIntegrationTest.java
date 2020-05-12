package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.middleware.Client;
import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MvcIntegrationTest
{
    protected ExecutorService executor = Executors.newFixedThreadPool(256);

    /**
     * Util to construct and run the server in a separate thread.
     * @param port the reference port fot the server
     * @return the running server
     */
    public Server buildAndRunServer(int port)
    {
        Server server;
        try
        {
            server = new Server(port);
            executor.submit(server);
            return server;
        }
        catch (IOException e)
        {
            System.err.println("Impossible to start the server!\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Util to construct and run a client in a separate thread.
     * @param port the port of the server to connect to
     * @return the running client
     */
    public Client buildAndRunClient(int port)
    {
        Client client = new Client("127.0.0.1", port);
        ClientView cw = new ClientView(client);
        client.setClientView(cw);

        Cli cli = new Cli();
        cw.setUi(cli);
        cli.addObserver(cw);
        try
        {
            executor.submit(client);
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
        }
        return client;
    }

    /**
     * Util to wait for a certain amount of seconds in a safe way
     * @param seconds the desired waiting time expressed in seconds
     */
    public void safeWaitFor(int seconds)
    {
        try
        {
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch(InterruptedException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
