package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.ConnectionState;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.middleware.Client;
import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Server;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class IntegrationCommunicationTest extends MvcIntegrationTest {

    int port = 50000;

    //tests that one view can correctly receive a start signal, request and receive its id
    //runs the server and connects one view
    //this test is very significative because it is dependent on the entire chain of observer/observable patterns
    //to work, and on the network connection between Client and Server
    //@Test
    public void loopTest(int port){

        Server server = buildAndRunServer(port);
        safeWaitFor(1);
        Client client = buildAndRunClient(port);


        safeWaitFor(2);
        System.out.println(client.getClientView());
        assertEquals(0, client.getClientView().getId());
    }

    @Test
    public void loopLoopTest(){
        super.executor = Executors.newFixedThreadPool(256);
        int num = 5;
        for (int i=0; i<num;i++){
            loopTest(port-i);
        }
    }

    /*
    @Test
    public void loopTest2(){

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

     */



}
