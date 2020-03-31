package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PieceBagTest
{

    @Test
    public void checkCantGetInfinitePieces(){
        PieceBag bag = new PieceBag();
        while(bag.hasPiece(Piece.LEVEL2)) {
            bag.getPiece(Piece.LEVEL2);
        }
        assertEquals(bag.getCount(Piece.LEVEL2), 0);
        assert(!bag.hasPiece(Piece.LEVEL2));
    }

}
