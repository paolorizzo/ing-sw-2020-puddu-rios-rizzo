package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;

/**
 * Observer interface for the communication from the User Interfaces to the View in the client
 */
public interface UserInterfaceObserver
{
    /**
     * uses the ip the user chose
     * @param ip the chosen ip address
     */
    void updateReadIp(String ip);

    /**
     * uses the port that the user chose
     * @param port the chosen port
     */
    void updateReadPort(String port);

    /**
     * uses the number of players chosen by the user
     * @param numPlayers the number of players
     */
    void updateReadNumPlayers(int numPlayers);

    /**
     * uses the name the user chose
     * @param name the chosen name
     */
    void updateReadName(String name);

    /**
     * uses the intent of the player to restore a saved game or not
     * @param restore the intent to restore
     */
    void updateReadRestore(boolean restore);

    /**
     * uses the choice of card from the deck that the user made
     * @param numCard the chosen card
     */
    void updateReadNumCard(int numCard);

    /**
     * uses the choice of card from the subset of possible cards
     * @param numCard the chosen card
     */
    void updateReadGod(int numCard);

    /**
     * uses the action that the player chose
     * @param action the chosen action
     */
    void updateReadAction(Action action);

    /**
     * uses the choice to end the turn prematurely made by the user
     */
    void updateReadVoluntaryEndOfTurn();
}
