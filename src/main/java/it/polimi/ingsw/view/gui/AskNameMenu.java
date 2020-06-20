package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AskNameMenu extends Menu {
    Rectangle rect;
    final TextField textName;
    Label labelName;
    Rectangle enter;
    public AskNameMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        rect = new Rectangle(200, 140);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2-rect.getWidth()/2);
        rect.setTranslateY(heightResolution/2-rect.getHeight()/2);

        labelName = new Label("Insert your name: ");
        labelName.setTranslateX(rect.getTranslateX() + 30);
        labelName.setTranslateY(rect.getTranslateY() + 20);
        labelName.setMaxWidth(140);
        labelName.setMaxHeight(30);

        textName = new TextField();
        textName.setMaxHeight(30);
        textName.setMaxWidth(90);
        textName.setTranslateX(rect.getTranslateX() + 30);
        textName.setTranslateY(rect.getTranslateY() + labelName.getMaxHeight()+20);

        enter = new Rectangle(30, 30 );
        enter.setTranslateX(rect.getTranslateX() + 30 + textName.getMaxWidth() + 20);
        enter.setTranslateY(rect.getTranslateY() + labelName.getMaxHeight() + 20);
        enter.setTranslateZ(0);
        enter.setFill(Color.BLUE);
        enter.setStroke(Color.BLACK);
        enter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadName(textName.getText());
                hide();
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(textName);
        group.getChildren().add(labelName);
        group.getChildren().add(enter);
    }
}
