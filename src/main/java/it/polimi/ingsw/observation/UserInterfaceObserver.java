package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;

/**
 * Observer interface for the communication from the User Interfaces to the View in the client
 */
public interface UserInterfaceObserver
{
    void updateReadIp(String ip);
    void updateReadPort(int port);
    void updateReadNumPlayers(int numPlayers);
    void updateReadName(String name);
    void updateReadRestore(boolean restore);
    void updateReadNumCard(int numCard);
    void updateReadGod(int numCard);
    void updateReadAction(Action action);
    void updateReadVoluntaryEndOfTurn();
}
