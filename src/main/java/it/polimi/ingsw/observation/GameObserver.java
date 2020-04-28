package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Sex;

import java.util.List;

public interface GameObserver {

    public void updateNumPlayers(int numPlayers);
    public void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn);
    public void updateEndOfTurnPlayer(int id);
    public void updateAction(int id, Action action);
    public void updatePlayerWin(int id);
    public void updatePlayerLose(int id);
}
