package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;

public interface UserInterfaceObserver {
    public void updateReadNumPlayers(int numPlayers);
    public void updateReadName(String name);
    public void updateReadNumCard(int numCard);
    public void updateReadGod(int numCard);
    public void updateReadAction(Action action);
}
