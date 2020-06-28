package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.middleware.Client;

import java.io.IOException;

public class CliApp {
    public static void main( String[] args )
    {
        run();
    }
    public static void run(){
        Cli cli = new Cli();
        Client client = new Client();

        ClientView cw = new ClientView(client, client);

        client.setClientView(cw);
        cw.setUi(cli);
        cli.addObserver(cw);

        try
        {
            Thread t = new Thread(client);
            t.start();
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
        }
    }
}
