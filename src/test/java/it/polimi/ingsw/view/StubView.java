package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

/**
 * the purpose of this class is to offer the same interface that VirtualView offers, to test
 * the controller and its integration with the model in a functioning mvc
 * its methods are not supposed to have any particular significance, hence why most are blank
 * a couple of them have a body, when it is useful for testing purposes
 */
public class StubView extends View{
    public int id;
    public int numPlayers;
    public boolean allPlayersConnected;
    public String name;
    public Deck deck;
    public List<Card> chosenCards;
    public Card god;

    public StubView(){
        super();
        id = -1;
        numPlayers = 0;
        allPlayersConnected = false;
        name = null;
        deck = null;
        chosenCards = null;
        god = null;
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
    public void updateDeck(Deck deck){
        this.deck = deck;
    }
    @Override
    public void updateCards(int id, List<Card> cards){
        if(this.id ==id)
            this.chosenCards = cards;
    }
    @Override
    public void updateGod(int id, Card card){
        if(this.id == id)
            god = card;
    }
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
