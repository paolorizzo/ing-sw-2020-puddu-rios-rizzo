package it.polimi.ingsw.model.power;

import it.polimi.ingsw.exception.InvalidActionTreeGenerationException;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.Arrays;

public class PrometheusPowerTest extends PowerTest {

    public void assertPower(Board board, Turn turn, ActionTree lastLayer){
        int countMove = 0 , countBuild = 0;
        for(Action action: turn.getActions()){
            String workerID = action.getWorkerID();
            int targetX = action.getTargetX();
            int targetY = action.getTargetY();
            if(action instanceof MoveAction){
                if(countBuild == 1){
                    countMove++;
                    int startX = ((MoveAction) action).getStartX();
                    int startY = ((MoveAction) action).getStartY();
                    assert(workerID.equals(board.getSpaces()[startX][startY].getWorkerOnIt().toString()));
                    assert(board.getSpaces()[startX][startY].getLevel()>=board.getSpaces()[targetX][targetY].getLevel()); //don't go up
                    assert(Math.abs(startX-targetX)<=1 && Math.abs(startY-targetY)<=1 && (startX-targetX != 0 || startY-targetY != 0));
                    if(lastLayer.getAction() == action && lastLayer.isWin()){
                        assert(((MoveAction) action).getDirection() == Direction.UP);
                        assert(board.getSpaces()[startX][startY].getLevel() == 2 && board.getSpaces()[targetX][targetY].getLevel() == 3);
                    }else if(lastLayer.getAction() == action){
                        assert(lastLayer.isLose());
                    }
                }else{
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
                }
            }else if(action instanceof BuildAction){
                assert((countMove == 1 && countBuild == 0) || (countBuild==1 && countMove == 1) || (countBuild==0 && countMove == 0));
                countBuild++;
                Piece piece = ((BuildAction) action).getPiece();
                assert(board.getSpaces()[targetX][targetY].getLastPiece().nextPiece() == piece);
            } else {
                assert(false);
            }
            board.executeAction(action);
        }
        if(lastLayer.isWin()){
            assert(countMove == 1);
        }else if(lastLayer.isLose()){
            assert(countMove == 0 || (countMove == 1 && countBuild == 0) || (countBuild == 1 && countMove == 1));
        }else{
            assert(countBuild == 1 || countBuild == 2);
            assert(countMove == 1);
        }
    }

    @Test(expected = InvalidActionTreeGenerationException.class)
    public void tryFirstBuildAfterAction() {
        PowerStrategy pw = new PrometheusPower();
        ActionTree t = new ActionTree(new MoveAction("P0-M", 1, 1, Direction.UP, 0, 0), false, false, false, true);
        Player p0 = new Player(0, "name1");
        Player p1 = new Player(1, "name2");
        pw.addBuildLayer(t, p0, new Board(Arrays.asList(p0, p1)));
    }

    @Test(expected = InvalidActionTreeGenerationException.class)
    public void tryMoveAfterNotBuildAction()
    {
        Player p0 = new Player("Matteo", Color.BLUE, 0);
        Player p1 = new Player("Paolo", Color.WHITE, 1);
        Board board = new Board(Arrays.asList(p0, p1));
        Action a1 = new SetupAction(p0.getWorker(Sex.MALE).toString(), 1, 1);
        Action a2 = new SetupAction(p0.getWorker(Sex.FEMALE).toString(), 2, 2);
        Action a3 = new SetupAction(p1.getWorker(Sex.MALE).toString(), 1, 3);
        Action a4 = new SetupAction(p1.getWorker(Sex.FEMALE).toString(), 3, 1);
        board.executeAction(a1);
        board.executeAction(a2);
        board.executeAction(a3);
        board.executeAction(a4);

        PowerStrategy pw = new PrometheusPower();
        ActionTree t = new ActionTree(new MoveAction("P0-M", 1, 2, Direction.SAME, 1, 1), false, false, false, true);

        pw.addMoveLayer(t, p0, board);
    }
}
