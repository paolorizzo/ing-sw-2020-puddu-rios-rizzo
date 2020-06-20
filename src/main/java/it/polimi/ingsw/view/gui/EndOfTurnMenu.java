package it.polimi.ingsw.view.gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EndOfTurnMenu extends Menu {
    Rectangle endOfTurn;

    public EndOfTurnMenu(int widthResolution, int heightResolution, final ActionFSM actionFSM) {
        super(widthResolution, heightResolution);

        endOfTurn = new Rectangle(30, 30);
        endOfTurn.setFill(Color.RED);
        endOfTurn.setStroke(Color.BLACK);
        endOfTurn.setStrokeWidth(2);
        endOfTurn.setTranslateX(widthResolution - 150 - endOfTurn.getWidth());
        endOfTurn.setTranslateY(heightResolution - 100 - endOfTurn.getHeight());

        endOfTurn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                actionFSM.voluntaryEndOfTurn();
            }
        });

        group.getChildren().add(endOfTurn);

    }
}
