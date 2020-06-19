package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;
import java.util.Map;

/**
 * Observer interface for the Feed, which incapsulates the communication from a model to a view
 */
public interface FeedObserver {

    //general updates
    void updateOk(int id);
    void updateKo(int id, String problem);
    void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn);

    //connection phase updates
    void updateStart();
    void updateID(int id);
    void updateNumPlayers(int numPlayers);
    void updateAllPlayersConnected();
    void updateName(int id, String name);

    //restore phase updates
    void updateGameAvailable(boolean available);
    void updateRestore(boolean intentToRestore);
    void updateRemap(Map<Integer, Integer> idMap);
    void updateResume();

    //setup phase updates
    void updateDeck(Deck deck);
    void updateCards(int id, List<Card> cards);
    void updateGod(int id, Card card);

    //turn phase updates
    void updateEndOfTurnPlayer(int id);
    void updateAction(int id, Action action);
    void updatePlayerWin(int id);
    void updatePlayerLose(int id);

}
