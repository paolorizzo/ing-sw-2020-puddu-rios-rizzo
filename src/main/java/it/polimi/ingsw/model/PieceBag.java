package it.polimi.ingsw.model;

import java.util.Arrays;

public class PieceBag {
    int countLevel[] = new int[5];

    public PieceBag()
    {
        countLevel[Piece.LEVEL0.getLevel()] = 0;
        countLevel[Piece.LEVEL1.getLevel()] = 22;
        countLevel[Piece.LEVEL2.getLevel()] = 18;
        countLevel[Piece.LEVEL3.getLevel()] = 14;
        countLevel[Piece.DOME.getLevel()] = 18;
    }

    int getCount(Piece p)
    {
        return countLevel[p.getLevel()];
    }

    public boolean hasPiece(Piece p)
    {
        return getCount(p) > 0;
    }

    public void pickPiece(Piece p)
    {
        if(getCount(p) == 0)
            throw new IllegalArgumentException();
        else
            countLevel[p.getLevel()]--;
    }

    public void undoPickPiece(Piece p)
    {
        countLevel[p.getLevel()]++;
    }

    /**
     * returns true if the count of levels match
     * @param o the other object
     * @return true if the other object is a piecebag and the level counts match
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof PieceBag)) return false;
        PieceBag that = (PieceBag) o;
        boolean equality = true;
        for(int i=0;i<5;i++)
            equality &= (this.countLevel[i] == that.countLevel[i]);

        return equality;
    }
}
