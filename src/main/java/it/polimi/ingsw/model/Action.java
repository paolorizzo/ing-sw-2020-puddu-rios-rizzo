package it.polimi.ingsw.model;

public class Action {
    protected String workerID;
    protected int startX, startY, targetX, targetY;

    public Action(String workerID, int startX, int startY, int targetX, int targetY) {

        if(!(startX >= 0 && startX <= 4) || !(startY >= 0 && startY <= 4) || !(targetX >= 0 && targetX <= 4) || !(targetY >= 0 && targetY <= 4))
            throw new IllegalArgumentException();

        this.workerID = workerID;
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public String getWorkerID() {
        return workerID;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }
}
