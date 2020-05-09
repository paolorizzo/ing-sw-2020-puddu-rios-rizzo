package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

public class AthenaPowerTest extends PowerTest {

    @Override
    public void assertPruning(Turn turn,  ActionTree lastLayer) {
        for(Action action: turn.getActions()){
            if(action instanceof MoveAction)
                assert(((MoveAction) action).getDirection() != Direction.UP);
        }
    }

}

