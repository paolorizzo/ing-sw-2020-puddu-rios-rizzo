package it.polimi.ingsw;

import it.polimi.ingsw.view.middleware.Client;
import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MvcIntegrationTest {
    protected ExecutorService executor = Executors.newFixedThreadPool(128);

    //constructs the server and runs it in a thread
    //also returns the server
    public Server buildAndRunServer(int port){

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

    //constructs the client and runs it in a thread
    //also returns the client
    public Client buildAndRunClient(int port){
        Client client = new Client("127.0.0.1", port);
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

    public void safeWaitFor(int seconds)
    {
        try{
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch(InterruptedException e)
        {
            System.err.println(e.getMessage());
        }
    }

}
