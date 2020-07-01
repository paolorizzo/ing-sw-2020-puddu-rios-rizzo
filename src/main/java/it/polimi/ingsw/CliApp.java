package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.middleware.Client;

import java.io.IOException;

/**
 * The launcher for the command line user interface.
 */
public class CliApp
{
    /**
     * Runs the CLI.
     * @param args command line arguments for the main method.
     */
    public static void main( String[] args )
    {
        run();
    }

    /**
     * Creates a client node with a CLI-based user interface.
     */
    public static void run()
    {
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
