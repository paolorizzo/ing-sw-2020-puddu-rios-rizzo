package it.polimi.ingsw.view;


import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

/**
 * interface for the user interfaces, which are the cli and the gui.
 */
public interface UserInterface
{
    /**
     * Asks for the IP address and port number of the desired game server.
     */
    void askIpAndPort();

    /**
     * Asks for the desired number of players.
     */
    void askNumPlayers();

    /**
     * Asks for the desired username.
     */
    void askUsername();

    /**
     * Asks if the user wants to restore the stored game.
     */
    void askRestore();

    /**
     * Asks the user to select a card from the deck.
     * @param deck the collection of cards.
     */
    void askCard(Deck deck);

    /**
     * Asks the user to select a god for the game.
     * @param cards the set of available cards.
     */
    void askGod(List<Card> cards);

    /**
     * Asks the user to set up a worker on the board.
     * @param possibleActions the possible setup choices list.
     */
    void askSetupWorker(List<Action> possibleActions);

    /**
     * Asks the user to perform an action.
     * @param possibleActions the possible actions list.
     * @param canEndOfTurn a boolean flag indicating the possibility to end the current turn within the current action.
     */
    void askAction(List<Action> possibleActions, boolean canEndOfTurn);

    /**
     * Retrieves the current number of active players in the game.
     * @return the number of active players.
     */
    int getNumPlayersRegister();

    /**
     * Sets the number of players in the user interface model representation.
     * @param numPlayers the number of players.
     */
    void setNumPlayers(int numPlayers);

    /**
     * Adds a new player to the game.
     * @param id the players's id.
     * @param name the player's username.
     */
    void registerPlayer(int id, String name);

    /**
     * Registers the card chosen by another player.
     * @param id the player's id.
     * @param card the chosen power card.
     */
    void registerGod(int id, Card card);

    /**
     * Updates the user interface following an action.
     * @param action the action object.
     */
    void executeAction(Action action);

    /**
     * Displays the win dialog.
     * @param id the winner's id.
     */
    void winAnnounce(int id);

    /**
     * Displays the lose dialog.
     * @param id the loser's id.
     */
    void loseAnnounce(int id);

    /**
     * Removes all the active workers of a player from the board.
     * @param id the player's id.
     */
    void removeWorkersOfPlayer(int id);

    /**
     * Gets the number of players set for the game.
     * @return the original number of players.
     */
    int getNumPlayers();

    /**
     * Informs the user interface about the currently active player.
     * @param id the player's id.
     */
    void setCurrentPlayer(int id);

    /**
     * Displays an error message.
     * @param e the error text.
     */
    void showError(String e);

    /**
     * Informs the user about an occurred disconnection.
     * @param message a String containing details about the cause of the disconnection.
     */
    void showDisconnection(String message);
}
