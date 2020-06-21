package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.model.power.*;
import it.polimi.ingsw.view.cli.RectangleCLI;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Deck implements Serializable {
    HashMap<Integer, Card> cardsInDeck;

    /**
     * constructs a deck that contains all the cards that have been implemented so far
     */
    public Deck(){
        cardsInDeck = new HashMap<>();
        loadCards();
    }
    /**
     * reads the cards from a json file and adds them to the deck
     */
    private void loadCards(){
        try{
            StringBuilder stringCards = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(RectangleCLI.class.getResourceAsStream("/cards.json")));
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
    /**
     * utility methods that allows the user to get a Card object from an integer representing its number
     * once a card has been picked, it is removed form the deck
     * @param num the number of the card that is being picked
     * @return the actual card that corresponds to that number in the map
     */
    public Card pickCard(int num){
        if(!cardsInDeck.containsKey(num))
            throw new IllegalArgumentException("Card number "+num+" alredy used!");
        Card card = cardsInDeck.get(num);
        cardsInDeck.remove(num);
        return card;
    }
    /**
     *
     * @return the list of all the cards that are present in the deck
     */
    public List<Card> getCards(){
        List<Card> listOfCards = new ArrayList<>();
        for(Card card: cardsInDeck.values())
            listOfCards.add(card);
        return listOfCards;
    }

    /**
     * compares this object against any other
     * @param o the other object
     * @return true if the other object is a Deck, and their remaining cards match
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Deck)) return false;
        Deck that = (Deck) o;
        return this.cardsInDeck.equals(that.cardsInDeck);
    }
}
