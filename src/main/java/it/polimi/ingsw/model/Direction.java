package it.polimi.ingsw.model;

public enum Direction {
    DOWN, SAME, UP;

    static Direction compareTwoLevels(int start, int end){
        if(start == end)
            return SAME;
        else if(start<end)
            return UP;
        else
            return DOWN;
    }
}
