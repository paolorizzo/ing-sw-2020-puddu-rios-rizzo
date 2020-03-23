package it.polimi.ingsw.model;

public class PieceBag {
    int countLevel[] = new int[5];

    public PieceBag(){
        countLevel[Piece.LEVEL0.ordinal()] = 0;
        countLevel[Piece.LEVEL1.ordinal()] = 22;
        countLevel[Piece.LEVEL2.ordinal()] = 18;
        countLevel[Piece.LEVEL3.ordinal()] = 14;
        countLevel[Piece.DOME.ordinal()] = 18;
    }

    int getCount(Piece p){
        return countLevel[p.ordinal()];
    }
    boolean hasPiece(Piece p){
        return getCount(p) > 0;
    }
    void getPiece(Piece p){
        if(getCount(p) == 0)
            throw new IllegalArgumentException();
        countLevel[p.ordinal()]--;
    }
}
