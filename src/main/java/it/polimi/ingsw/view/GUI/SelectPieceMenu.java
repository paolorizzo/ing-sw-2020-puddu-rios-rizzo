package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.Piece;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectPieceMenu extends Group {
    Rectangle rect;
    Rectangle LEVEL1Button;
    Rectangle LEVEL2Button;
    Rectangle LEVEL3Button;
    Rectangle DOMEButton;
    public SelectPieceMenu(final ActionFSM actionFSM) {
        rect = new Rectangle(200, 200);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(1400 / 2 - rect.getWidth() - 110);
        rect.setTranslateY(70 - 800 / 2 + 150);
        rect.setVisible(false);

        LEVEL1Button = new Rectangle(50, 50);
        LEVEL1Button.setTranslateX(rect.getTranslateX() + 35);
        LEVEL1Button.setTranslateY(rect.getTranslateY() + 35);
        LEVEL1Button.setFill(Color.GREEN);
        LEVEL1Button.setStroke(Color.BLACK);
        LEVEL1Button.setVisible(false);
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
        LEVEL2Button.setVisible(false);
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
        LEVEL3Button.setVisible(false);
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
        DOMEButton.setVisible(false);
        DOMEButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.DOME);
            }
        });

        this.getChildren().add(rect);
        this.getChildren().add(LEVEL1Button);
        this.getChildren().add(LEVEL2Button);
        this.getChildren().add(LEVEL3Button);
        this.getChildren().add(DOMEButton);

    }
    public void show(){
        rect.setVisible(true);
        LEVEL1Button.setVisible(true);
        LEVEL2Button.setVisible(true);
        LEVEL3Button.setVisible(true);
        DOMEButton.setVisible(true);
    }
    public void hide(){
        rect.setVisible(false);
        LEVEL1Button.setVisible(false);
        LEVEL2Button.setVisible(false);
        LEVEL3Button.setVisible(false);
        DOMEButton.setVisible(false);
    }
}
