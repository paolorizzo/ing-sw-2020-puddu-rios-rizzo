package it.polimi.ingsw.view;


import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public interface UserInterface
{


    void askNumPlayers();
    void askUsername();
    void askCard(Deck deck);
    void askGod(List<Card> cards);
    void askSetupWorker(List<Action> possibleActions);
    void askAction(List<Action> possibleActions, boolean canEndOfTurn);

    int getNumPlayersRegister();

    void setNumPlayers(int numPlayers);
    void registerPlayer(int id, String name);
    void registerGod(int id, Card card);
    void executeAction(Action action);
    void winAnnounce(int id);
    void loseAnnounce(int id);

    void removeWorkersOfPlayer(int id);

    int getNumPlayers();

    void showLogo();
    void setCurrentPlayer(int id);
    void showError(String e);
}
