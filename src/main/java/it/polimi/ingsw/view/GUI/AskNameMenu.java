package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
        rect = new Rectangle(200, 100);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(-rect.getWidth()/2);
        rect.setTranslateY(-100);
        rect.setTranslateZ(-500);
        rect.setVisible(false);

        labelName = new Label("Name: ");
        labelName.setTranslateX(rect.getTranslateX() + 30);
        labelName.setTranslateY(rect.getTranslateY() + 20);
        labelName.setTranslateZ(-500);
        labelName.setVisible(false);

        textName = new TextField();
        textName.setTranslateX(rect.getTranslateX() + labelName.getWidth() +30);
        textName.setTranslateY(rect.getTranslateY() + 20);
        textName.setTranslateZ(-500);
        textName.setVisible(false);

        enter = new Rectangle(60, 60 );
        enter.setTranslateX(rect.getTranslateX() + 30 + rect.getWidth() + 20);
        enter.setTranslateY(rect.getTranslateY() + 20);
        enter.setTranslateZ(-500);
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
