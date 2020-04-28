package it.polimi.ingsw.view.gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EndOfTurnMenu extends Group {
    Rectangle endOfTurn;

    public EndOfTurnMenu(final ActionFSM actionFSM) {
        endOfTurn = new Rectangle(30, 30);
        endOfTurn.setFill(Color.RED);
        endOfTurn.setStroke(Color.BLACK);
        endOfTurn.setStrokeWidth(2);
        endOfTurn.setTranslateX(1400/2 -150 -endOfTurn.getWidth());
        endOfTurn.setTranslateY(800/2 -100 -endOfTurn.getHeight());
        endOfTurn.setTranslateZ(0);
        endOfTurn.setVisible(false);

        endOfTurn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                actionFSM.voluntaryEndOfTurn();
            }
        });

        this.getChildren().add(endOfTurn);

    }
    public void show(){
        endOfTurn.setVisible(true);
    }
    public void hide(){
        endOfTurn.setVisible(false);
    }
}
