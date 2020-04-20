package it.polimi.ingsw.view.GUI;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectTypeActionMenu extends Group {
    Rectangle rect;
    Rectangle moveButton;
    Rectangle buildButton;
    Rectangle unselectButton;
    public SelectTypeActionMenu(final ActionFSM actionFSM) {
        rect = new Rectangle(200, 100);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(1400 / 2 - rect.getWidth() - 110);
        rect.setTranslateY(70 - 800 / 2);
        rect.setVisible(false);

        moveButton = new Rectangle(60, 30);
        moveButton.setTranslateX(rect.getTranslateX() + 30);
        moveButton.setTranslateY(rect.getTranslateY() + 20);
        moveButton.setFill(Color.GREEN);
        moveButton.setStroke(Color.BLACK);
        moveButton.setVisible(false);
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
        buildButton.setVisible(false);
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
        unselectButton.setVisible(false);
        unselectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("unselect");
            }
        });


        this.getChildren().add(rect);
        this.getChildren().add(moveButton);
        this.getChildren().add(buildButton);
        this.getChildren().add(unselectButton);

    }
    public void show(){
        rect.setVisible(true);
        moveButton.setVisible(true);
        buildButton.setVisible(true);
        unselectButton.setVisible(true);
    }
    public void hide(){
        rect.setVisible(false);
        moveButton.setVisible(false);
        buildButton.setVisible(false);
        unselectButton.setVisible(false);
    }

}
