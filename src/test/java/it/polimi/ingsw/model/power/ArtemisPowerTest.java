package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.power.ApolloPower;
import it.polimi.ingsw.model.power.PowerStrategy;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ArtemisPowerTest extends PowerTest {

    public void assertPower(Board board, Turn turn, ActionTree lastLayer){
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
                if(lastLayer.getAction() == action && lastLayer.isWin()){
                    assert(((MoveAction) action).getDirection() == Direction.UP);
                    assert(board.getSpaces()[startX][startY].getLevel() == 2 && board.getSpaces()[targetX][targetY].getLevel() == 3);
                }else if(lastLayer.getAction() == action){
                    assert(lastLayer.isLose());
                }

            }else if(action instanceof BuildAction){
                assert(countMove != 0);
                countBuild++;
                Piece piece = ((BuildAction) action).getPiece();
                assert(board.getSpaces()[targetX][targetY].getLastPiece().nextPiece() == piece);
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++){
                        Space space = board.getSpaces()[i][j];
                        if(space.hasWorkerOnIt() && space.getWorkerOnIt().toString().equals(workerID))
                            assert(board.getSpaces()[targetX][targetY].getAdjacentSpaces().contains(space));
                    }
                }
            } else {
                assert(false);
            }
            board.executeAction(action);
        }
        if(lastLayer.isWin()){
            assert(countMove == 1 || countMove == 2);
        }else if(lastLayer.isLose()){
            assert(countMove == 0 || (countMove == 1 && countBuild == 0) || (countMove == 2 && countBuild == 0));
        }else{
            assert(countBuild == 1);
            assert(countMove == 1 || countMove == 2);
        }
    }

}

