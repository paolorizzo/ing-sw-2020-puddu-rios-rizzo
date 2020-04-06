package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

public class HephaestusPowerTest extends PowerTest {

    public void assertPower(Board board, Turn turn, ActionTree lastLayer){
        int countMove = 0 , countBuild = 0;
        int oldTargetX = 0, oldTargetY = 0;
        for(Action action: turn.getActions()){
            String workerID = action.getWorkerID();
            int targetX = action.getTargetX();
            int targetY = action.getTargetY();
            if(action instanceof MoveAction){
                assert(countBuild == 0);
                countMove++;
                int startX = ((MoveAction) action).getStartX();
                int startY = ((MoveAction) action).getStartY();
                assert(workerID.equals(board.getSpaces()[startX][startY].getWorkerOnIt().toString()));
                assert(board.getSpaces()[startX][startY].getLevel()>=board.getSpaces()[targetX][targetY].getLevel()-1);
                assert(Math.abs(startX-targetX)<=1 && Math.abs(startY-targetY)<=1 && (startX-targetX != 0 || startY-targetY != 0));
                if(lastLayer.getAction() == action && lastLayer.isWin()){
                    assert(((MoveAction) action).getDirection() == Direction.UP);
                    assert(board.getSpaces()[startX][startY].getLevel() == 2 && board.getSpaces()[targetX][targetY].getLevel() == 3);
                }else if(lastLayer.getAction() == action){
                    assert(lastLayer.isLose());
                }
            }else if(action instanceof BuildAction){
                assert(countMove != 0);
                Piece piece = ((BuildAction) action).getPiece();
                assert(board.getSpaces()[targetX][targetY].getLastPiece().nextPiece() == piece);
                if(countBuild == 0){
                    oldTargetX = targetX;
                    oldTargetY = targetY;
                }else{
                    assert (countBuild == 1);
                    assert(oldTargetX == targetX && oldTargetY == targetY);
                }

                countBuild++;
            } else {
                assert(false);
            }
            board.executeAction(action);
        }

        if(lastLayer.isWin()){
            assert(countMove == 1);
        }else if(lastLayer.isLose()){
            assert(countMove == 0 || (countMove == 1 && countBuild == 0));
        }else{
            assert(countBuild == 1 || countBuild == 2);
            assert(countMove == 1);
        }
    }

}
