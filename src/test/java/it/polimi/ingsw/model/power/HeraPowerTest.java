package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

public class HeraPowerTest extends PowerTest{
    @Override
    public void assertPruning(Turn turn, ActionTree lastLayer) {
        Action action = lastLayer.getAction();
        if(lastLayer.isLose())
            return;
        int targetX = action.getTargetX();
        int targetY = action.getTargetY();
        if(action instanceof MoveAction && lastLayer.isWin())
            assert(!(targetX == 0 || targetY == 0 || targetX == 4 || targetY == 4));

    }
}
