package it.polimi.ingsw.view.gui;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectTypeActionMenu extends Menu {
    Rectangle rect;
    Rectangle moveButton;
    Rectangle buildButton;
    Rectangle unselectButton;
    public SelectTypeActionMenu(int widthResolution, int heightResolution, final ActionFSM actionFSM) {
        super(widthResolution, heightResolution);


        rect = new Rectangle(200, 100);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution-rect.getWidth()-50);
        rect.setTranslateY(50);

        moveButton = new Rectangle(60, 30);
        moveButton.setTranslateX(rect.getTranslateX() + 30);
        moveButton.setTranslateY(rect.getTranslateY() + 20);
        moveButton.setFill(Color.GREEN);
        moveButton.setStroke(Color.BLACK);
        moveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("move");
            }
        });

        buildButton = new Rectangle(60, 30 );
        buildButton.setTranslateX(rect.getTranslateX() + 30 + moveButton.getWidth() + 20);
        buildButton.setTranslateY(rect.getTranslateY() + 20);
        buildButton.setFill(Color.BLUE);
        buildButton.setStroke(Color.BLACK);
        buildButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("build");
            }
        });

        unselectButton = new Rectangle(15, 15);
        unselectButton.setTranslateX(rect.getTranslateX() + rect.getWidth() - unselectButton.getWidth());
        unselectButton.setTranslateY(rect.getTranslateY());
        unselectButton.setFill(Color.RED);
        unselectButton.setStroke(Color.BLACK);
        unselectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("unselect");
            }
        });


        group.getChildren().add(rect);
        group.getChildren().add(moveButton);
        group.getChildren().add(buildButton);
        group.getChildren().add(unselectButton);

    }
}
