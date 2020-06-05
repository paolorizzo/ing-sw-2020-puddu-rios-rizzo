package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.Board;
import it.polimi.ingsw.view.middleware.Client;
import javafx.application.Application;
import javafx.stage.Stage;

//import javafx.scene.paint.Color;

public class GuiApp extends Application {
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;

    @Override
    public void start(Stage stage){


        Client client = new Client("127.0.0.1", 42069);
        ClientView cw = new ClientView(client);

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

    public static void main(String[] args) {

        launch(args);
    }
}
