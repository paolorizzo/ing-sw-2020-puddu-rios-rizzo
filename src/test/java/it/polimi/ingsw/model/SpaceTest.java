package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpaceTest {

    @Test(expected = IllegalArgumentException.class)
    public void checkBuildOrder()
    {
        Space s = new Space(1,1);
        s.addPiece(Piece.LEVEL2);
        s.addPiece(Piece.LEVEL1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkDoubleBuild()
    {
        Space s = new Space(1,1);
        s.addPiece(Piece.LEVEL2);
        s.addPiece(Piece.LEVEL2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryToRemoveLevel0()
    {
        Space s = new Space(1,1);
        s.removeLastPiece();
    }

    @Test
    public void checkConstructor(){
        int x = 2;
        int y = 3;
        Space space = new Space(2, 3);
        assertEquals(space.getPosX(), x);
        assertEquals(space.getPosY(), y);
        assertEquals(space.getAdjacentSpaces().size(), 0);
        assertEquals(space.getLevel(), 0);
        assert(space.isFreeSpace());
    }

    @Test
    public void checkPeripheral()
    {
        Space space1 = new Space(0, 3);
        Space space2 = new Space(4, 3);
        Space space3 = new Space(2, 0);
        Space space4 = new Space(2, 4);

        assert(space1.isPeripheralSpace());
        assert(space2.isPeripheralSpace());
        assert(space3.isPeripheralSpace());
        assert(space4.isPeripheralSpace());
    }

    /**
     * tests that the toString methods works as expected
     */
    @Test
    public void checkToString(){
        Space s = new Space(0, 0);
        assertEquals("    ", s.toString());
        Player p = new Player("pippo", Color.BLUE, 0);
        Worker w = new Worker(Sex.FEMALE, p);
        s.setWorkerOnIt(w);
        assertEquals("P0-F", s.toString());
    }

    /**
     * tests that the fullEquals method works as intended
     */
    @Test
    public void checkFullEquals(){
        Controller c1 = new Controller();
        ControllerTest ct1 = new ControllerTest();
        ct1.playSomeTurns(c1);
        Controller c2 = new Controller();
        ControllerTest ct2 = new ControllerTest();
        ct2.playSomeTurns(c2);
        Space[][] s1 = c1.getModel().board.getSpaces();
        Space[][] s2 = c2.getModel().board.getSpaces();
        for(int i=0; i<5; i++){
            for(int j=0;j<5;j++){
                assert( s1[i][j].fullEquals(s2[i][j]));
            }
        }
        assert(! s1[0][1].equals(s2[1][0]));
    }

}
