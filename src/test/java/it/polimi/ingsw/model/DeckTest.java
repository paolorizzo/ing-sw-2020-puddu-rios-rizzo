package it.polimi.ingsw.model;

import org.junit.Test;

public class DeckTest {
    @Test
    public void pickCardTest(){
        Deck deck = new Deck();
        Card card = deck.pickCard(1);
        assert(!deck.getCards().contains(card));
    }
    @Test(expected = IllegalArgumentException.class)
    public void checkNoDoublePick()
    {
        Deck deck = new Deck();
        deck.pickCard(1);
        deck.pickCard(1);
    }
}
