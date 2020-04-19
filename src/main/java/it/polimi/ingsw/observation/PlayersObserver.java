package it.polimi.ingsw.observation;

public interface PlayersObserver {

    public void updateStart();
    public void updateID(int id);
    public void updateName(int id, String name);
    public void updateOk(int id);
    public void updateKo(int id);
}
