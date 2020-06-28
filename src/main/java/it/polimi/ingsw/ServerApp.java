package it.polimi.ingsw;
import it.polimi.ingsw.view.middleware.Server;

import java.io.IOException;

public class ServerApp
{
    public static void main( String[] args )
    {
        run(42069);
    }
    public static void run(int port){
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