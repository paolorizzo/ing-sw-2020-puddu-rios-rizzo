package it.polimi.ingsw.view.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * Menu to choose the own god
 */
public class AskGodMenu extends Menu {
    private Rectangle rect;
    private HashMap<Integer, Label> cardLabels;
    /**
     * It creates and sets the menu graphics to choose the own god
     * @param widthResolution the width resolution of window
     * @param heightResolution hr the height resolution of window
     */
    public AskGodMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        rect = new Rectangle(460, 290);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2-rect.getWidth()/2);
        rect.setTranslateY(heightResolution/2-rect.getHeight()/2);
        rect.setTranslateZ(0);

        group.getChildren().add(rect);

        StackPane titlePane = new StackPane();
        titlePane.setTranslateX(rect.getTranslateX() + 10);
        titlePane.setTranslateY(rect.getTranslateY() + 25);
        titlePane.setMaxWidth(rect.getWidth()-20);
        titlePane.setMinWidth(rect.getWidth()-20);
        titlePane.setMaxHeight(30);
        titlePane.setMinHeight(30);

        Text title = new Text();
        title.setText("CHOOSE YOUR GOD");
        title.setFont(new Font("Forte", 30));
        title.setFill(Color.GOLD);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(1);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(0);
        titlePane.getChildren().add(title);

        group.getChildren().add(titlePane);

        cardLabels = new HashMap<>();

        try{
            StringBuilder stringCards = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(Deck.class.getResourceAsStream("/cards.json")));
            try{
                while (reader.ready()) {
                    String line = reader.readLine();
                    stringCards.append(line + System.lineSeparator());
                }
            }
            catch(IOException e)
            {
                System.out.println("error in reading the file");
            }

            JsonObject jsonObject = (JsonObject) new JsonParser().parse(stringCards.toString());
            JsonArray cards = jsonObject.get("cards").getAsJsonArray();

            for(final JsonElement card: cards){
                final int num = card.getAsJsonObject().get("num").getAsInt();
                final String name = card.getAsJsonObject().get("name").getAsString();
                final String desc = card.getAsJsonObject().get("desc").getAsString();
                Image image = GraphicsLoader.instance().getImage(name);
                cardLabels.put(num, new Label(""));
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(180);
                imageView.setFitWidth(120);
                cardLabels.get(num).setGraphic(imageView);
                cardLabels.get(num).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        hide();
                        notifyReadGod(num);
                    }
                });
                cardLabels.get(num).setVisible(false);
                group.getChildren().add(cardLabels.get(num));

                final Rectangle rectDesc = new Rectangle(250,200);
                rectDesc.setFill(patt);
                rectDesc.setStroke(Color.BLACK);
                rectDesc.setStrokeWidth(2);
                rectDesc.setVisible(false);

                StackPane namePane = new StackPane();
                namePane.setTranslateX(rect.getTranslateX() + 10);
                namePane.setTranslateY(rect.getTranslateY() + 10);

                Rectangle nameRect = new Rectangle(230, 30);
                nameRect.setVisible(false);
                namePane.getChildren().add(nameRect);

                Text nameText = new Text();
                nameText.setText(name);
                nameText.setFont(new Font("Forte", 25));
                nameText.setFill(Color.GOLD);
                nameText.setStroke(Color.BLACK);
                nameText.setStrokeWidth(1);
                nameText.setVisible(false);
                namePane.getChildren().add(nameText);


                final Label cardDesc = new Label(desc);
                cardDesc.setFont(new Font("Forte", 16));
                cardDesc.setWrapText(true);
                cardDesc.setMaxHeight(145);
                cardDesc.setMaxWidth(230);
                cardDesc.setVisible(false);

                cardLabels.get(num).setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        rectDesc.setTranslateX(cardLabels.get(num).getTranslateX()+cardLabels.get(num).getWidth()-10);
                        rectDesc.setTranslateY(cardLabels.get(num).getTranslateY()+cardLabels.get(num).getHeight()/3);

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
                cardLabels.get(num).setOnMouseExited(new EventHandler<MouseEvent>() {
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
        }catch (Exception e){
            System.out.println("Error during the loading of deck: "+e.getMessage());
        }
    }

    /**
     * It places and shows the remained cards
     * @param cards a list of remained cards
     */
    public void setCards(List<Card> cards){
        double offsetX = 10;
        int offsetY = 80;
        for(Card card: cards){
            offsetX += (rect.getWidth()-20-cardLabels.get(card.getNum()).getWidth()*cards.size())/((2+cards.size()-1));
            cardLabels.get(card.getNum()).setVisible(true);
            cardLabels.get(card.getNum()).setTranslateX(rect.getTranslateX() + offsetX);
            cardLabels.get(card.getNum()).setTranslateY(rect.getTranslateY() + offsetY);
            offsetX += cardLabels.get(card.getNum()).getWidth();
        }
    }

}