package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public class StubView extends View{

    //general updates
    @Override
    public void updateOk(int id){}
    @Override
    public void updateKo(int id){}

    //connection phase updates
    @Override
    public void updateStart(){}
    @Override
    public void updateID(int id){}
    @Override
    public void updateNumPlayers(int numPlayers){}
    @Override
    public void updateName(int id, String name){}

    //setup phase updates
    @Override
    public void updateAllPlayersConnected(){}
    @Override
    public void updateDeck(Deck deck){}
    @Override
    public void updateCards(int id, List<Card> cards){}
    @Override
    public void updateGod(int id, Card card){}
    @Override
    public void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn){}

    //turn phase updates
    @Override
    public void updateEndOfTurnPlayer(int id){}
    @Override
    public void updateAction(int id, Action action){}
    @Override
    public void updatePlayerWin(int id){}
    @Override
    public void updatePlayerLose(int id){}


}
