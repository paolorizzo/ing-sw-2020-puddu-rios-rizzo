package it.polimi.ingsw.view;


import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public interface UserInterface
{
    void showLogo();

    void askNumPlayers();
    void askUsername();
    void askCard(Deck deck);
    void askGod(List<Card> cards);
    void askSetupWorker(List<Action> possibleActions);
    void askAction(List<Action> possibleActions, boolean canEndOfTurn);

    int getNumPlayersRegister();

    void registerPlayer(int id, String name);
    void registerGod(int id, Card card);
    void executeAction(Action action);

    void removeWorkersOfPlayer(int id);
}
