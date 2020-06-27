package it.polimi.ingsw.model;

/**
 * Enumerates the possible building levels that can be build on a Space,
 * including ground level
 */
public enum Piece {
    LEVEL0, LEVEL1, LEVEL2, LEVEL3, DOME;

    /**
     * returns an integer which identifies the level
     * @return an integer which identifies the level
     */
    public int getLevel(){
        return this.ordinal();
    }

    /**
     * returns the immediately superior level to this one
     * @return the immediately superior level to this one
     */
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
