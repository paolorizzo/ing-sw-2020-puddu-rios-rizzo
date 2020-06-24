package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.observation.*;
import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Message;

import java.util.List;
import java.util.Map;

/**
 * View inheritor that simulates the presence of an actual view on the server side
 * of the MVC pattern. This allows to have a full MVC pattern on the server, even though
 * the View logic is on the client. This class mainly forwards the notification calls
 * through a custom remote method invocation
 */
public class VirtualView extends View
{
    //inherits attributes from View, notably the feeds
    private Connection connection;

    /**
     * simple constructor that sets the connection on the view
     * @param c the connection
     */
    public VirtualView(Connection c)
    {
        super();
        this.connection = c;
    }

    /**
     * calls on the local controller, which is the Controller,
     * to handle a disconnection
     */
    public synchronized void connectionLost()
    {
        System.out.println("Connection lost " + this);
        getController().handleDisconnection();
    }

    //general updates

    /**
     * forwards the update through the socket
     * @param id the id of the client that the ok is directed to
     */
    @Override
    public synchronized void updateOk(int id){
        connection.sendMessage("notifyOk", id);
    }

    /**
     * forwards the update through the socket
     * @param id the id of the client that he ko is directed to
     * @param problem the problem that caused the ko
     */
    @Override
    public synchronized void updateKo(int id, String problem){
        connection.sendMessage("notifyKo", id, problem);
    }

    //connection phase updates

    /**
     * forwards the update through the socket
     */
    @Override
    public synchronized void updateStart(){
        connection.sendMessage("notifyStart");
    }

    /**
     * forwards the update through the socket
     * @param id the new id
     */
    @Override
    public synchronized void updateID(int id){
        connection.sendMessage("notifyID", id);
    }

    /**
     * forwards the update through the socket
     * @param numPlayers the numebr of players in the game
     */
    @Override
    public synchronized void updateNumPlayers(int numPlayers){
        connection.sendMessage("notifyNumPlayers", numPlayers);
    }

    /**
     * forwards the update through the socket
     */
    @Override
    public synchronized void updateAllPlayersConnected(){ connection.sendMessage("notifyAllPlayersConnected"); }

    @Override
    public synchronized void updateDisconnection()
    {

        connection.sendMessage("notifyDisconnection");
    }

    /**
     * forwards the update through the socket
     * @param id the id of the client
     * @param name the name of the client
     */
    @Override
    public synchronized void updateName(int id, String name){
        connection.sendMessage("notifyName", id, name);
    }

    //restore phase updates
    /**
     * forwards the update through the socket
     * @param available flag which is true if there is a saved game for the given set of names
     */
    @Override
    public synchronized void updateGameAvailable(boolean available){
        connection.sendMessage("notifyGameAvailable", available);
    }

    /**
     * forwards the update through the socket
     * @param intentToRestore flag which is true if there is an intention to restore a saved game
     */
    @Override
    public synchronized void updateRestore(boolean intentToRestore){
        connection.sendMessage("notifyRestore", intentToRestore);
    }

    /**
     * forwards the update through the socket
     * @param idMap the map from current ids to previous ids
     */
    @Override
    public synchronized void updateRemap(Map<Integer, Integer> idMap){
        connection.sendMessage("notifyRemap", idMap);
    }

    /**
     * forwards the update through the socket
     */
    @Override
    public synchronized void updateResume(){
        connection.sendMessage("notifyResume");
    }

    //setup phase updates
    /**
     * forwards the update through the socket
     * @param deck the deck of all possible cards
     */
    @Override
    public synchronized void updateDeck(Deck deck){
        connection.sendMessage("notifyDeck", deck);
    }

    /**
     * forwards the update through the socket
     * @param id the id of the player for whom the list of cards is valid
     * @param cards the list of acceptable cards for that player
     */
    @Override
    public synchronized void updateCards(int id, List<Card> cards){
        connection.sendMessage("notifyCards", id, cards);
    }

    /**
     * forwards the update through the socket
     * @param id the id of a client
     * @param card the client's chosen card
     */
    @Override
    public synchronized void updateGod(int id, Card card) {connection.sendMessage("notifyGod", id, card); }


    //turn phase updates

    /**
     * forwards the update through the socket
     * @param id the id of the player whose turn it is
     * @param possibleActions the list of actions that that player can take
     * @param canEndOfTurn boolean representing whether that player can end their turn at this time
     */
    @Override
    public synchronized void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn) {connection.sendMessage("notifyCurrentPlayer", id, possibleActions, canEndOfTurn); }

    /**
     * forwards the update through the socket
     * @param id the id of the player
     */
    @Override
    public synchronized void updateEndOfTurnPlayer(int id) {connection.sendMessage("notifyEndOfTurnPlayer", id); }

    /**
     * forwards the update through the socket
     * @param id the id of the player
     * @param action the action they took
     */
    @Override
    public synchronized void updateAction(int id, Action action) {connection.sendMessage("notifyAction", id, action); }

    /**
     * forwards the update through the socket
     * @param id the id of the winning player
     */
    @Override
    public synchronized void updatePlayerWin(int id) {connection.sendMessage("notifyPlayerWin", id); }

    /**
     * forwards the update through the socket
     * @param id the id of the losing player
     */
    @Override
    public synchronized void updatePlayerLose(int id) {connection.sendMessage("notifyPlayerLose", id); }
}
