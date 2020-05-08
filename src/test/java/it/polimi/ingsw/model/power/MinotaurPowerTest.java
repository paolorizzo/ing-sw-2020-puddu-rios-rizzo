package it.polimi.ingsw.model.power;

import it.polimi.ingsw.exception.InvalidActionTreeGenerationException;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.Arrays;

public class MinotaurPowerTest extends PowerTest {

    public void assertPower(Board board, Turn turn, ActionTree lastLayer){
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

                assert(forcedTargetX == 2*targetX-startX);
                assert(forcedTargetX<5 && forcedTargetX>=0);
                assert(forcedTargetY == 2*targetY-startY);
                assert(forcedTargetY<5 && forcedTargetY>=0);

                if(lastLayer.getAction() == action && lastLayer.isWin()){
                    assert(((MoveAction) action).getDirection() == Direction.UP);
                    assert(board.getSpaces()[startX][startY].getLevel() == 2 && board.getSpaces()[targetX][targetY].getLevel() == 3);
                }else if(lastLayer.getAction() == action){
                    assert(lastLayer.isLose());
                }

            }else if(action instanceof MoveAction){
                assert(countMoveAndForce == 0);
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
                assert(countMove != 0 || countMoveAndForce != 0);
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
            assert(countMove+countMoveAndForce == 1);
        }else if(lastLayer.isLose()){
            assert(countMove+countMoveAndForce == 0 || (countMove+countMoveAndForce == 1 && countBuild == 0));
        }else{
            assert(countBuild == 1);
            assert(countMove+countMoveAndForce == 1);
        }

    }
    @Test(expected = InvalidActionTreeGenerationException.class)
    public void tryMoveAfterAction()
    {
        PowerStrategy pw = new MinotaurPower();
        ActionTree t = new ActionTree(new MoveAction("P0-M", 1, 1, Direction.UP, 0, 0), false, false, false, true);
        Player p0 = new Player(0, "name1");
        Player p1 = new Player(1, "name2");
        pw.addMoveLayer(t, p0, new Board());
    }
}
