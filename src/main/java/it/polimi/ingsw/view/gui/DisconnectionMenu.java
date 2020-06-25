package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Piece;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class DisconnectionMenu extends Menu
{
    Rectangle rect;
    Text message;
    public DisconnectionMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        rect = new Rectangle(500, 270);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2-rect.getWidth()/2);
        rect.setTranslateY(heightResolution/2-rect.getHeight()/2);

        Image disconnectedImage = GraphicsLoader.instance().getImage("disconnected");
        ImageView disconnectedImageView = new ImageView(disconnectedImage);
        disconnectedImageView.setFitHeight(100);
        disconnectedImageView.setFitWidth(100);
        Label disconnected = new Label("");
        disconnected.setTranslateX(rect.getTranslateX()+200);
        disconnected.setTranslateY(rect.getTranslateY()+130);
        disconnected.setGraphic(disconnectedImageView);

        StackPane messagePane = new StackPane();
        messagePane.setTranslateX(rect.getTranslateX()+50);
        messagePane.setTranslateY(rect.getTranslateY()+30);
        Rectangle messageRect = new Rectangle(400, 50);
        messageRect.setVisible(false);
        messagePane.getChildren().add(messageRect);

        message = new Text();
        message.setText("");
        message.setFont(new Font("Forte", 30));
        message.setFill(Color.PALEVIOLETRED);
        message.setStroke(Color.BLACK);
        message.setStrokeWidth(1);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setWrappingWidth(400);
        message.setTranslateY(0);
        messagePane.getChildren().add(message);

        group.getChildren().add(rect);
        group.getChildren().add(disconnected);
        group.getChildren().add(messagePane);
    }

    public void show(String message){
        this.message.setText(message);
        super.show();
    }
}
