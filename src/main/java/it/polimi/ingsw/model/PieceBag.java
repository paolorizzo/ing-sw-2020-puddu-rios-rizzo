package it.polimi.ingsw.model;

import java.util.Arrays;

public class PieceBag {
    int countLevel[] = new int[5];

    /**
     * constructs a piece bag with the right number of pieces
     * to start the game
     */
    public PieceBag()
    {
        countLevel[Piece.LEVEL0.getLevel()] = 0;
        countLevel[Piece.LEVEL1.getLevel()] = 22;
        countLevel[Piece.LEVEL2.getLevel()] = 18;
        countLevel[Piece.LEVEL3.getLevel()] = 14;
        countLevel[Piece.DOME.getLevel()] = 18;
    }

    /**
     * gets the count of remaining pieces of a given type
     * @param p the given type of piece
     * @return the count of remaining pieces of the given type
     */
    public int getCount(Piece p)
    {
        return countLevel[p.getLevel()];
    }

    /**
     * checks whether there are remaining pieces of a given type
     * @param p the given type of piece
     * @return true if there remains at least one piece of the given type
     */
    public boolean hasPiece(Piece p)
    {
        return getCount(p) > 0;
    }

    /**
     * picks a piece of the given type from the bag
     * @param p the given type of piece
     */
    public void pickPiece(Piece p)
    {
        if(getCount(p) == 0)
            throw new IllegalArgumentException();
        else
            countLevel[p.getLevel()]--;
    }

    /**
     * puts a piece of the given type back into the bag
     * @param p the type of piece
     */
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
