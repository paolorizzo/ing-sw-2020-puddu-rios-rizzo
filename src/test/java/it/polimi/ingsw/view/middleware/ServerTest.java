package it.polimi.ingsw.view.middleware;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.*;

public class ServerTest
{
    private ExecutorService executor = Executors.newFixedThreadPool(128);

    // checks the registration of connections on the server's side
    // while 4 clients try to connect, only 3 must be accepted
    // waits 1 second after running the clients' threads to check for the number of registered connections
    @Test
    public void checkRegisteredConnections()
    {
        final int port = 12345;
        final Server server;
        final Client client1 = new Client("127.0.0.1", port);
        final Client client2 = new Client("127.0.0.1", port);
        final Client client3 = new Client("127.0.0.1", port);
        final Client client4 = new Client("127.0.0.1", port);

        try
        {
            server = new Server(port);
            executor.submit(server);
            executor.submit(client1);
            executor.submit(client2);
            executor.submit(client3);
            executor.submit(client4);

            try
            {
                TimeUnit.SECONDS.sleep(1);
                assertEquals(server.getNumberOfConnections(), 3);
            }
            catch(InterruptedException e)
            {
                System.err.println(e.getMessage());
            }

            executor.shutdownNow();
        }
        catch (IOException e)
        {
            System.err.println("Impossible to start the server!\n" + e.getMessage());
        }
    }

    // checks the correct setting of the number of players on server's side
    // invokes the method that Connection would call after receiving the corresponding update from the client view
    @Test
    public void checkSetNumberOfPlayers()
    {
        final int port = 12346;
        final Server server;

        try
        {
            server = new Server(port);
            executor.submit(server);
            server.setNumberOfPlayers(3);
            assertEquals(server.getNumberOfPlayers(), 3);

            executor.shutdownNow();
        }
        catch (IOException e)
        {
            System.err.println("Impossible to start the server!\n" + e.getMessage());
        }
    }

    // raw check of the execution of startGame() by the server, by evaluating the assignment of the boolean flag isGameOn
    @Test
    public void checkStartGame()
    {
        final int port = 12347;
        final Server server;
        final Client client1 = new Client("127.0.0.1", port);
        final Client client2 = new Client("127.0.0.1", port);

        try
        {
            server = new Server(port);
            executor.submit(server);
            executor.submit(client1);
            executor.submit(client2);

            try
            {
                TimeUnit.SECONDS.sleep(1);
                server.setNumberOfPlayers(2);
                TimeUnit.SECONDS.sleep(1);
                server.startGame();
                assert(server.isGameOn());

            }
            catch(InterruptedException e)
            {
                System.err.println(e.getMessage());
            }

            executor.shutdownNow();
        }
        catch (IOException e)
        {
            System.err.println("Impossible to start the server!\n" + e.getMessage());
        }
    }
}
