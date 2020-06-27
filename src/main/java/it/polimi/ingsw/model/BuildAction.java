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
     * constructs a complete BuildAction
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
     * constructs a BuildAction by decorating an Action
     * @param action the action to decorate
     * @param piece the type of building that is being built
     */
    public BuildAction(Action action, Piece piece){
        super(action.workerID, action.targetX, action.targetY);
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
     * when saving this object to a map, puts the relevant info inside it
     * @param map the map in which to put the info
     */
    @Override
    public void putEntries(Map<String, Object> map){
        super.putEntries(map);
        map.put("piece", this.piece.ordinal());
    }

    /**
     * Will be called through simulated override
     * converts a map back to a BuildAction object
     * @param map the map that will be used to convert
     * @return a BuildAction object as described in the map
     */
    static public BuildAction fromMap(Map<String, Object> map){
        checkTypeCorrectness(map, BuildAction.class);
        Action action = Action.parseMap(map);
        int pieceNum = getInt(map, "piece");
        Piece piece = Piece.values()[pieceNum];
        return new BuildAction(action, piece);
    }

    /**
     * returns a string that represents this action
     * @return a string that represents this action
     */
    @Override
    public String toString() {
        return "BuildAction{" +
                "piece=" + piece +
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
        if (!(o instanceof BuildAction)) return false;
        if (!super.equals(o)) return false;
        BuildAction that = (BuildAction) o;
        return piece == that.piece;
    }

    /**
     * checks if this action is performed by the given worker, and uses
     * the given piece
     * @param workerID the id of the worker object of the query
     * @param piece the piece to use for the query
     * @return true if this action matches the arguments of the call
     */
    public boolean matches(String workerID, Piece piece){
        if(!this.workerID.equals(workerID))
            return false;
        if(this.piece != piece)
            return false;
        return true;
    }

    /**
     * Checks if this action is performed by the given worker, is
     * contextual to the given piece, and has as its actual target
     * the space identified by the arguments targetX and targetY
     * @param workerID workerID the id of the worker object of the query
     * @param targetX the x coordinate of the target space
     * @param targetY the y coordinate of the target space
     * @param piece the piece to use for the query
     * @return true if this action matches the arguments
     */
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
