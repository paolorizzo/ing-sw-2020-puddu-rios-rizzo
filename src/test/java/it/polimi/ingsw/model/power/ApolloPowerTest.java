package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.power.ApolloPower;
import it.polimi.ingsw.model.power.PowerStrategy;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ApolloPowerTest extends PowerTest {

    public void assertPower(Board board, Turn turn){
        int countMoveAndForce = 0, countMove = 0 , countBuild = 0;
        for(Action action: turn.getActions()){
            String workerID = action.getWorkerID();
            int targetX = action.getTargetX();
            int targetY = action.getTargetY();
            if(action instanceof MoveAndForceAction){
                assert(countBuild == 0);
                assert(countMove == 0);
                countMoveAndForce++;
                int startX = ((MoveAndForceAction) action).getStartX();
                int startY = ((MoveAndForceAction) action).getStartY();
                String forcedWorkerID = ((MoveAndForceAction) action).getForcedWorkerId();
                int forcedStartX = ((MoveAndForceAction) action).getForcedStartX();
                int forcedStartY = ((MoveAndForceAction) action).getForcedStartY();
                int forcedTargetX = ((MoveAndForceAction) action).getForcedTargetX();
                int forcedTargetY = ((MoveAndForceAction) action).getForcedTargetY();

                assert(workerID.equals(board.getSpaces()[startX][startY].getWorkerOnIt().toString()));
                assert(forcedWorkerID.equals(board.getSpaces()[forcedStartX][forcedStartY].getWorkerOnIt().toString()));
                assert(board.getSpaces()[startX][startY].getLevel()>=board.getSpaces()[targetX][targetY].getLevel()-1);
                assert(Math.abs(startX-targetX)<=1 && Math.abs(startY-targetY)<=1 && (startX-targetX != 0 || startY-targetY != 0));
                assert(targetX == forcedStartX);
                assert(targetY == forcedStartY);
                assert(forcedTargetX == startX);
                assert(forcedTargetY == startY);
            }else if(action instanceof MoveAction){
                assert(countMoveAndForce == 0);
                assert(countBuild == 0);
                countMove++;
                int startX = ((MoveAction) action).getStartX();
                int startY = ((MoveAction) action).getStartY();
                assert(workerID.equals(board.getSpaces()[startX][startY].getWorkerOnIt().toString()));
                assert(board.getSpaces()[startX][startY].getLevel()>=board.getSpaces()[targetX][targetY].getLevel()-1);
                assert(Math.abs(startX-targetX)<=1 && Math.abs(startY-targetY)<=1 && (startX-targetX != 0 || startY-targetY != 0));

            }else if(action instanceof BuildAction){
                assert(countMove != 0 || countMoveAndForce != 0);
                countBuild++;
                Piece piece = ((BuildAction) action).getPiece();
                assert(board.getSpaces()[targetX][targetY].getLastPiece().nextPiece() == piece);
            } else {
                assert(false);
            }
            board.executeAction(action);
        }
        assert(countBuild <= 1);
        assert(countMoveAndForce+countMove <= 1);
    }

}

