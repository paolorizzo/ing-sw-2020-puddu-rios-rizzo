package it.polimi.ingsw;

import it.polimi.ingsw.view.middleware.Server;

import java.io.IOException;

public class App
{
    public static void main( String[] args )
    {
        Server server;
        try
        {
            server = new Server(42069);
            server.run();
        }
        catch (IOException e)
        {
            System.err.println("Impossible to start the server!\n" + e.getMessage());
        }
    }
}
