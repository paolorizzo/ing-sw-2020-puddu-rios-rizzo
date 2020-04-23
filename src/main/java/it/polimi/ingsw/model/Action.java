package it.polimi.ingsw.model;

import java.util.Objects;

public class Action {
    protected String workerID;
    protected int targetX, targetY;

    public Action(String workerID, int targetX, int targetY) {

        if(!(targetX >= 0 && targetX <= 4) || !(targetY >= 0 && targetY <= 4))
            throw new IllegalArgumentException();

        this.workerID = workerID;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public String getWorkerID() {
        return workerID;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    @Override
    public String toString() {
        return "Action{" +
                "workerID='" + workerID + '\'' +
                ", targetX=" + targetX +
                ", targetY=" + targetY +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return targetX == action.targetX &&
                targetY == action.targetY &&
                workerID.equals(action.workerID);
    }

    public boolean matches(String workerID){
        return this.workerID.equals(workerID);
    }
    public boolean matches(String workerID, int targetX, int targetY){
        if(!this.workerID.equals(workerID))
            return false;
        if(this.targetX != targetX)
            return false;
        if(this.targetY != targetY)
            return false;
        return true;
    }
    public boolean matches(String workerID, Piece piece){
        return false;
    }
    public boolean matches(String workerID, int targetX, int targetY, Piece piece){
        return false;
    }

}
