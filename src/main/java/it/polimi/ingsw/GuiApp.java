package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.Board;
import it.polimi.ingsw.view.middleware.Client;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The launcher for the graphical user interface based on JavaFX.
 */
public class GuiApp extends Application
{
    /**
     * Creates a client node with a graphical user interface.
     * @param stage the stage of the JavaFX application.
     */
    @Override
    public void start(Stage stage)
    {
        Client client = new Client();
        ClientView cw = new ClientView(client, client);

        client.setClientView(cw);

        Board board = new Board(cw);
        cw.setUi(board);

        try {
            Thread t = new Thread(client);
            t.start();
        } catch (Exception e) {
            System.err.println(e.toString());
        }



        stage.setTitle("Santorini");
        stage.setScene(board.getScene());
        stage.show();
    }

    /**
     * Launches the application.
     * @param args command line arguments for the main method.
     */
    public static void main(String[] args)
    {

        launch(args);
    }
}
