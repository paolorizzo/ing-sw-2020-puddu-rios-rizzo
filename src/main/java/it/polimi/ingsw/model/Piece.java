package it.polimi.ingsw.model;

public enum Piece {
    LEVEL0, LEVEL1, LEVEL2, LEVEL3, DOME;

    public int getLevel(){
        return this.ordinal();
    }
}
