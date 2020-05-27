package it.polimi.ingsw.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.lang.reflect.Method;

/**
 * represents a generalization of the actions that a worker can perform
 */
public class Action implements Serializable {
    protected String workerID;
    protected int targetX, targetY;

    /**
     * constructs a basic action
     * @param workerID the id of the worker that performs the action. Contains info about the id of the player that owns it
     * @param targetX the x coordinate on the board of the designated action
     * @param targetY the y coordinate on the board of the designated action
     */
    public Action(String workerID, int targetX, int targetY) {

        if(!(targetX >= 0 && targetX <= 4) || !(targetY >= 0 && targetY <= 4))
            throw new IllegalArgumentException();

        this.workerID = workerID;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /**
     * returns a string that represents the worker
     * @return a string that represents the worker
     */
    public String getWorkerID() {
        return workerID;
    }

    /**
     * returns the x coordinate of the target of the action
     * @return the x coordinate of the target of the action
     */
    public int getTargetX() {
        return targetX;
    }

    /**
     * returns the y coordinate of the target of the action
     * @return the y coordinate of the target of the action
     */
    public int getTargetY() {
        return targetY;
    }

    /**
     * returns a map that represents an Action Object, embedding the information about the dynamic type in the map
     * @return a map representing the Action object
     */
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("class", this.getClass());
        map.put("workerId", this.workerID);
        map.put("targetX", this.targetX);
        map.put("targetY", this.targetY);
        return map;
    }

    /**
     * converts a map to an Action object, if possible
     * supports dynamic types by reading the related information from the map
     * and calling the adequate dynamic version of the fromMap method
     * In practice, simulates overriding this method, because java
     * doesn't natively allow overriding static methods
     * @param map the map to convert to an Action object
     * @return the appropriate subclassed action converted from the map
     */
    static public Action fromMap(Map<String, Object> map){
        Class mapClass = (Class) map.get("class");
        if(!Action.class.equals(mapClass)){
            Method[] possibleMethods = mapClass.getMethods();
            for(Method method:possibleMethods){
                if(method.getName().equals("fromMap")){
                    try{
                        return (Action) method.invoke(null, map);
                    }
                    catch(IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            throw new IllegalArgumentException("Class " + mapClass.toString() + " does not have a fromMap method");
        }
        else{
            String workerId = (String) map.get("workerId");
            int targetX = (int) map.get("targetX");
            int targetY = (int) map.get("targetY");
            return new Action(workerId, targetX, targetY);
        }
    }

    /**
     * returns a string that represents this action
     * @return a string that represents this action
     */
    @Override
    public String toString() {
        return "Action{" +
                "workerID='" + workerID + '\'' +
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
