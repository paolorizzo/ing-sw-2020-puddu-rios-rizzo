package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AskRestoreMenu extends Menu {
    Rectangle rect;
    Rectangle yesRect;
    Rectangle noRect;

    public AskRestoreMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        rect = new Rectangle(200, 100);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution / 2 - rect.getWidth() / 2);
        rect.setTranslateY(heightResolution / 2 - rect.getHeight() / 2);
        rect.setTranslateZ(0);

        yesRect = new Rectangle(60, 60);
        yesRect.setTranslateX(rect.getTranslateX() + 30);
        yesRect.setTranslateY(rect.getTranslateY() + 20);
        yesRect.setTranslateZ(0);
        yesRect.setFill(Color.ROYALBLUE);
        yesRect.setStroke(Color.BLACK);
        yesRect.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadRestore(true);
                hide();
            }
        });

        noRect = new Rectangle(60, 60);
        noRect.setTranslateX(rect.getTranslateX() + 30 + yesRect.getWidth() + 20);
        noRect.setTranslateY(rect.getTranslateY() + 20);
        noRect.setTranslateZ(0);
        noRect.setFill(Color.RED);
        noRect.setStroke(Color.BLACK);
        noRect.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadRestore(false);
                hide();
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(yesRect);
        group.getChildren().add(noRect);

    }
}