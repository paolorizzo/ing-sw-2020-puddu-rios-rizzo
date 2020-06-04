package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public interface FeedObserver {

    //general updates
    public void updateOk(int id);
    public void updateKo(int id, String problem);
    public void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn);

    //connection phase updates
    public void updateStart();
    public void updateID(int id);
    public void updateNumPlayers(int numPlayers);
    public void updateAllPlayersConnected();
    public void updateName(int id, String name);

    //restore phase updates
    public void updateGameAvailable(boolean available);
    public void updateRestore(boolean intentToRestore);

    //setup phase updates
    public void updateDeck(Deck deck);
    public void updateCards(int id, List<Card> cards);
    public void updateGod(int id, Card card);

    //turn phase updates
    public void updateEndOfTurnPlayer(int id);
    public void updateAction(int id, Action action);
    public void updatePlayerWin(int id);
    public void updatePlayerLose(int id);

}
