package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PieceBagTest
{

    @Test
    public void EndOfPiece(){
        PieceBag bag = new PieceBag();
        while(bag.hasPiece(Piece.LEVEL2)) {
            bag.getPiece(Piece.LEVEL2);
        }
        assertEquals(bag.getCount(Piece.LEVEL2), 0);
        assert(!bag.hasPiece(Piece.LEVEL2));
        assertEquals(bag.getCount(Piece.LEVEL0), 0);
        assertEquals(bag.getCount(Piece.LEVEL1), 22);
        assertEquals(bag.getCount(Piece.LEVEL3), 14);
        assertEquals(bag.getCount(Piece.DOME), 18);
    }

}
