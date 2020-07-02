package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;
import java.util.Map;

/**
 *The observable class for the feed, which is the connection point
 * from which the model delivers updates to the views
 */
public class FeedObservable extends Observable<FeedObserver> {

    /**
     * simple constructor that reflects on Observable<T>
     */
    public FeedObservable(){
        super();
    }

    //general notifies

    /**
     * notifies the observers that an action has been successful
     * @param id the id of the client whose action was successful
     */
    public synchronized void notifyOk(int id) {
        for(FeedObserver obs:observers){
            obs.updateOk(id);
        }
    }

    /**
     * notifies the observers that an action was not successful
     * @param id the id of the client whose action failed
     * @param problem a description of what went wrong
     */
    public synchronized void notifyKo(int id, String problem){
        for(FeedObserver obs:observers){
            obs.updateKo(id, problem);
        }
    }

    public synchronized void notifyDisconnection()
    {
        for(FeedObserver obs:observers){
            try
            {
                obs.updateDisconnection();
            }
            catch(Exception e)
            {
                System.out.println("Notifying all the players about the disconnection");
            }

        }
    }

    /**
     * notifies everyone whose turn it is
     * @param id the id of the current player
     * @param possibleActions a list of actions that the players can take
     * @param canEndOfTurn true if the player can choose to end their turn at this time
     */
    public synchronized void notifyCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn) {
        for(FeedObserver obs:observers){
            obs.updateCurrentPlayer(id, possibleActions, canEndOfTurn);
        }
    }

    //notifies related to the connection phase

    /**
     * notifies a client that it can start
     * This is one of the components that handles the synchronization during the Connection of the
     * clients
     */
    public synchronized void notifyStart(){
        for(FeedObserver obs:observers){
            obs.updateStart();
        }
    }

    /**
     * notifies a client of what its id is
     * Every client connected so far gets this notification, but it is ignored if a
     * client already possesses an id
     * @param id the id given to the client
     */
    public synchronized void notifyID(int id){
        for(FeedObserver obs:observers){
            obs.updateID(id);
        }
    }

    /**
     * notifies the number of players
     * @param numPlayers the number of players
     */
    public synchronized void notifyNumPlayers(int numPlayers){
        for(FeedObserver obs:observers){
            obs.updateNumPlayers(numPlayers);
        }
    }

    /**
     * notifies the clients that all players are connected
     */
    public synchronized void notifyAllPlayersConnected(){
        for(FeedObserver obs:observers){
            obs.updateAllPlayersConnected();
        }
    }

    /**
     * notifies the association between an id and a name
     * @param id the id of the client
     * @param name the name the client has chosen
     */
    public synchronized void notifyName(int id, String name){
        for(FeedObserver obs:observers){
            obs.updateName(id, name);
        }
    }

    //notifies related to the restore phase
    /**
     * notifies observers regarding the availability of a saved game for their name set
     * @param available represents whether a game is available
     */
    public synchronized void notifyGameAvailable(boolean available){
        for(FeedObserver obs:observers){
            obs.updateGameAvailable(available);
        }
    }

    /**
     * notifies observer regarding whether a saved game will be restored or not
     * @param intentToRestore a boolean representing the decision to restore the game or not
     */
    public synchronized void notifyRestore(boolean intentToRestore){
        for(FeedObserver obs:observers){
            obs.updateRestore(intentToRestore);
        }
    }

    /**
     * notifies to the observers the need to remap their ids
     * @param idMap a map containing the info about the new ids for every old id
     */
    public synchronized void notifyRemap(Map<Integer, Integer> idMap){
        for(FeedObserver obs:observers){
            obs.updateRemap(idMap);
        }
    }

    /**
     * notifies to the observers the restoration has terminated, and they can resume the game
     */
    public synchronized void notifyResume(){
        for(FeedObserver obs:observers){
            obs.updateResume();
        }
    }

    //notifies related to the setup phase

    /**
     * sends the deck as a notification
     * @param deck the deck of cards from which it is possible to pick
     */
    public synchronized void notifyDeck(Deck deck) {
        for(FeedObserver obs:observers){
            obs.updateDeck(deck);
        }
    }

    /**
     * sends the set of cards that it is possible to pick a god from
     * @param id the id of the client to whom this set is being given
     * @param cards the list of cards from which to pick
     */
    public synchronized void notifyCards(Integer id, List<Card> cards) {
        for(FeedObserver obs:observers){
            obs.updateCards(id, cards);
        }
    }

    /**
     * notifies the choice of god made by a player
     * @param id the id of choosing player
     * @param card the card the player chose
     */
    public synchronized void notifyGod(Integer id, Card card){
        for(FeedObserver obs:observers){
            obs.updateGod(id, card);
        }
    }

    //notifies related to the turn phase

    /**
     * notifies a voluntary end of turn
     * This happens when a god power allows a player to make a third action, or to pass
     * @param id the id of the player that is ending their turn
     */
    public synchronized void notifyEndOfTurnPlayer(int id) {
        for(FeedObserver obs:observers){
            obs.updateEndOfTurnPlayer(id);
        }
    }

    /**
     * notifies an action taken by a player
     * @param id the id of the player that took the action
     * @param action the action taken
     */
    public synchronized void notifyAction(int id, Action action){
        for(FeedObserver obs:observers){
            obs.updateAction(id, action);
        }
    }

    /**
     * notifies the event that a player won
     * @param id the id of the winning player
     */
    public synchronized void notifyPlayerWin(int id){
        for(FeedObserver obs:observers){
            obs.updatePlayerWin(id);
        }
    }

    /**
     * notifies the loss of a player
     * @param id the id of the losing player
     */
    public synchronized void notifyPlayerLose(int id){
        for(FeedObserver obs:observers){
            obs.updatePlayerLose(id);
        }
    }

}
