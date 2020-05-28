package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * represents the action of moving a worker, and in doing so causing an opponent's worker to change its position too, thereby forcing it
 */
public class MoveAndForceAction extends MoveAction {
    private String forcedWorkerId;
    private int forcedStartX, forcedStartY, forcedTargetX, forcedTargetY;

    /**
     * constructs a complete MoveAndForceAction
     * @param workerID a string that univocally represents a worker
     * @param targetX the x coordinate of the space on which the worker moves
     * @param targetY the y coordinate of the space on which the worker moves
     * @param direction the direction of the movement. Can be either up, down, or on the same level
     * @param startX the x coordinate of the starting space of the worker
     * @param startY the y coordinate of the starting space of the worker
     * @param forcedWorkerId a string that univocally represents the opponent's forced worker
     * @param forcedStartX the x coordinate of the space on which the opponent's forced worker moves
     * @param forcedStartY the y coordinate of the space on which the opponent's forced worker moves
     * @param forcedTargetX the x coordinate of the starting space of the opponent's forced worker
     * @param forcedTargetY the y coordinate of the starting space of the opponent's forced worker
     */
    public MoveAndForceAction(String workerID, int targetX, int targetY, Direction direction, int startX, int startY, String forcedWorkerId, int forcedStartX, int forcedStartY, int forcedTargetX, int forcedTargetY) {
        super(workerID, targetX, targetY, direction, startX, startY);
        this.forcedWorkerId = forcedWorkerId;
        this.forcedStartX = forcedStartX;
        this.forcedStartY = forcedStartY;
        this.forcedTargetX = forcedTargetX;
        this.forcedTargetY = forcedTargetY;
    }

    /**
     * constructs a MoveAndForceACtion object by decorating a MoveAction
     * @param moveAction the MoveAction to decorate
     * @param forcedWorkerId a string that univocally represents the opponent's forced worker
     * @param forcedStartX the x coordinate of the space on which the opponent's forced worker moves
     * @param forcedStartY the y coordinate of the space on which the opponent's forced worker moves
     * @param forcedTargetX the x coordinate of the starting space of the opponent's forced worker
     * @param forcedTargetY the y coordinate of the starting space of the opponent's forced worker
     */
    public MoveAndForceAction(MoveAction moveAction, String forcedWorkerId, int forcedStartX, int forcedStartY, int forcedTargetX, int forcedTargetY){
        super(moveAction.workerID, moveAction.targetX, moveAction.targetY, moveAction.direction, moveAction.startX, moveAction.startY);
        this.forcedWorkerId = forcedWorkerId;
        this.forcedStartX = forcedStartX;
        this.forcedStartY = forcedStartY;
        this.forcedTargetX = forcedTargetX;
        this.forcedTargetY = forcedTargetY;

    }

    /**
     * returns a string that univocally represent's the opponent's forced worker
     * @return a string that univocally represent's the opponent's forced worker
     */
    public String getForcedWorkerId() {
        return forcedWorkerId;
    }

    /**
     * returns the starting x coordinate of the opponent's forced worker
     * @return the starting x coordinate of the opponent's forced worker
     */
    public int getForcedStartX() {
        return forcedStartX;
    }

    /**
     * returns the starting y coordinate of the opponent's forced worker
     * @return the starting y coordinate of the opponent's forced worker
     */
    public int getForcedStartY() {
        return forcedStartY;
    }

    /**
     * returns the final x coordinate of the opponent's forced worker
     * @return the final x coordinate of the opponent's forced worker
     */
    public int getForcedTargetX() {
        return forcedTargetX;
    }

    /**
     * returns the final y coordinate of the opponent's forced worker
     * @return the final y coordinate of the opponent's forced worker
     */
    public int getForcedTargetY() {
        return forcedTargetY;
    }

    /**
     * when saving this object to a map, puts the relevant info inside it
     * @param map the map in which to put the info
     */
    @Override
    public void putEntries(Map<String, Object> map){
        super.putEntries(map);
        map.put("forcedWorkerId", this.forcedWorkerId);
        map.put("forcedStartX", this.forcedStartX);
        map.put("forcedStartY", this.forcedStartY);
        map.put("forcedTargetX", this.forcedTargetX);
        map.put("forcedTargetY", this.forcedTargetY);
    }

    /**
     * this method will be called through a simulated overriding in Action
     * builds a MoveAndForceAction object from a map containing the necessary info, and the necessary type
     * @param map the map to convert into a MoveAndForceAction object
     * @return a MoveAndForceAction object as described in the map
     */
    static public MoveAndForceAction fromMap(Map<String, Object> map){
        checkTypeCorrectness(map, MoveAndForceAction.class);
        MoveAction moveAction = MoveAction.fromMap(map);
        String forcedWorkerId = (String) map.get("forcedWorkerId");
        int forcedStartX = getInt(map, "forcedStartX");
        int forcedStartY = getInt(map, "forcedStartY");
        int forcedTargetX = getInt(map, "forcedTargetX");
        int forcedTargetY = getInt(map, "forcedTargetY");
        return new MoveAndForceAction(moveAction, forcedWorkerId, forcedStartX, forcedStartY, forcedTargetX, forcedTargetY);
    }

    /**
     * returns a string that represents this action
     * @return a string that represents this action
     */
    @Override
    public String toString() {
        return "MoveAndForceAction{" +
                "forcedWorkerId='" + forcedWorkerId + '\'' +
                ", forcedStartX=" + forcedStartX +
                ", forcedStartY=" + forcedStartY +
                ", forcedTargetX=" + forcedTargetX +
                ", forcedTargetY=" + forcedTargetY +
                ", direction=" + direction +
                ", startX=" + startX +
                ", startY=" + startY +
                ", workerID='" + workerID + '\'' +
                ", targetX=" + targetX +
                ", targetY=" + targetY +
                '}';
    }

    /**
     * compares two actions and returns true if their attributes correspond
     * @param o the object against which to compare this one
     * @return true if theattribute of the compared object correspond to those of the current object
     */
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

}
