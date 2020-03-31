package it.polimi.ingsw.model;

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

}
