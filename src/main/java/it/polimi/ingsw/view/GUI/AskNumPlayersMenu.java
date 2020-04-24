package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AskNumPlayersMenu extends Group {
    Rectangle rect;
    Rectangle twoPlayer;
    Rectangle treePlayer;
    final ClientView cw;
    public AskNumPlayersMenu(ClientView clientView) {
        this.cw = clientView;
        rect = new Rectangle(200, 100);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(-rect.getWidth()/2);
        rect.setTranslateY(-100);
        rect.setTranslateZ(-500);
        rect.setVisible(false);

        twoPlayer = new Rectangle(60, 60);
        twoPlayer.setTranslateX(rect.getTranslateX() + 30);
        twoPlayer.setTranslateY(rect.getTranslateY() + 20);
        twoPlayer.setTranslateZ(-500);
        twoPlayer.setFill(Color.GREEN);
        twoPlayer.setStroke(Color.BLACK);
        twoPlayer.setVisible(false);
        twoPlayer.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                cw.updateReadNumPlayers(2);
                hide();
            }
        });

        treePlayer = new Rectangle(60, 60 );
        treePlayer.setTranslateX(rect.getTranslateX() + 30 + twoPlayer.getWidth() + 20);
        treePlayer.setTranslateY(rect.getTranslateY() + 20);
        treePlayer.setTranslateZ(-500);
        treePlayer.setFill(Color.BLUE);
        treePlayer.setStroke(Color.BLACK);
        treePlayer.setVisible(false);
        treePlayer.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                cw.updateReadNumPlayers(3);
                hide();
            }
        });

        this.getChildren().add(rect);
        this.getChildren().add(twoPlayer);
        this.getChildren().add(treePlayer);

    }
    public void show(){
        rect.setVisible(true);
        twoPlayer.setVisible(true);
        treePlayer.setVisible(true);
    }
    public void hide(){
        rect.setVisible(false);
        twoPlayer.setVisible(false);
        treePlayer.setVisible(false);
    }
}
