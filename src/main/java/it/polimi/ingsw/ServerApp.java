package it.polimi.ingsw;
import it.polimi.ingsw.view.middleware.Server;

import java.io.IOException;

/**
 * The launcher for the server of a game.
 */
public class ServerApp
{
    /**
     * Runs the server on port 42069
     * @param args command line arguments for the main method.
     */
    public static void main( String[] args )
    {

        run(42069);
    }

    /**
     * Runs the server on a specified port.
     * @param port the port number.
     */
    public static void run(int port)
    {
        Server server;
        try
        {
            server = new Server(port);
            server.run();
        }
        catch (IOException e)
        {
            System.err.println("Impossible to start the server!\n" + e.getMessage());
        }
    }
}