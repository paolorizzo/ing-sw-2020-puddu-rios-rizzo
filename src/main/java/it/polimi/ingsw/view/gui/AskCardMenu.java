package it.polimi.ingsw.view.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.power.PowerStrategy;
import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AskCardMenu extends Menu {
    Rectangle rect;
    HashMap<Integer, Label> cardLabels;
    public AskCardMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        rect = new Rectangle(740, 650);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2-rect.getWidth()/2);
        rect.setTranslateY(heightResolution/2-rect.getHeight()/2);
        rect.setTranslateZ(0);

        group.getChildren().add(rect);
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
                        notifyReadNumCard(num);
                    }
                });
                group.getChildren().add(cardLabels.get(num));
                final Rectangle rectDesc = new Rectangle(200,130);
                rectDesc.setFill(Color.LIGHTGRAY);
                rectDesc.setStroke(Color.BLACK);
                rectDesc.setStrokeWidth(2);
                rectDesc.setVisible(false);
                final Label cardDesc = new Label(name+"\n"+desc);
                cardDesc.setWrapText(true);
                cardDesc.setMaxHeight(110);
                cardDesc.setMaxWidth(180);
                cardDesc.setVisible(false);
                rectDesc.toFront();
                cardDesc.toFront();
                cardLabels.get(num).setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        rectDesc.setTranslateX(cardLabels.get(num).getTranslateX()+cardLabels.get(num).getWidth()-10);
                        rectDesc.setTranslateY(cardLabels.get(num).getTranslateY()+cardLabels.get(num).getHeight()/3);
                        cardDesc.setTranslateX(rectDesc.getTranslateX()+10);
                        cardDesc.setTranslateY(rectDesc.getTranslateY()+10);
                        cardDesc.setVisible(true);
                        rectDesc.setVisible(true);
                        rectDesc.toFront();
                        cardDesc.toFront();
                    }
                });
                cardLabels.get(num).setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        cardDesc.setVisible(false);
                        rectDesc.setVisible(false);
                    }
                });
                group.getChildren().add(rectDesc);
                group.getChildren().add(cardDesc);

            }
        }catch (Exception e){
            System.out.println("Error during the loading of deck: "+e.getMessage());
        }

    }
    public void setDeck(Deck deck){
        int offsetX = 30, offsetY = 30;
        List<Card> cards = deck.getCards();
        int countcard = 0;
        for(Card card: cards){
            cardLabels.get(card.getNum()).setVisible(true);
            cardLabels.get(card.getNum()).setTranslateX(rect.getTranslateX() + offsetX);
            cardLabels.get(card.getNum()).setTranslateY(rect.getTranslateY() + offsetY);
            offsetX +=140;
            countcard++;
            if(countcard%5 == 0){
                offsetX = 30;
                offsetY +=200;
            }
        }
    }
    public void hide(){
        group.setVisible(false);
        if(cardLabels != null){

            for(Label cardLabel: cardLabels.values()){
                cardLabel.setVisible(false);
            }
        }
    }
}