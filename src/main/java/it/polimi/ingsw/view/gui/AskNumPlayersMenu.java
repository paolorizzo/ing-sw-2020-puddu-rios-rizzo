package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AskNumPlayersMenu extends Menu {
    Rectangle rect;
    Rectangle twoPlayer;
    Rectangle treePlayer;

    public AskNumPlayersMenu() {
        super();
        rect = new Rectangle(200, 100);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(1400 / 2 - rect.getWidth() / 2);
        rect.setTranslateY(800 / 2 - rect.getHeight() / 2);
        rect.setTranslateZ(0);

        twoPlayer = new Rectangle(60, 60);
        twoPlayer.setTranslateX(rect.getTranslateX() + 30);
        twoPlayer.setTranslateY(rect.getTranslateY() + 20);
        twoPlayer.setTranslateZ(0);
        twoPlayer.setFill(Color.GREEN);
        twoPlayer.setStroke(Color.BLACK);
        twoPlayer.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadNumPlayers(2);
                hide();
            }
        });

        treePlayer = new Rectangle(60, 60);
        treePlayer.setTranslateX(rect.getTranslateX() + 30 + twoPlayer.getWidth() + 20);
        treePlayer.setTranslateY(rect.getTranslateY() + 20);
        treePlayer.setTranslateZ(0);
        treePlayer.setFill(Color.BLUE);
        treePlayer.setStroke(Color.BLACK);
        treePlayer.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadNumPlayers(3);
                hide();
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(twoPlayer);
        group.getChildren().add(treePlayer);

    }
}