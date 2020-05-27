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
     * converts a MoveAction object to a map, while also embedding in it information about
     * the dynamic type
     * @return a map representing the object
     */
    @Override
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("class", this.getClass());
        map.put("workerId", this.workerID);
        map.put("targetX", this.targetX);
        map.put("targetY", this.targetY);
        map.put("direction", this.direction.ordinal());
        map.put("startX", this.startX);
        map.put("startY", this.startY);
        map.put("forcedWorkerId", this.forcedWorkerId);
        map.put("forcedStartX", this.forcedStartX);
        map.put("forcedStartY", this.forcedStartY);
        map.put("forcedTargetX", this.forcedTargetX);
        map.put("forcedTargetY", this.forcedTargetY);
        return map;
    }

    /**
     * this method will be called through a simulated overriding in Action
     * builds a MoveAndForceAction object from a map containing the necessary info, and the necessary type
     * @param map the map to convert into a MoveAndForceAction object
     * @return a MoveAndForceAction object as described in the map
     */
    static public MoveAndForceAction fromMap(Map<String, Object> map){
        Class mapClass = (Class) map.get("class");
        if(!MoveAndForceAction.class.equals(mapClass))
            throw new IllegalArgumentException("Tried to build an object of type MoveAndForceAction from a map of type " + mapClass.toString());
        String workerId = (String) map.get("workerId");
        int targetX = (int) map.get("targetX");
        int targetY = (int) map.get("targetY");
        int directionNum = (int) map.get("direction");
        Direction direction = Direction.values()[directionNum];
        int startX = (int) map.get("startX");
        int startY = (int) map.get("startY");
        String forcedWorkerId = (String) map.get("forcedWorkerId");
        int forcedStartX = (int) map.get("forcedStartX");
        int forcedStartY = (int) map.get("forcedStartY");
        int forcedTargetX = (int) map.get("forcedTargetX");
        int forcedTargetY = (int) map.get("forcedTargetY");
        return new MoveAndForceAction(workerId, targetX, targetY, direction, startX, startY, forcedWorkerId, forcedStartX, forcedStartY, forcedTargetX, forcedTargetY);
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
