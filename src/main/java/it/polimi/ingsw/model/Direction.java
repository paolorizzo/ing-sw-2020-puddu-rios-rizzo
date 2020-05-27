package it.polimi.ingsw.model;

/**
 * represents the directions in which a worker can move
 */
public enum Direction {
    DOWN, SAME, UP;

    /**
     * discerns the direction of the movement, given the levels of two spaces
     * @param start the level of the starting space
     * @param end the level of the space on which the movement ends
     * @return the corresponding direction
     */
    public static Direction compareTwoLevels(int start, int end){
        if(start == end)
            return SAME;
        else if(start<end)
            return UP;
        else
            return DOWN;
    }
}
