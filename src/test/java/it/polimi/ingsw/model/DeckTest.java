package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

    /**
     * tests that the equals method is working as intended
     */
    @Test
    public void checkEquals(){
        Controller c1 = new Controller();
        ControllerTest ct1 = new ControllerTest();
        ct1.playSomeTurns(c1);
        Deck d1 = c1.getModel().game.getDeck();
        Controller c2 = new Controller();
        ControllerTest ct2 = new ControllerTest();
        Deck d2 = c2.getModel().game.getDeck();

        assertNotEquals(d1, d2);
        ct2.playSomeTurns(c2);
        assertEquals(d1, d2);
    }
}
