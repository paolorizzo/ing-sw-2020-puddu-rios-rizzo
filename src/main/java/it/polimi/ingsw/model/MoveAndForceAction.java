package it.polimi.ingsw.model;

import java.util.Objects;

public class MoveAndForceAction extends MoveAction {
    private String forcedWorkerId;
    private int forcedStartX, forcedStartY, forcedTargetX, forcedTargetY;

    public MoveAndForceAction(String workerID, int targetX, int targetY, Direction direction, int startX, int startY, String forcedWorkerId, int forcedStartX, int forcedStartY, int forcedTargetX, int forcedTargetY) {
        super(workerID, targetX, targetY, direction, startX, startY);
        this.forcedWorkerId = forcedWorkerId;
        this.forcedStartX = forcedStartX;
        this.forcedStartY = forcedStartY;
        this.forcedTargetX = forcedTargetX;
        this.forcedTargetY = forcedTargetY;
    }

    public String getForcedWorkerId() {
        return forcedWorkerId;
    }

    public int getForcedStartX() {
        return forcedStartX;
    }

    public int getForcedStartY() {
        return forcedStartY;
    }

    public int getForcedTargetX() {
        return forcedTargetX;
    }

    public int getForcedTargetY() {
        return forcedTargetY;
    }

    @Override
    public String toString() {
        return "MoveAndForceAction{" +
                "forcedWorkerId='" + forcedWorkerId + '\'' +
                ", forcedStartX=" + forcedStartX +
                ", forcedStartY=" + forcedStartY +
                ", forcedTargetX=" + forcedTargetX +
                ", forcedTargetY=" + forcedTargetY +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveAndForceAction)) return false;
        if (!super.equals(o)) return false;
        MoveAndForceAction that = (MoveAndForceAction) o;
        return forcedStartX == that.forcedStartX &&
                forcedStartY == that.forcedStartY &&
                forcedTargetX == that.forcedTargetX &&
                forcedTargetY == that.forcedTargetY &&
                forcedWorkerId.equals(that.forcedWorkerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), forcedWorkerId, forcedStartX, forcedStartY, forcedTargetX, forcedTargetY);
    }
}
