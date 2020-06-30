package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.SetupAction;

import java.util.List;

/**
 * Interface for a controller
 * It is implemented by two classes, Controller and Client
 * Controller actually implements the logic, providing a way to modify the model
 * Client only offers the interface to allow ClientView, the view on the client,
 * to believe it is talking to a real controller. What it actually does is simply
 * forward the method call through our remote method invocation system
 */
public interface ControllerInterface {

    //general

    /**
     * takes actions based on the disconnection of a view
     */
    void handleDisconnection();

    /**
     * kills the controller
     */
    void kill();
    //connection phase

    /**
     * generates an id in a sequential fashion, and
     * publishes it
     */
    void generateId();

    /**
     * acknowledges that the last generated id has been well received,
     * and prepares to accept the next view and generate the next id
     * @param id the id that is being acknowledged
     */
    void ackId(int id);

    /**
     * Sets the number of players
     * @param id the id of the view that is setting it
     * @param numPlayers the number of players
     */
    void setNumPlayers(int id, int numPlayers);

    /**
     * prompts the controller to publish the number of
     * players to the observers
     */
    void getNumPlayers();

    /**
     * prompts the controller to publish a custom message
     * when all players are connected
     */
    void requestAllPlayersConnected();

    /**
     * requests a name for the player with the given id
     * @param id the id of the player for whom to set the name
     * @param name the chosen name
     */
    void setName(int id, String name);
    //restore phase

    /**
     * asks the controller if there is a game available for the current set
     * of players. The controller will answer through the observer-observable pattern
     */
    void isGameAvailable();

    /**
     * tells the controller to restore (or not to restore)
     * an existing saved game
     * @param id the id of the commanding view
     * @param intentToRestore the intention to restore the saved game or not
     */
    void restore(int id, boolean intentToRestore);

    /**
     * publishes info about whether a saved game will be restored or not
     */
    void willRestore();

    //setup phase

    /**
     * requests the controller to send the full deck
     */
    void requestDeck();

    /**
     * tells the controller the numbers of the cards chosen
     * by the first players
     * @param id the id of the view that is issuing the command
     * @param numCards the list of the numbers of the cards that were chosen
     */
    void publishCards(int id, List<Integer> numCards);

    /**
     * requests the list of cards that are allowed for a player
     * @param id the id of the player for whom the list is valid
     * @throws InterruptedException if the wait of the requester is interrupted
     */
    void requestCards(int id) throws InterruptedException;

    /**
     * sets the card for a player
     * @param id the id of the player
     * @param numCard the number of the card the player has chosen
     */
    void setCard(int id, int numCard);

    /**
     * requests to place a worker on the board
     * the permission is granted automatically when it becomes
     * the requesting client's turn
     * @param id the id of the requesting client
     * @throws InterruptedException if the wait of the requester is interrupted
     */
    void requestToSetupWorker(int id) throws InterruptedException;

    /**
     * places a worker on the board for a given player
     * @param id the id of the player
     * @param action the setup action to place the worker
     */
    void setupWorker(int id, SetupAction action);

    //game phase

    /**
     * requests to perform an action
     * the permission is granted automatically when
     * it becomes the requesting client's turn
     * @param id the id of the requesting client's
     * @throws InterruptedException if the wait of the requester is interrupted
     */
    void requestActions(int id) throws InterruptedException;

    /**
     * publishes an action taken by a player
     * @param id the id of the player
     * @param action the action chosen
     */
    void publishAction(int id, Action action);

    /**
     * publishes the intention to voluntarily end the turn
     * @param id the id of the publishing player
     */
    void publishVoluntaryEndOfTurn(int id);

    /**
     * deletes the view related to an id
     * @param id the id of the view to be deleted
     */
    void deleteId(int id);

}
