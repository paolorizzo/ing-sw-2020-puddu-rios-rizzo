package it.polimi.ingsw.model;

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

    void pickPiece(Piece p)
    {
        if(getCount(p) == 0)
            throw new IllegalArgumentException();
        else
            countLevel[p.getLevel()]--;
    }

    void undoPickPiece(Piece p)
    {
        countLevel[p.getLevel()]++;
    }
}
