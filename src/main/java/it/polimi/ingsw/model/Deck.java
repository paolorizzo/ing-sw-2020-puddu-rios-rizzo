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
        cardsInDeck.put(3, new Card(3, "Athena", "Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.", new AthenaPower()));
        cardsInDeck.put(4, new Card(4,"Atlas", "Your Build: Your Worker may build a dome at any level.", new AtlasPower()));
        cardsInDeck.put(5, new Card(5, "Demeter","Your Build: Your Worker may build one additional time, but not on the same space.", new DemeterPower()));
        cardsInDeck.put(6, new Card(6, "Hephaestus", "Your Build: Your Worker may build one additional block (not dome) on top of your first block.", new HephaestusPower()));
        cardsInDeck.put(8, new Card(8, "Minotaur", "Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.", new MinotaurPower()));
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
