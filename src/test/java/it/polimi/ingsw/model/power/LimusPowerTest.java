package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

public class LimusPowerTest extends PowerTest {

    @Override
    public void assertPruning(Board board, Player myself, Player other, Turn myLastTurn,  Turn otherTurn, ActionTree otherLastLayer) {
        for(Action action: otherTurn.getActions()){
            if(action instanceof BuildAction){
                Space target = board.getSpaces()[action.getTargetX()][action.getTargetY()];
                assert(!target.getAdjacentSpaces().contains(myself.getWorker(Sex.MALE).getSpace()));
                assert(!target.getAdjacentSpaces().contains(myself.getWorker(Sex.FEMALE).getSpace()));
            }
        }
    }

}

