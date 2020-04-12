package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.middleware.Client;
import it.polimi.ingsw.view.middleware.Server;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class IntegrationCommunicationTest {

    private ExecutorService executor = Executors.newFixedThreadPool(128);

    //tests that one view can correctly request and receive its id
    //runs the server and connects one view
    //this test is very significative because it is dependent on the entire chain of observer/observable patterns
    //to work, and on the network connection between Client and Server
    @Test
    public void loopTest(){

        int port = 40000;
        Server server = buildAndRunServer(port);
        Client client = buildAndRunClient(port);
        ClientView cw = client.getClientView();
        try{
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e)
        {
            System.err.println(e.getMessage());
        }
        assertEquals(0, cw.getID());
    }


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
}
