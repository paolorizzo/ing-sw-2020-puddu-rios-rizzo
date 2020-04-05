package it.polimi.ingsw.model;

import it.polimi.ingsw.model.power.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Deck {
    HashMap<Integer, Card> cardsInDeck;
    public Deck(){
        cardsInDeck = new HashMap<>();
        cardsInDeck.put(1, new Card(1,"Apollo", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated.", new ApolloPower()));
        cardsInDeck.put(2, new Card(2,"Artemis", "Your Move: Your Worker may move one additional time, but not back to its initial space. ", new ArtemisPower()));
        cardsInDeck.put(4, new Card(4,"Atlas", "Your Build: Your Worker may build a dome at any level.", new AtlasPower()));
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
