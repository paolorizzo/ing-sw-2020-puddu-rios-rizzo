package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

public class HypnusPowerTest extends PowerTest {

    @Override
    public void assertPruning(Board board, Player myself, Player other, Turn myLastTurn,  Turn otherTurn, ActionTree otherLastLayer) {
        Worker m = other.getWorker(Sex.MALE);
        Worker f = other.getWorker(Sex.FEMALE);
        Worker cantmoveworker = null;
        if(m.getSpace().getLevel()>f.getSpace().getLevel()){
            cantmoveworker = m;
        }else if(f.getSpace().getLevel()>m.getSpace().getLevel()){
            cantmoveworker = f;
        }
        if(cantmoveworker!=null){
            for(Action action: otherTurn.getActions()){
                if(action instanceof MoveAction)
                    assert(!action.getWorkerID().equals(cantmoveworker.toString()));
            }
        }

    }
}
