package it.polimi.ingsw.model;

import java.util.Objects;

public class MoveAction extends Action{

    protected Direction direction;
    protected int startX, startY;

    public MoveAction(String workerID, int targetX, int targetY, Direction direction, int startX, int startY) {
        super(workerID, targetX, targetY);
        this.direction = direction;
        this.startX = startX;
        this.startY = startY;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    @Override
    public String toString() {
        return "MoveAction{" +
                "direction=" + direction +
                ", startX=" + startX +
                ", startY=" + startY +
                ", workerID='" + workerID + '\'' +
                ", targetX=" + targetX +
                ", targetY=" + targetY +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveAction)) return false;
        if (!super.equals(o)) return false;
        MoveAction that = (MoveAction) o;
        return startX == that.startX &&
                startY == that.startY &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), direction, startX, startY);
    }
}
