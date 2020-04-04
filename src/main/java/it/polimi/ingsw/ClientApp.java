package it.polimi.ingsw;

import it.polimi.ingsw.view.middleware.Client;

import java.io.IOException;

public class ClientApp {
    public static void main( String[] args )
    {
        Client client = new Client("127.0.0.1", 42069);
        try
        {
            client.run();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
