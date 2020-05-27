package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * represents the action of building any type of building on a space
 */
public class BuildAction extends Action{
    private Piece piece;

    /**
     * contructs a complete BuildAction
     * @param workerID a string that univocally represents a worker
     * @param targetX the x coordinate on the board of the space on which the building is erected
     * @param targetY the y coordinate on the board of the space on which the building is erected
     * @param piece the type of building that is being erected
     */
    public BuildAction(String workerID, int targetX, int targetY, Piece piece) {
        super(workerID, targetX, targetY);
        this.piece = piece;
    }

    /**
     * returns the type of building that is being erected
     * @return the type of building that is being erected
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * converts a BuildAction object to a map, embedding dynamic type information
     * @return a map representing the object
     */
    @Override
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("class", this.getClass());
        map.put("piece", this.piece.ordinal());
        map.put("workerId", this.workerID);
        map.put("targetX", this.targetX);
        map.put("targetY", this.targetY);
        return map;
    }

    /**
     * Will be called through simulated override
     * converts a map back to a BuildAction object
     * @param map the map that will be used to convert
     * @return a BuildAction object as described in the map
     */
    static public BuildAction fromMap(Map<String, Object> map){
        Class mapClass = (Class) map.get("class");
        if(!BuildAction.class.equals(mapClass))
            throw new IllegalArgumentException("Tried to build an object of type BuildAction from a map of type " + mapClass.toString());
        String workerId = (String) map.get("workerId");
        int targetX = (int) map.get("targetX");
        int targetY = (int) map.get("targetY");
        int pieceNum = (int) map.get("piece");
        Piece piece = Piece.values()[pieceNum];
        return new BuildAction(workerId, targetX, targetY, piece);
    }

    @Override
    public String toString() {
        return "BuildAction{" +
                "piece=" + piece +
                ", workerID='" + workerID + '\'' +
                ", targetX=" + targetX +
                ", targetY=" + targetY +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuildAction)) return false;
        if (!super.equals(o)) return false;
        BuildAction that = (BuildAction) o;
        return piece == that.piece;
    }
    public boolean matches(String workerID, Piece piece){
        if(!this.workerID.equals(workerID))
            return false;
        if(this.piece != piece)
            return false;
        return true;
    }
    public boolean matches(String workerID, int targetX, int targetY, Piece piece){
        if(!this.workerID.equals(workerID))
            return false;
        if(this.targetX != targetX)
            return false;
        if(this.targetY != targetY)
            return false;
        if(this.piece != piece)
            return false;
        return true;
    }
}
