package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AskNameMenu extends Menu {
    Rectangle rect;
    final TextField textName;
    Label labelName;
    Rectangle enter;
    public AskNameMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        rect = new Rectangle(250, 160);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2-rect.getWidth()/2);
        rect.setTranslateY(heightResolution/2-rect.getHeight()/2);

        labelName = new Label("Insert your name: ");
        labelName.setFont(new Font("Forte", 22));
        labelName.setTranslateX(rect.getTranslateX() + 30);
        labelName.setTranslateY(rect.getTranslateY() + 20);
        labelName.setMaxWidth(190);
        labelName.setMaxHeight(30);

        textName = new TextField();
        textName.setMaxWidth(190);
        textName.setMinWidth(190);
        textName.setMaxHeight(30);
        textName.setTranslateX(rect.getTranslateX() + 30);
        textName.setTranslateY(rect.getTranslateY() + labelName.getMaxHeight()+30);


        enter = new Rectangle(100, 30 );
        enter.setTranslateX(rect.getTranslateX() + rect.getWidth()/2 - enter.getWidth()/2);
        enter.setTranslateY(labelName.getTranslateY() + labelName.getMaxHeight() + 50);
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
                hide();
                notifyReadName(textName.getText());
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(textName);
        group.getChildren().add(labelName);

        group.getChildren().add(enter);
        group.getChildren().add(enterPane);

    }
}
