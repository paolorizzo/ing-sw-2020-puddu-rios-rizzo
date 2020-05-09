package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

public class HeraPowerTest extends PowerTest{
    @Override
    public void assertPruning(Board board, Player myself, Player other, Turn myLastTurn,  Turn otherTurn, ActionTree otherLastLayer) {
        Action action = otherLastLayer.getAction();
        if(otherLastLayer.isLose())
            return;
        int targetX = action.getTargetX();
        int targetY = action.getTargetY();
        if(action instanceof MoveAction && otherLastLayer.isWin())
            assert(!(targetX == 0 || targetY == 0 || targetX == 4 || targetY == 4));

    }
}
