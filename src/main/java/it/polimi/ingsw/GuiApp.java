package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.gui.*;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.Board;
import it.polimi.ingsw.view.middleware.Client;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
//import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GuiApp extends Application {
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;

    @Override
    public void start(Stage stage) throws Exception {


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
