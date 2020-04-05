package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.Arrays;

public class BoardTest {
    @Test
    public void testConstructor(){
        Player p1 = new Player("Paolo", Color.BLUE, 1);
        Player p2 = new Player("Francesco", Color.WHITE, 2);
        Board board = new Board(Arrays.asList(p1, p2));

        Space[][] spaces = board.getSpaces();

        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                for(int i2=0;i2<5;i2++){
                    for(int j2=0;j2<5;j2++){
                        if(Math.abs(i-i2)<=1 && Math.abs(j-j2)<=1 && (i!=i2 || j!=j2))
                            assert(spaces[i][j].getAdjacentSpaces().contains(spaces[i2][j2]));
                    }
                }
            }
        }
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                for(Space adj : spaces[i][j].getAdjacentSpaces()) {
                    assert(Math.abs(i-adj.getPosX())<=1 && Math.abs(j-adj.getPosY())<=1 && (i!=adj.getPosX() || j!=adj.getPosY()));
                }
            }
        }
    }
    @Test
    public void testExecuteAndUndoAction(){
        Player p1 = new Player("Paolo", Color.BLUE, 1);
        Player p2 = new Player("Federico", Color.WHITE, 2);
        Board board = new Board(Arrays.asList(p1, p2));
        Action a1 = new SetupAction(p1.getWorker(Sex.MALE).toString(), 1, 3);
        Action a2 = new SetupAction(p1.getWorker(Sex.FEMALE).toString(), 3, 3);
        Action a3 = new SetupAction(p2.getWorker(Sex.MALE).toString(), 0, 4);
        Action a4 = new SetupAction(p2.getWorker(Sex.FEMALE).toString(), 2, 2);
        board.executeAction(a1);
        board.executeAction(a2);
        board.executeAction(a3);
        board.executeAction(a4);

        assert(board.getSpaces()[1][3].getWorkerOnIt() == p1.getWorker(Sex.MALE));
        assert(board.getSpaces()[3][3].getWorkerOnIt() == p1.getWorker(Sex.FEMALE));
        assert(board.getSpaces()[0][4].getWorkerOnIt() == p2.getWorker(Sex.MALE));
        assert(board.getSpaces()[2][2].getWorkerOnIt() == p2.getWorker(Sex.FEMALE));
        assert(p1.getWorker(Sex.MALE).getSpace() == board.getSpaces()[1][3]);
        assert(p1.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[3][3]);
        assert(p2.getWorker(Sex.MALE).getSpace() == board.getSpaces()[0][4]);
        assert(p2.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[2][2]);

        board.undoExecuteAction(a3);

        assert(board.getSpaces()[1][3].getWorkerOnIt() == p1.getWorker(Sex.MALE));
        assert(board.getSpaces()[3][3].getWorkerOnIt() == p1.getWorker(Sex.FEMALE));
        assert(board.getSpaces()[0][4].getWorkerOnIt() == null);
        assert(board.getSpaces()[2][2].getWorkerOnIt() == p2.getWorker(Sex.FEMALE));
        assert(p1.getWorker(Sex.MALE).getSpace() == board.getSpaces()[1][3]);
        assert(p1.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[3][3]);
        assert(p2.getWorker(Sex.MALE).getSpace() == null);
        assert(p2.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[2][2]);


        board.executeAction(a3);

        assert(board.getSpaces()[1][3].getWorkerOnIt() == p1.getWorker(Sex.MALE));
        assert(board.getSpaces()[3][3].getWorkerOnIt() == p1.getWorker(Sex.FEMALE));
        assert(board.getSpaces()[0][4].getWorkerOnIt() == p2.getWorker(Sex.MALE));
        assert(board.getSpaces()[2][2].getWorkerOnIt() == p2.getWorker(Sex.FEMALE));
        assert(p1.getWorker(Sex.MALE).getSpace() == board.getSpaces()[1][3]);
        assert(p1.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[3][3]);
        assert(p2.getWorker(Sex.MALE).getSpace() == board.getSpaces()[0][4]);
        assert(p2.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[2][2]);

        Action m1 = new MoveAction("P1-M", 2, 3, Direction.SAME, 1, 3);
        board.executeAction(m1);
        assert(board.getSpaces()[2][3].getWorkerOnIt() == p1.getWorker(Sex.MALE));
        assert(p1.getWorker(Sex.MALE).getSpace() == board.getSpaces()[2][3]);
        assert(board.getSpaces()[1][3].getWorkerOnIt() == null);

        board.undoExecuteAction(m1);

        assert(board.getSpaces()[2][3].getWorkerOnIt() == null);
        assert(board.getSpaces()[1][3].getWorkerOnIt() == p1.getWorker(Sex.MALE));
        assert(board.getSpaces()[3][3].getWorkerOnIt() == p1.getWorker(Sex.FEMALE));
        assert(board.getSpaces()[0][4].getWorkerOnIt() == p2.getWorker(Sex.MALE));
        assert(board.getSpaces()[2][2].getWorkerOnIt() == p2.getWorker(Sex.FEMALE));
        assert(p1.getWorker(Sex.MALE).getSpace() == board.getSpaces()[1][3]);
        assert(p1.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[3][3]);
        assert(p2.getWorker(Sex.MALE).getSpace() == board.getSpaces()[0][4]);
        assert(p2.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[2][2]);

        Action b1 = new BuildAction("P1-F", 4, 4, Piece.LEVEL1);
        board.executeAction(b1);
        assert(board.getPieceBag().getCount(Piece.LEVEL1) == 21);
        assert(board.getSpaces()[4][4].getLevel() == 1);

        board.undoExecuteAction(b1);

        assert(board.getPieceBag().getCount(Piece.LEVEL1) == 22);
        assert(board.getSpaces()[4][4].getLevel() == 0);

        Action maf1 = new MoveAndForceAction("P2-F", 1, 3, Direction.SAME, 2, 2, "P1-M", 1, 3, 2, 2);

        board.executeAction(maf1);

        assert(board.getSpaces()[2][2].getWorkerOnIt() == p1.getWorker(Sex.MALE));
        assert(board.getSpaces()[3][3].getWorkerOnIt() == p1.getWorker(Sex.FEMALE));
        assert(board.getSpaces()[0][4].getWorkerOnIt() == p2.getWorker(Sex.MALE));
        assert(board.getSpaces()[1][3].getWorkerOnIt() == p2.getWorker(Sex.FEMALE));
        assert(p1.getWorker(Sex.MALE).getSpace() == board.getSpaces()[2][2]);
        assert(p1.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[3][3]);
        assert(p2.getWorker(Sex.MALE).getSpace() == board.getSpaces()[0][4]);
        assert(p2.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[1][3]);

        board.undoExecuteAction(maf1);

        assert(board.getSpaces()[1][3].getWorkerOnIt() == p1.getWorker(Sex.MALE));
        assert(board.getSpaces()[3][3].getWorkerOnIt() == p1.getWorker(Sex.FEMALE));
        assert(board.getSpaces()[0][4].getWorkerOnIt() == p2.getWorker(Sex.MALE));
        assert(board.getSpaces()[2][2].getWorkerOnIt() == p2.getWorker(Sex.FEMALE));
        assert(p1.getWorker(Sex.MALE).getSpace() == board.getSpaces()[1][3]);
        assert(p1.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[3][3]);
        assert(p2.getWorker(Sex.MALE).getSpace() == board.getSpaces()[0][4]);
        assert(p2.getWorker(Sex.FEMALE).getSpace() == board.getSpaces()[2][2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionExecuteAction()
    {

        Player p1 = new Player("Paolo", Color.BLUE, 1);
        Player p2 = new Player("Federico", Color.WHITE, 2);
        Board board = new Board(Arrays.asList(p1, p2));

        Action a = new Action("P1-M", 1, 1);
        board.executeAction(a);
    }
    @Test(expected = IllegalArgumentException.class)
    public void exceptionUndoExecuteAction()
    {

        Player p1 = new Player("Paolo", Color.BLUE, 1);
        Player p2 = new Player("Federico", Color.WHITE, 2);
        Board board = new Board(Arrays.asList(p1, p2));

        Action a = new Action("P1-M", 1, 1);
        board.undoExecuteAction(a);
    }

}
