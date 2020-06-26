package it.polimi.ingsw.view.gui;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AskIpAndPortMenu extends Menu
{
    Rectangle rect;
    final TextField ipText, portText;
    Label ipName, portName;
    Rectangle enter;
    public AskIpAndPortMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);

        rect = new Rectangle(360, 300);
        rect.setFill(Color.LIGHTGRAY);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2-rect.getWidth()/2);
        rect.setTranslateY(heightResolution/2-rect.getHeight()/2);


        StackPane titlePane = new StackPane();
        titlePane.setTranslateX(rect.getTranslateX() + 10);
        titlePane.setTranslateY(rect.getTranslateY() + 25);
        titlePane.setMaxWidth(rect.getWidth()-20);
        titlePane.setMinWidth(rect.getWidth()-20);
        titlePane.setMaxHeight(30);
        titlePane.setMinHeight(30);

        Text title = new Text();
        title.setText("CONNECT TO SERVER");
        title.setFont(new Font("Forte", 30));
        title.setFill(Color.GOLD);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(1);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(0);
        titlePane.getChildren().add(title);

        ipName = new Label("Insert a valid IP address for the server: ");
        ipName.setFont(new Font("Forte", 16));
        ipName.setTranslateX(rect.getTranslateX() + 40);
        ipName.setTranslateY(titlePane.getTranslateY() + titlePane.getHeight()+ 55);
        ipName.setMaxWidth(320);
        ipName.setMinWidth(320);
        ipName.setTextAlignment(TextAlignment.CENTER);
        ipName.setMaxHeight(30);

        ipText = new TextField();
        ipText.setMaxWidth(260);
        ipText.setMinWidth(260);
        ipText.setMaxHeight(30);
        ipText.setTranslateX(rect.getTranslateX() + 50);
        ipText.setTranslateY(ipName.getTranslateY() + ipName.getMaxHeight() + 5);

        portName = new Label("Insert a valid port address for the server: ");
        portName.setFont(new Font("Forte", 16));
        portName.setTranslateX(rect.getTranslateX() + 40);
        portName.setTranslateY(ipText.getTranslateY() + ipText.getMaxHeight() + 15);
        portName.setTextAlignment(TextAlignment.CENTER);
        portName.setMaxWidth(320);
        portName.setMinWidth(320);
        portName.setMaxHeight(30);

        portText = new TextField();
        portText.setMaxWidth(260);
        portText.setMinWidth(260);
        portText.setMaxHeight(30);
        portText.setTranslateX(rect.getTranslateX() + 50);
        portText.setTranslateY(portName.getTranslateY() + portName.getMaxHeight() + 5);


        enter = new Rectangle(100, 30 );
        enter.setTranslateX(rect.getTranslateX() + rect.getWidth()/2 - enter.getWidth()/2);
        enter.setTranslateY(portText.getTranslateY() + portText.getMaxHeight() + 15);
        enter.setTranslateZ(0);
        enter.setFill(Color.BLUE);
        enter.setStroke(Color.BLACK);



        StackPane enterPane = new StackPane();
        enterPane.setTranslateX(enter.getTranslateX());
        enterPane.setTranslateY(enter.getTranslateY());
        enterPane.setMaxWidth(enter.getWidth());
        enterPane.setMinWidth(enter.getWidth());
        enterPane.setMaxHeight(enter.getHeight());
        enterPane.setMinHeight(enter.getHeight());

        Text enterText = new Text();
        enterText.setText("ENTER");
        enterText.setFont(new Font("Forte", 22));
        enterText.setFill(Color.WHITE);
        enterText.setStroke(Color.BLACK);
        enterText.setStrokeWidth(1);
        enterText.setTextAlignment(TextAlignment.CENTER);
        enterText.setTranslateY(0);
        enterPane.getChildren().add(enterText);

        enterPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {


                if(ipText.getText().length() == 0){
                    notifyIp("127.0.0.1");
                    notifyPort(42069);
                }else{

                    notifyIp(ipText.getText());
                    notifyPort(Integer.parseInt(portText.getText()));
                }
                hide();
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(titlePane);
        group.getChildren().add(ipText);
        group.getChildren().add(portText);
        group.getChildren().add(ipName);
        group.getChildren().add(portName);
        group.getChildren().add(enter);
        group.getChildren().add(enterPane);

    }
}
