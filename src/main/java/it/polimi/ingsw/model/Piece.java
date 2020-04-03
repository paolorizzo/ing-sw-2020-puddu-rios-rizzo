package it.polimi.ingsw.model;

public enum Piece {
    LEVEL0, LEVEL1, LEVEL2, LEVEL3, DOME;

    public int getLevel(){
        return this.ordinal();
    }
    public Piece nextPiece(){
        switch(this){
            case LEVEL0: return LEVEL1;
            case LEVEL1: return LEVEL2;
            case LEVEL2: return LEVEL3;
            case LEVEL3: return DOME;
            default: throw new IllegalArgumentException("DOME is the last piece.");
        }
    }
}
