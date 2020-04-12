package it.polimi.ingsw.observation;

public interface RequestsObserver {

    public void updateRequestID();
    public void updateRequestNumPlayers();
    public void updateAckID();
}
