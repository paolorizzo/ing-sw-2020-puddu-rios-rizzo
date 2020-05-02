package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public class StubView extends View{
    public int id;
    public int numPlayers;
    public boolean allPlayersConnected;
    public String name;

    public StubView(){
        super();
        id = -1;
        numPlayers = 0;
        allPlayersConnected = false;
        name = null;
    }

    //general updates
    @Override
    public void updateOk(int id){}
    @Override
    public void updateKo(int id){}

    //connection phase updates
    @Override
    public void updateStart(){}
    @Override
    public void updateID(int id){
        if(this.id == -1)
            this.id = id;
    }
    @Override
    public void updateNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }
    @Override
    public void updateAllPlayersConnected(){
        allPlayersConnected = true;
    }
    @Override
    public void updateName(int id, String name){
        if(this.id == id)
            this.name = name;
    }

    //setup phase updates
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
