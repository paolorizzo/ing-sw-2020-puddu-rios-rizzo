package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class AskNameMenu extends Group {
    Rectangle rect;
    final TextField textName;
    Label labelName;
    Rectangle enter;
    final ClientView cw;
    public AskNameMenu(ClientView clientView) {
        this.cw = clientView;
        rect = new Rectangle(200, 140);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(1400/2-rect.getWidth()/2);
        rect.setTranslateY(800/2-rect.getHeight()/2);
        rect.setVisible(false);

        labelName = new Label("Insert your name: ");
        labelName.setTranslateX(rect.getTranslateX() + 30);
        labelName.setTranslateY(rect.getTranslateY() + 20);
        labelName.setMaxWidth(140);
        labelName.setMaxHeight(30);
        labelName.setVisible(false);

        textName = new TextField();
        textName.setMaxHeight(30);
        textName.setMaxWidth(90);
        textName.setTranslateX(rect.getTranslateX() + 30);
        textName.setTranslateY(rect.getTranslateY() + labelName.getMaxHeight()+20);
        textName.setVisible(false);

        enter = new Rectangle(30, 30 );
        enter.setTranslateX(rect.getTranslateX() + 30 + textName.getMaxWidth() + 20);
        enter.setTranslateY(rect.getTranslateY() + labelName.getMaxHeight() + 20);
        enter.setTranslateZ(0);
        enter.setFill(Color.BLUE);
        enter.setStroke(Color.BLACK);
        enter.setVisible(false);
        enter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                cw.updateReadName(textName.getText());
                hide();
            }
        });

        this.getChildren().add(rect);
        this.getChildren().add(textName);
        this.getChildren().add(labelName);
        this.getChildren().add(enter);
    }
    public void show(){
        rect.setVisible(true);
        textName.setVisible(true);
        labelName.setVisible(true);
        enter.setVisible(true);
    }
    public void hide(){
        rect.setVisible(false);
        textName.setVisible(false);
        labelName.setVisible(false);
        enter.setVisible(false);
    }
}
