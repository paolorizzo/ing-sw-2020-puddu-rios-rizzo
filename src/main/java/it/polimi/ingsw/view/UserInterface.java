package it.polimi.ingsw.view;


import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public interface UserInterface
{
    void askNumPlayers();
    void askUsername();
    void askGod(List<Card> cards);
    void askSetupWorker(List<Action> possbileActions);
    void askAction(List<Action> possibleActions);

    void showLogo();


    void askCard(Deck deck);

    int getNumPlayersRegister();

    void registerPlayer(int id, String name);
    void registerGod(int id, Card card);

    void executeAction(Action action);

}
