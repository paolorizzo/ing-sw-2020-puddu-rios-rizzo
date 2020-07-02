package it.polimi.ingsw.model;

import it.polimi.ingsw.model.power.ApolloPower;
import it.polimi.ingsw.model.power.PowerStrategy;
import org.junit.Test;

public class CardTest {
    @Test
    public void constructorTest(){
        PowerStrategy power = new ApolloPower();
        Card card = new Card(1,"Apollo", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated.", power);
        assert(card.getNum() == 1);
        assert(card.getName().equals("Apollo"));
        assert(card.getDescription().equals("Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));
        assert(card.getPowerStrategy() == power);
    }
    @Test
    public void cloneTest(){
        PowerStrategy power = new ApolloPower();
        Card card = new Card(1,"Apollo", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated.", power);
        Card clone = card.clone();
        assert(clone.equals(card));
        assert(clone != card);
    }
}
