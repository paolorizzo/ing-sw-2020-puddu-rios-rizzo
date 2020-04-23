package it.polimi.ingsw.model;

import java.util.Objects;

public class BuildAction extends Action{
    private Piece piece;

    public BuildAction(String workerID, int targetX, int targetY, Piece piece) {
        super(workerID, targetX, targetY);
        this.piece = piece;
    }
    public Piece getPiece() {
        return piece;
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
