package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * represents the action of moving a worker, as performed by a player in their turn
 */
public class MoveAction extends Action{

    protected Direction direction;
    protected int startX, startY;

    /**
     * constructs a complete MoveAction
     * @param workerID a string that univocally represents a worker
     * @param targetX the x coordinate on the board of the space on which to move
     * @param targetY the y coordinate on the board of the space on which to move
     * @param direction the direction of the movement. Can be either up, down, or on the same level
     * @param startX  the x coordinate on the board of the space from which the worker moves
     * @param startY  the y coordinate on the board of the space from which the worker moves
     */
    public MoveAction(String workerID, int targetX, int targetY, Direction direction, int startX, int startY) {
        super(workerID, targetX, targetY);
        this.direction = direction;
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * constructs a MoveAction by decorating an Action
     * @param action the action to decorate
     * @param direction the direction of the movement
     * @param startX the starting x coordinate of the worker
     * @param startY the starting y coordinate of the worker
     */
    public MoveAction(Action action, Direction direction, int startX, int startY){
        super(action.workerID, action.targetX, action.targetY);
        this.direction = direction;
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * returns the direction of the movement of the worker
     * @return the direction of the movement of the worker
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * returns the starting x coordinate of the worker
     * @return the starting x coordinate of the worker
     */
    public int getStartX() {
        return startX;
    }

    /**
     * returns the starting y coordinate of the worker
     * @return the starting y coordinate of the worker
     */
    public int getStartY() {
        return startY;
    }

    /**
     * when saving this object to a map, puts the relevant info inside it
     * @param map the map in which to put the info
     */
    @Override
    public void putEntries(Map<String, Object> map){
        super.putEntries(map);
        map.put("direction", this.direction.ordinal());
        map.put("startX", this.startX);
        map.put("startY", this.startY);
    }

    /**
     * this method will be called through a simulated overriding in Action
     * builds a MoveAction object from a map containing the necessary info, and the necessary type
     * @param map the map to convert into a MoveAction object
     * @return a MoveAction object as described in the map
     */
    static public MoveAction fromMap(Map<String, Object> map){
        checkTypeCorrectness(map, MoveAction.class);
        Action action = Action.parseMap(map);
        int directionNum = getInt(map, "direction");
        Direction direction = Direction.values()[directionNum];
        int startX = getInt(map, "startX");
        int startY = getInt(map, "startY");
        return new MoveAction(action, direction, startX, startY);
    }

    /**
     * returns a string that represents this action
     * @return a string that represents this action
     */
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

    /**
     * compares two actions and returns true if their attributes correspond
     * @param o the object against which to compare this one
     * @return true if theattribute of the compared object correspond to those of the current object
     */
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

}
