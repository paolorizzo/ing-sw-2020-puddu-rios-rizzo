package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
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


public class PlayersMenu extends Menu {
    private int offsetX, offsetY;

    private Text [] playerNames;
    private Label [] playerCardLabels;
    private Label currentPlayerLabel;
    public PlayersMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        offsetX = 20;
        offsetY = 20;
        currentPlayerLabel = new Label("");
        Image image = GraphicsLoader.instance().getImage("CurrentPlayer");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(100);
        currentPlayerLabel.setGraphic(imageView);
        currentPlayerLabel.setVisible(false);
        group.getChildren().add(currentPlayerLabel);
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
                final Rectangle rectDesc = new Rectangle(250,200);
                rectDesc.setFill(new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false));
                rectDesc.setStroke(Color.BLACK);
                rectDesc.setStrokeWidth(2);
                rectDesc.setVisible(false);

                StackPane namePane = new StackPane();
                namePane.setTranslateX(rectDesc.getTranslateX() + 10);
                namePane.setTranslateY(rectDesc.getTranslateY() + 10);

                Rectangle nameRect = new Rectangle(230, 30);
                nameRect.setVisible(false);
                namePane.getChildren().add(nameRect);

                Text nameText = new Text();
                nameText.setText(player.getCard().getName());
                nameText.setFont(new Font("Forte", 25));
                nameText.setFill(Color.GOLD);
                nameText.setStroke(Color.BLACK);
                nameText.setStrokeWidth(1);
                nameText.setVisible(false);
                namePane.getChildren().add(nameText);


                final Label cardDesc = new Label(player.getCard().getDescription());
                cardDesc.setFont(new Font("Forte", 16));
                cardDesc.setWrapText(true);
                cardDesc.setMaxHeight(145);
                cardDesc.setMaxWidth(230);
                cardDesc.setVisible(false);

                playerCardLabels[id].setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        rectDesc.setTranslateX(playerCardLabels[id].getTranslateX()+playerCardLabels[id].getWidth()-10);
                        rectDesc.setTranslateY(playerCardLabels[id].getTranslateY()+playerCardLabels[id].getHeight()/3);

                        namePane.setTranslateX(rectDesc.getTranslateX() + 10);
                        namePane.setTranslateY(rectDesc.getTranslateY() + 10);

                        nameText.setTextAlignment(TextAlignment.CENTER);
                        nameText.setTranslateY(0);

                        cardDesc.setTranslateX(rectDesc.getTranslateX()+10);
                        cardDesc.setTranslateY(rectDesc.getTranslateY()+45);

                        cardDesc.setVisible(true);
                        rectDesc.setVisible(true);
                        nameText.setVisible(true);

                        rectDesc.toFront();
                        cardDesc.toFront();
                        namePane.toFront();
                        nameText.toFront();
                    }
                });
                playerCardLabels[id].setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        cardDesc.setVisible(false);
                        rectDesc.setVisible(false);
                        nameText.setVisible(false);
                    }
                });
                group.getChildren().add(rectDesc);
                group.getChildren().add(cardDesc);
                group.getChildren().add(namePane);
            }
        });



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
                    group.getChildren().add(playerCardLabels[i]);

                    group.getChildren().add(playerPane);
                    offsetX += 90+15;
                }
            }
        });
    }

    public void setCurrentPlayer(int id){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currentPlayerLabel.setTranslateX(playerCardLabels[id].getTranslateX());
                currentPlayerLabel.setTranslateY(playerCardLabels[id].getTranslateY()+playerCardLabels[id].getGraphic().getLayoutBounds().getHeight()+10);
                currentPlayerLabel.setVisible(true);
            }
        });
    }

}
