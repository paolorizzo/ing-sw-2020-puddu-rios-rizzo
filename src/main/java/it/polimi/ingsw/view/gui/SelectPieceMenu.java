package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Piece;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectPieceMenu extends Menu {
    Rectangle rect;
    Rectangle LEVEL1Button;
    Rectangle LEVEL2Button;
    Rectangle LEVEL3Button;
    Rectangle DOMEButton;
    public SelectPieceMenu(int widthResolution, int heightResolution, final ActionFSM actionFSM) {
        super(widthResolution, heightResolution);

        rect = new Rectangle(200, 200);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution-rect.getWidth()-50);
        rect.setTranslateY(200);

        LEVEL1Button = new Rectangle(50, 50);
        LEVEL1Button.setTranslateX(rect.getTranslateX() + 35);
        LEVEL1Button.setTranslateY(rect.getTranslateY() + 35);
        LEVEL1Button.setFill(Color.GREEN);
        LEVEL1Button.setStroke(Color.BLACK);
        LEVEL1Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL1);
            }
        });

        LEVEL2Button = new Rectangle(50, 50);
        LEVEL2Button.setTranslateX(rect.getTranslateX()+35+LEVEL1Button.getWidth()+30);
        LEVEL2Button.setTranslateY(rect.getTranslateY()+35);
        LEVEL2Button.setFill(Color.GREEN);
        LEVEL2Button.setStroke(Color.BLACK);
        LEVEL2Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL2);
            }
        });

        LEVEL3Button = new Rectangle(50, 50);
        LEVEL3Button.setTranslateX(rect.getTranslateX()+35);
        LEVEL3Button.setTranslateY(rect.getTranslateY()+35+LEVEL1Button.getHeight()+30);
        LEVEL3Button.setFill(Color.GREEN);
        LEVEL3Button.setStroke(Color.BLACK);
        LEVEL3Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL3);
            }
        });

        DOMEButton = new Rectangle(50, 50);
        DOMEButton.setTranslateX(rect.getTranslateX()+35+LEVEL3Button.getWidth()+30);
        DOMEButton.setTranslateY(rect.getTranslateY()+35+LEVEL2Button.getHeight()+30);
        DOMEButton.setFill(Color.GREEN);
        DOMEButton.setStroke(Color.BLACK);
        DOMEButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.DOME);
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(LEVEL1Button);
        group.getChildren().add(LEVEL2Button);
        group.getChildren().add(LEVEL3Button);
        group.getChildren().add(DOMEButton);

    }
}
