package it.polimi.ingsw.controller;

public interface ControllerInterface {

    //connection phase
    public void generateId();
    public void ackId(int id);
    public void setNumPlayers(int id, int numPlayers);
    public void getNumPlayers();
    public void requestAllPlayersConnected();
    public void setName(int id, String name);
    public void deleteId(int id);
    public void kill();

}
