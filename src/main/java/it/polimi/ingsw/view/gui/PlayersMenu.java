package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class PlayersMenu extends Group {
    private int offsetX, offsetY;

    private Text [] playerNames;
    private Label [] playerCardLabels;

    public PlayersMenu(){
        offsetX = 20;
        offsetY = 20;
    }
    public void addNamePlayer(Player player){
        String name = player.getNickname();
        int id = player.getId();

        playerNames[id].setText(name);
        switch (player.getColor().toString()){
            case "WHITE":
                playerNames[id].setFill(Color.WHITE);
                break;
            case "ORANGE":
                playerNames[id].setFill(Color.ORANGE);
                break;
            case "BLUE":
                playerNames[id].setFill(Color.ROYALBLUE);
                break;
        }

    }
    public void addGodPlayer(Player player){

        String nameGod = player.getCard().getName();
        final int id = player.getId();

        Image image = GraphicsLoader.instance().getImage(nameGod);
        final ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(100);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                playerCardLabels[id].setGraphic(imageView);
            }
        });

    }
    public void setPlayerTurn(int id){

    }
    public void setNumPlayers(final int numPlayers){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                playerNames = new Text[numPlayers];
                playerCardLabels = new Label[numPlayers];
                for(int i=0;i<numPlayers;i++){
                    StackPane playerPane = new StackPane();
                    playerPane.setTranslateX(offsetX);
                    playerPane.setTranslateY(offsetY);

                    Rectangle rect = new Rectangle(100, 30);
                    rect.setVisible(false);
                    playerPane.getChildren().add(rect);

                    playerNames[i] = new Text();
                    playerNames[i].setText("???");
                    playerNames[i].setFont(new Font("Forte", 30));
                    playerNames[i].setFill(Color.GRAY);
                    playerNames[i].setStroke(Color.BLACK);
                    playerNames[i].setStrokeWidth(1);
                    playerNames[i].setTextAlignment(TextAlignment.CENTER);
                    playerNames[i].setTranslateY(0);

                    playerPane.getChildren().add(playerNames[i]);

                    playerCardLabels[i] = new Label("");
                    Image image = GraphicsLoader.instance().getImage("Anonymous");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(100);
                    playerCardLabels[i].setGraphic(imageView);
                    playerCardLabels[i].setVisible(true);
                    playerCardLabels[i].setTranslateX(offsetX);
                    playerCardLabels[i].setTranslateY(offsetY + rect.getHeight() + 20);
                    getChildren().add(playerCardLabels[i]);

                    getChildren().add(playerPane);
                    offsetX += 90+15;
                }
            }
        });

    }
}
