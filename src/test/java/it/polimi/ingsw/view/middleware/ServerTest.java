package it.polimi.ingsw.view.middleware;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ServerTest
{
    // checks the registration of connections on the server's side
    // waits 2 seconds after running the clients' threads to check for the number of registered connections
    @Test
    public void checkRegisteredConnections()
    {
        final Server server;
        final Client client1 = new Client("127.0.0.1", 42069);
        final Client client2 = new Client("127.0.0.1", 42069);

        try
        {
            server = new Server();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    server.run();
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        client1.run();
                    }
                    catch (IOException e)
                    {
                        System.err.println(e.getMessage());
                    }

                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        client2.run();
                    }
                    catch (IOException e)
                    {
                        System.err.println(e.getMessage());
                    }

                }
            }).start();

            try
            {
                TimeUnit.SECONDS.sleep(2);
                assertEquals(server.getNumberOfConnections(), 2);
            }
            catch(InterruptedException e)
            {
                System.err.println(e.getMessage());
            }
        }
        catch (IOException e)
        {
            System.err.println("Impossible to start the server!\n" + e.getMessage());
        }
    }

}
