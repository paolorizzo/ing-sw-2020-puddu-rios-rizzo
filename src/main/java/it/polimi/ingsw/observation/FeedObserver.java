package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;
import java.util.Map;

/**
 * Observer interface for the Feed, which incapsulates the communication from a model to a view
 * There are 2 main classes that inherit from this one: ClientView and VirtualView
 * ClientView exists on the client and actually contains the logic for the communications in the MVC pattern
 * VirtualView exists on the server and only propagates notifies from the model through the connection
 * with the client. It serves to let the serves ignore the fact that it is not talking to a functioning view
 * For every method here, VirtualView simply forwards it and ClientView actually serves it
 */
public interface FeedObserver {

    //general updates

    /**
     * handles a positive feedback
     * @param id the id of the client that the ok is directed to
     */
    void updateOk(int id);

    /**
     * handles a negative feedback
     * @param id the id of the client that he ko is directed to
     * @param problem the problem that caused the ko
     */
    void updateKo(int id, String problem);

    /**
     * handles the notification about whose turn it is
     * @param id the id of the player whose turn it is
     * @param possibleActions the list of actions that that player can take
     * @param canEndOfTurn boolean representing whether that player can end their turn at this time
     */
    void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn);

    //connection phase updates

    /**
     * starts the view
     */
    void updateStart();

    /**
     * handles the new id
     * typically, a view will ignore the id if it already has one
     * @param id the new id
     */
    void updateID(int id);

    /**
     * handles the info about the number of players in the game
     * @param numPlayers the numebr of players in the game
     */
    void updateNumPlayers(int numPlayers);

    /**
     * handles the connection of all payers
     */
    void updateAllPlayersConnected();

    /**
     * handles the association between an id and a name
     * @param id the id of the client
     * @param name the name of the client
     */
    void updateName(int id, String name);

    //restore phase updates

    /**
     * handles the information about the existence of a saved game
     * @param available flag which is true if there is a saved game for the given set of names
     */
    void updateGameAvailable(boolean available);

    /**
     * handles the information about the intention to restore an existing saved game
     * @param intentToRestore flag which is true if there is an intention to restore a saved game
     */
    void updateRestore(boolean intentToRestore);

    /**
     * handles the remapping of the ids, to match those of the saved game
     * @param idMap the map from current ids to previous ids
     */
    void updateRemap(Map<Integer, Integer> idMap);

    /**
     * resumes the game after a restore
     */
    void updateResume();

    //setup phase updates

    /**
     * handles the receival of the deck
     * @param deck the deck of all possible cards
     */
    void updateDeck(Deck deck);

    /**
     * handles the receival of the subset of cards which a player can choose from
     * @param id the id of the player for whom the list of cards is valid
     * @param cards the list of acceptable cards for that player
     */
    void updateCards(int id, List<Card> cards);

    /**
     * handles the association between an id and a card
     * @param id the id of a client
     * @param card the client's chosen card
     */
    void updateGod(int id, Card card);

    //turn phase updates

    /**
     * Handles the choice of a player to end their turn
     * @param id the id of the player
     */
    void updateEndOfTurnPlayer(int id);

    /**
     * handles the action a player took
     * @param id the id of the player
     * @param action the action they took
     */
    void updateAction(int id, Action action);

    /**
     * handles the win of a player
     * @param id the id of the winning player
     */
    void updatePlayerWin(int id);

    /**
     * handles the loss of a player
     * @param id the id of the losing player
     */
    void updatePlayerLose(int id);

}
