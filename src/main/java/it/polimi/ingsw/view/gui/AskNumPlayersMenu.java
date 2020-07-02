package it.polimi.ingsw.view.gui;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Menu to ask and get the number of player of the game
 */
public class AskNumPlayersMenu extends Menu {
    private Rectangle rect;
    /**
     * It creates and sets the menu graphics to choose the own god
     * @param widthResolution the width resolution of window
     * @param heightResolution hr the height resolution of window
     */
    public AskNumPlayersMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        rect = new Rectangle(390, 240);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2 - rect.getWidth() / 2);
        rect.setTranslateY(heightResolution/2 - rect.getHeight() / 2);
        rect.setTranslateZ(0);

        StackPane titlePane = new StackPane();
        titlePane.setTranslateX(rect.getTranslateX() + 10);
        titlePane.setTranslateY(rect.getTranslateY() + 20);
        titlePane.setMaxWidth(rect.getWidth()-20);
        titlePane.setMinWidth(rect.getWidth()-20);
        titlePane.setMaxHeight(30);
        titlePane.setMinHeight(30);

        Text title = new Text();
        title.setText("START GAME");
        title.setFont(new Font("Forte", 30));
        title.setFill(Color.GOLD);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(1);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(0);

        titlePane.getChildren().add(title);


        Image twoImage = GraphicsLoader.instance().getImage("two_players");
        ImageView twoImageView = new ImageView(twoImage);
        twoImageView.setFitHeight(150);
        twoImageView.setFitWidth(150);
        Label twoButton = new Label("");
        twoButton.setTranslateX(rect.getTranslateX() + 30);
        twoButton.setTranslateY(rect.getTranslateY() + 70);
        twoButton.setGraphic(twoImageView);
        twoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadNumPlayers(2);
                hide();
            }
        });

        StackPane twoTextPane = new StackPane();
        twoTextPane.setTranslateX(twoButton.getTranslateX());
        twoTextPane.setTranslateY(twoButton.getTranslateY() + 120);

        Rectangle twoTextRect = new Rectangle(twoButton.getWidth(), 30);
        twoTextRect.setVisible(false);
        twoTextPane.getChildren().add(twoTextRect);

        Text twoText = new Text();
        twoText.setText("TWO PLAYERS");
        twoText.setFont(new Font("Forte", 22));
        twoText.setFill(Color.GOLD);
        twoText.setStroke(Color.BLACK);
        twoText.setStrokeWidth(1);
        twoText.setTextAlignment(TextAlignment.CENTER);
        twoText.setTranslateY(0);
        twoTextPane.getChildren().add(twoText);
        twoTextPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadNumPlayers(2);
                hide();
            }
        });

        Image threeImage = GraphicsLoader.instance().getImage("three_players");
        ImageView threeImageView = new ImageView(threeImage);
        threeImageView.setFitHeight(150);
        threeImageView.setFitWidth(150);
        Label threeButton = new Label("");
        threeButton.setTranslateX(rect.getTranslateX() + 210);
        threeButton.setTranslateY(rect.getTranslateY() + 70);
        threeButton.setGraphic(threeImageView);
        threeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadNumPlayers(3);
                hide();
            }
        });

        StackPane threeTextPane = new StackPane();
        threeTextPane.setTranslateX(threeButton.getTranslateX());
        threeTextPane.setTranslateY(threeButton.getTranslateY() + 120);

        Rectangle threeTextRect = new Rectangle(threeButton.getWidth(), 30);
        threeTextRect.setVisible(false);
        threeTextPane.getChildren().add(threeTextRect);

        Text threeText = new Text();
        threeText.setText("THREE PLAYERS");
        threeText.setFont(new Font("Forte", 22));
        threeText.setFill(Color.GOLD);
        threeText.setStroke(Color.BLACK);
        threeText.setStrokeWidth(1);
        threeText.setTextAlignment(TextAlignment.CENTER);
        threeText.setTranslateY(0);
        threeTextPane.getChildren().add(threeText);
        threeTextPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notifyReadNumPlayers(3);
                hide();
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(titlePane);
        group.getChildren().add(twoButton);
        group.getChildren().add(twoTextPane);
        group.getChildren().add(threeButton);
        group.getChildren().add(threeTextPane);

    }
}