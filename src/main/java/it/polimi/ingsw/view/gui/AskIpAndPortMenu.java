package it.polimi.ingsw.view.gui;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class AskIpAndPortMenu extends Menu
{
    Rectangle rect;
    final TextField ipText, portText;
    Label ipName, portName;
    Rectangle enter;
    public AskIpAndPortMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);

        rect = new Rectangle(280, 250);
        rect.setFill(Color.LIGHTGRAY);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2-rect.getWidth()/2);
        rect.setTranslateY(heightResolution/2-rect.getHeight()/2);

        ipName = new Label("Insert a valid IP address for the server: ");
        ipName.setTranslateX(rect.getTranslateX() + 30);
        ipName.setTranslateY(rect.getTranslateY() + 20);
        ipName.setMaxWidth(240);
        ipName.setMaxHeight(30);

        ipText = new TextField();
        ipText.setMaxWidth(220);
        ipText.setMaxHeight(30);
        ipText.setMinWidth(220);
        ipText.setTranslateX(rect.getTranslateX() + 30);
        ipText.setTranslateY(ipName.getTranslateY() + ipName.getMaxHeight() + 5);

        portName = new Label("Insert a valid port address for the server: ");
        portName.setTranslateX(rect.getTranslateX() + 30);
        portName.setTranslateY(ipText.getTranslateY() + ipText.getMaxHeight() + 15);
        portName.setMaxWidth(240);
        portName.setMaxHeight(30);

        portText = new TextField();
        portText.setMaxWidth(220);
        portText.setMaxHeight(30);
        portText.setMinWidth(220);
        portText.setTranslateX(rect.getTranslateX() + 30);
        portText.setTranslateY(portName.getTranslateY() + portName.getMaxHeight() + 5);


        enter = new Rectangle(60, 30 );
        enter.setTranslateX(rect.getTranslateX() + rect.getWidth()/2 - enter.getWidth()/2);
        enter.setTranslateY(portText.getTranslateY() + portText.getMaxHeight() + 15);
        enter.setTranslateZ(0);
        enter.setFill(Color.BLUE);
        enter.setStroke(Color.BLACK);


        enter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                notifyIp(ipText.getText());
                notifyPort(Integer.parseInt(portText.getText()));

                /* DEFAULT
                notifyIp("127.0.0.1");
                notifyPort(42069);
                */
                hide();
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(ipText);
        group.getChildren().add(portText);
        group.getChildren().add(ipName);
        group.getChildren().add(portName);
        group.getChildren().add(enter);
    }
}
