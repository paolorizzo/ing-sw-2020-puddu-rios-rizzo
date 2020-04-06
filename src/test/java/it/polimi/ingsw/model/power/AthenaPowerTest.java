package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.power.ApolloPower;
import it.polimi.ingsw.model.power.PowerStrategy;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class AthenaPowerTest extends PowerTest {

    @Override
    public void assertPruning(Turn turn) {
        for(Action action: turn.getActions()){
            if(action instanceof MoveAction)
                assert(((MoveAction) action).getDirection() != Direction.UP);
        }
    }
}

