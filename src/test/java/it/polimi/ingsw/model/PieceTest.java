package it.polimi.ingsw.model;

import org.junit.Test;

public class PieceTest {
    @Test(expected = IllegalArgumentException.class)
    public void tryNextPieceOfDOME()
    {
        Piece p = Piece.DOME;
        p = p.nextPiece();
    }
}
