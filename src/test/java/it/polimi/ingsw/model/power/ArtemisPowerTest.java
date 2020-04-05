package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.power.ApolloPower;
import it.polimi.ingsw.model.power.PowerStrategy;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ArtemisPowerTest extends PowerTest {

    public void assertPower(Board board, Turn turn){
        int countMove = 0 , countBuild = 0;
        int oldStartX = -1, oldStartY = -1;
        for(Action action: turn.getActions()){
            String workerID = action.getWorkerID();
            int targetX = action.getTargetX();
            int targetY = action.getTargetY();
            if(action instanceof MoveAction){
                assert(countBuild == 0);
                countMove++;
                int startX = ((MoveAction) action).getStartX();
                int startY = ((MoveAction) action).getStartY();
                if(countMove == 0){
                    oldStartX = startX;
                    oldStartY = startY;
                }
                assert(workerID.equals(board.getSpaces()[startX][startY].getWorkerOnIt().toString()));
                assert(board.getSpaces()[startX][startY].getLevel()>=board.getSpaces()[targetX][targetY].getLevel()-1);
                assert(Math.abs(startX-targetX)<=1 && Math.abs(startY-targetY)<=1 && (startX-targetX != 0 || startY-targetY != 0));
                if(countMove == 1){
                    assert(targetX != oldStartX || targetY != oldStartY);
                }
            }else if(action instanceof BuildAction){
                assert(countMove != 0);
                countBuild++;
                Piece piece = ((BuildAction) action).getPiece();
                assert(board.getSpaces()[targetX][targetY].getLastPiece().nextPiece() == piece);
            } else {
                assert(false);
            }
            board.executeAction(action);
        }
        assert(countBuild <= 1);
        assert(countMove <= 1 || countMove <= 2);
    }

}

