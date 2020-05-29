package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PieceBagTest
{

    @Test
    public void checkCantGetInfinitePieces(){
        PieceBag bag = new PieceBag();
        while(bag.hasPiece(Piece.LEVEL2)) {
            bag.pickPiece(Piece.LEVEL2);
        }
        assertEquals(bag.getCount(Piece.LEVEL2), 0);
        assert(!bag.hasPiece(Piece.LEVEL2));
    }
    @Test(expected = IllegalArgumentException.class)
    public void tryToPickTooPiece()
    {
        PieceBag bag = new PieceBag();
        while(true){
            bag.pickPiece(Piece.LEVEL2);
        }
    }

    /**
     * tests that the equals method works as intended
     */
    @Test
    public void testEquals(){
        PieceBag pb1 = new PieceBag();
        PieceBag pb2 = new PieceBag();
        Piece p = Piece.LEVEL1;
        assert pb1.equals(pb2);
        assertEquals(pb1, pb2);
        assertNotEquals(pb1, p);
        pb1.pickPiece(p);
        assertNotEquals(pb1, pb2);
    }
}
