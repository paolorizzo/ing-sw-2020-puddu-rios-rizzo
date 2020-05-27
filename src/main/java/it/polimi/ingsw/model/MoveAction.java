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
        return map;
    }

    /**
     * this method will be called through a simulated overriding in Action
     * builds a MoveAction object from a map containing the necessary info, and the necessary type
     * @param map the map to convert into a MoveAction object
     * @return a MoveAction object as described in the map
     */
    static public MoveAction fromMap(Map<String, Object> map){
        Class mapClass = (Class) map.get("class");
        if(!MoveAction.class.equals(mapClass))
            throw new IllegalArgumentException("Tried to build an object of type MoveAction from a map of type " + mapClass.toString());
        String workerId = (String) map.get("workerId");
        int targetX = (int) map.get("targetX");
        int targetY = (int) map.get("targetY");
        int directionNum = (int) map.get("direction");
        Direction direction = Direction.values()[directionNum];
        int startX = (int) map.get("startX");
        int startY = (int) map.get("startY");
        return new MoveAction(workerId, targetX, targetY, direction, startX, startY);
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

}
