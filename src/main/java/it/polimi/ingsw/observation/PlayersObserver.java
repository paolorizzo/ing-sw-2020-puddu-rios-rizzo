package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public interface PlayersObserver {

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
