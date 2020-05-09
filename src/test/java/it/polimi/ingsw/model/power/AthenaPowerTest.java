package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

public class AthenaPowerTest extends PowerTest {

    @Override
    public void assertPruning(Board board, Player myself, Player other, Turn myLastTurn,  Turn otherTurn, ActionTree otherLastLayer) {
        if(myLastTurn == null){
            //no pruning
            return;
        }
        boolean moveUp = false;
        for(Action action: myLastTurn.getActions()){
            if(action instanceof MoveAction && ((MoveAction) action).getDirection() == Direction.UP)
                moveUp = true;
        }
        if(!moveUp){
            //no pruning
            return;
        }
        for(Action action: otherTurn.getActions()){
            if(action instanceof MoveAction)
                assert(((MoveAction) action).getDirection() != Direction.UP);
        }
    }

}

