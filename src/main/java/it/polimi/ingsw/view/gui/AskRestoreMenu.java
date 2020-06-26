package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AskRestoreMenu extends Menu {
    Rectangle rect;
    Rectangle yesRect;
    Rectangle noRect;

    public AskRestoreMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        rect = new Rectangle(400, 250);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution / 2 - rect.getWidth() / 2);
        rect.setTranslateY(heightResolution / 2 - rect.getHeight() / 2);
        rect.setTranslateZ(0);

        StackPane titlePane = new StackPane();
        titlePane.setTranslateX(rect.getTranslateX() + 10);
        titlePane.setTranslateY(rect.getTranslateY() + 20);
        titlePane.setMaxWidth(rect.getWidth()-20);
        titlePane.setMinWidth(rect.getWidth()-20);
        titlePane.setMaxHeight(30);
        titlePane.setMinHeight(30);

        Text title = new Text();
        title.setText("RESTORE GAME");
        title.setFont(new Font("Forte", 30));
        title.setFill(Color.GOLD);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(1);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(0);

        titlePane.getChildren().add(title);

        Label text = new Label("There is an unfinished game already with these names. Do you want to continue it?");
        text.setFont(new Font("Forte", 19));
        text.setTranslateX(rect.getTranslateX() + 25);
        text.setTranslateY(rect.getTranslateY() + 70);
        text.setWrapText(true);
        text.setMaxWidth(350);
        text.setMaxHeight(60);


        yesRect = new Rectangle(60, 60);
        yesRect.setTranslateX(rect.getTranslateX() + 130);
        yesRect.setTranslateY(rect.getTranslateY() + 140);
        yesRect.setTranslateZ(0);
        yesRect.setFill(Color.ROYALBLUE);
        yesRect.setStroke(Color.BLACK);

        StackPane yesPane = new StackPane();
        yesPane.setTranslateX(yesRect.getTranslateX());
        yesPane.setTranslateY(yesRect.getTranslateY());
        yesPane.setMaxWidth(yesRect.getWidth());
        yesPane.setMinWidth(yesRect.getWidth());
        yesPane.setMaxHeight(yesRect.getHeight());
        yesPane.setMinHeight(yesRect.getHeight());

        Text yesText = new Text();
        yesText.setText("YES");
        yesText.setFont(new Font("Forte", 22));
        yesText.setFill(Color.WHITE);
        yesText.setStroke(Color.BLACK);
        yesText.setStrokeWidth(1);
        yesText.setTextAlignment(TextAlignment.CENTER);
        yesPane.getChildren().add(yesText);

        yesPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadRestore(true);
                hide();
            }
        });

        noRect = new Rectangle(60, 60);
        noRect.setTranslateX(rect.getTranslateX() + 130 + yesRect.getWidth() + 20);
        noRect.setTranslateY(rect.getTranslateY() + 140);
        noRect.setTranslateZ(0);
        noRect.setFill(Color.INDIANRED);
        noRect.setStroke(Color.BLACK);

        StackPane noPane = new StackPane();
        noPane.setTranslateX(noRect.getTranslateX());
        noPane.setTranslateY(noRect.getTranslateY());
        noPane.setMaxWidth(noRect.getWidth());
        noPane.setMinWidth(noRect.getWidth());
        noPane.setMaxHeight(noRect.getHeight());
        noPane.setMinHeight(noRect.getHeight());

        Text noText = new Text();
        noText.setText("NO");
        noText.setFont(new Font("Forte", 22));
        noText.setFill(Color.WHITE);
        noText.setStroke(Color.BLACK);
        noText.setStrokeWidth(1);
        noText.setTextAlignment(TextAlignment.CENTER);
        noPane.getChildren().add(noText);

        noPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadRestore(false);
                hide();
            }
        });


        group.getChildren().add(rect);
        group.getChildren().add(titlePane);
        group.getChildren().add(text);
        group.getChildren().add(yesRect);
        group.getChildren().add(noRect);
        group.getChildren().add(yesPane);
        group.getChildren().add(noPane);

    }
}