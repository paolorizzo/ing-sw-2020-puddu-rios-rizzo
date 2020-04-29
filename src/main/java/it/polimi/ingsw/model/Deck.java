package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.model.power.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Deck implements Serializable {
    HashMap<Integer, Card> cardsInDeck;

    public Deck(){
        cardsInDeck = new HashMap<>();
        loadCard();
    }
    private void loadCard(){
        try{
            File file = new File("./src/main/resources/cards.json");
            StringBuilder stringCards = new StringBuilder((int)file.length());
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                stringCards.append(scanner.nextLine() + System.lineSeparator());
            }

            JsonObject jsonObject = (JsonObject) new JsonParser().parse(stringCards.toString());
            JsonArray cards = jsonObject.get("cards").getAsJsonArray();

            for(JsonElement card: cards){
                int num = card.getAsJsonObject().get("num").getAsInt();
                String name = card.getAsJsonObject().get("name").getAsString();
                String desc = card.getAsJsonObject().get("desc").getAsString();
                PowerStrategy power = (PowerStrategy) Class.forName("it.polimi.ingsw.model.power."+name+"Power").newInstance();
                cardsInDeck.put(num, new Card(num, name, desc, power));
            }
        }catch (Exception e){
            System.out.println("Error during the loading of deck: "+e.getMessage());
        }

    }
    public Card pickCard(int num){
        if(!cardsInDeck.containsKey(num))
            throw new IllegalArgumentException("Card number "+num+" alredy used!");
        Card card = cardsInDeck.get(num);
        cardsInDeck.remove(num);
        return card;
    }
    public List<Card> getCards(){
        List<Card> listOfCards = new ArrayList<>();
        for(Card card: cardsInDeck.values())
            listOfCards.add(card);
        return listOfCards;
    }
}
