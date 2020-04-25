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

        try
        {
            Thread t = new Thread(client);
            t.start();
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
        }

        //loadScenario1(board); //move
        //loadScenario2(board); //build
        //loadScenario3(board); //move and build
        //loadScenario4(board); //forced move



        Camera camera = new PerspectiveCamera(true);

        Scene scene = new Scene(board, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);

        scene.setCamera(camera);
        //scene.setFill(Color.CYAN);

        camera.translateXProperty().set(0);
        camera.translateYProperty().setValue(0);
        camera.translateZProperty().set(-1300);


        camera.setNearClip(0.1);
        camera.setFarClip(8000);

        stage.setTitle("Santorini");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

    void loadScenario1(Board board){
        List<Action> actions = new ArrayList<>();

        actions.add(new MoveAction("P1-M", 1, 2, Direction.DOWN, 2, 2));
        actions.add(new MoveAction("P1-M", 2, 3, Direction.SAME, 2, 2));
        actions.add(new MoveAction("P1-M", 3, 3, Direction.UP, 2, 2));

        actions.add(new MoveAction("P1-F", 4, 1, Direction.UP, 3, 1));
        actions.add(new MoveAction("P1-F", 3, 0, Direction.DOWN, 3, 1));

        board.setPossibleActions(actions);

        loadScenarioMappa(board);
    }

    void loadScenario2(Board board){
        List<Action> actions = new ArrayList<>();

        actions.add(new BuildAction("P1-M", 1, 2, Piece.LEVEL2));
        actions.add(new BuildAction("P1-M", 2, 3, Piece.LEVEL3));
        actions.add(new BuildAction("P1-M", 3, 3, Piece.DOME));

        actions.add(new BuildAction("P1-F", 4, 1, Piece.LEVEL3));
        actions.add(new BuildAction("P1-F", 3, 0, Piece.LEVEL1));
        actions.add(new BuildAction("P1-F", 2, 0, Piece.DOME));

        board.setPossibleActions(actions);

        loadScenarioMappa(board);
    }

    void loadScenario3(Board board){
        List<Action> actions = new ArrayList<>();

        actions.add(new MoveAction("P1-M", 1, 2, Direction.DOWN, 2, 2));
        actions.add(new MoveAction("P1-M", 2, 3, Direction.SAME, 2, 2));
        actions.add(new MoveAction("P1-M", 3, 3, Direction.UP, 2, 2));

        actions.add(new MoveAction("P1-F", 4, 1, Direction.UP, 3, 1));
        actions.add(new MoveAction("P1-F", 3, 0, Direction.DOWN, 3, 1));

        actions.add(new BuildAction("P1-M", 1, 2, Piece.LEVEL2));
        actions.add(new BuildAction("P1-M", 2, 3, Piece.LEVEL3));
        actions.add(new BuildAction("P1-M", 3, 3, Piece.DOME));

        actions.add(new BuildAction("P1-F", 4, 1, Piece.LEVEL3));
        actions.add(new BuildAction("P1-F", 3, 0, Piece.LEVEL1));
        actions.add(new BuildAction("P1-F", 2, 0, Piece.DOME));

        board.setPossibleActions(actions);

        loadScenarioMappa(board);
    }
    void loadScenario4(Board board) {
        List<Action> actions = new ArrayList<>();

        actions.add(new MoveAction("P1-M", 1, 2, Direction.DOWN, 2, 2));
        actions.add(new MoveAction("P1-M", 2, 3, Direction.SAME, 2, 2));
        actions.add(new MoveAction("P1-M", 3, 3, Direction.UP, 2, 2));
        actions.add(new MoveAndForceAction("P1-M", 2, 1, Direction.DOWN, 2, 2, "P2-M", 2, 1, 2, 0));

        actions.add(new MoveAction("P1-F", 4, 1, Direction.UP, 3, 1));
        actions.add(new MoveAction("P1-F", 3, 0, Direction.DOWN, 3, 1));


        board.setPossibleActions(actions);
        loadScenarioMappa(board);
    }
    void loadScenarioMappa(Board board){

        board.executeAction(new BuildAction("xxx", 2,2, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 2,2, Piece.LEVEL2));

        board.executeAction(new BuildAction("xxx", 3,1, Piece.LEVEL1));

        board.executeAction(new BuildAction("xxx", 2,1, Piece.LEVEL1));

        board.executeAction(new BuildAction("xxx", 0,0, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 0,0, Piece.LEVEL2));

        board.executeAction(new BuildAction("xxx", 1,1, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 1,1, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 1,1, Piece.LEVEL3));
        board.executeAction(new BuildAction("xxx", 1,1, Piece.DOME));

        board.executeAction(new BuildAction("xxx", 1,3, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 1,3, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 1,3, Piece.LEVEL3));
        board.executeAction(new BuildAction("xxx", 1,3, Piece.DOME));

        board.executeAction(new BuildAction("xxx", 3,2, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 3,2, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 3,2, Piece.LEVEL3));
        board.executeAction(new BuildAction("xxx", 3,2, Piece.DOME));

        board.executeAction(new BuildAction("xxx", 2,0, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 2,0, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 2,0, Piece.LEVEL3));

        board.executeAction(new BuildAction("xxx", 4,0, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 4,0, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 4,0, Piece.LEVEL3));
        board.executeAction(new BuildAction("xxx", 4,0, Piece.DOME));

        board.executeAction(new BuildAction("xxx", 4,2, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 4,2, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 4,2, Piece.LEVEL3));
        board.executeAction(new BuildAction("xxx", 4,2, Piece.DOME));

        board.executeAction(new BuildAction("xxx", 4,1, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 4,1, Piece.LEVEL2));

        board.executeAction(new BuildAction("xxx", 1,2, Piece.LEVEL1));

        board.executeAction(new BuildAction("xxx", 2,3, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 2,3, Piece.LEVEL2));

        board.executeAction(new BuildAction("xxx", 0,3, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 0,3, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 0,3, Piece.LEVEL3));
        board.executeAction(new BuildAction("xxx", 0,3, Piece.DOME));

        board.executeAction(new BuildAction("xxx", 3,3, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 3,3, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 3,3, Piece.LEVEL3));

        board.executeAction(new BuildAction("xxx", 4,3, Piece.LEVEL1));
        board.executeAction(new BuildAction("xxx", 4,3, Piece.LEVEL2));
        board.executeAction(new BuildAction("xxx", 4,3, Piece.LEVEL3));
        board.executeAction(new BuildAction("xxx", 4,3, Piece.DOME));

        board.registerPlayer(1, "Paolo");
        board.executeAction(new SetupAction("P1-M", 2, 2));
        board.executeAction(new SetupAction("P1-F", 3, 1));

        board.registerPlayer(2, "Federico");
        board.executeAction(new SetupAction("P2-M", 2, 1));
        board.executeAction(new SetupAction("P2-F", 0, 0));
    }
}
