package it.polimi.ingsw.view.gui;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class WinMenu extends Group {
    Rectangle rect;
    public WinMenu(){

        rect = new Rectangle(500, 300);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(1400/2-rect.getWidth()/2);
        rect.setTranslateY(800/2-rect.getHeight()/2);
        rect.setTranslateZ(0);

        this.getChildren().add(rect);
        StackPane textPane = new StackPane();

        //textPane.setLayoutX(rect.getWidth());
        //textPane.setLayoutY(30);
        textPane.setMinWidth(rect.getWidth());
        textPane.setTranslateX(rect.getTranslateX());
        textPane.setTranslateY(rect.getTranslateY()+20);

        Text text = new Text();
        text.setText("YOU WIN");
        text.setFont(new Font("Forte", 30));
        text.setFill(Color.GRAY);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(1);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTranslateY(0);
        textPane.getChildren().add(text);

        this.getChildren().add(textPane);


    }
}
