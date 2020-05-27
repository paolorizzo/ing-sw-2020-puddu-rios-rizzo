package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;
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
