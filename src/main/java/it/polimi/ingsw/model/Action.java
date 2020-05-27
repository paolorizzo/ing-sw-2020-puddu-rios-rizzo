package it.polimi.ingsw.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.lang.reflect.Method;

public class Action implements Serializable {
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
