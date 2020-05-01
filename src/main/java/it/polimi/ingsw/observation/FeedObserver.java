package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public interface FeedObserver {

    public void updateNumPlayers(int numPlayers);
    public void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn);
    public void updateEndOfTurnPlayer(int id);
    public void updateAction(int id, Action action);
    public void updatePlayerWin(int id);
    public void updatePlayerLose(int id);

    public void updateStart();
    public void updateID(int id);
    public void updateName(int id, String name);
    public void updateAllPlayersConnected();
    public void updateDeck(Deck deck);
    public void updateCards(int id, List<Card> cards);
    public void updateGod(int id, Card card);
    public void updateOk(int id);
    public void updateKo(int id);
}
