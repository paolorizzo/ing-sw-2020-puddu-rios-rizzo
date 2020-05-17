package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.observation.*;
import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Message;

import java.util.List;

//TODO test
public class VirtualView extends View
{
    //inherits attributes from View, notably the feeds
    private Connection connection;

    public VirtualView(Connection c)
    {
        super();
        this.connection = c;
    }

    public synchronized void clientNotReachable()
    {
        System.out.println("Client not reachable " + this);
    }

    //general updates
    @Override
    public synchronized void updateOk(int id){
        connection.sendMessage("notifyOk", id);
    }

    @Override
    public synchronized void updateKo(int id, String problem){
        connection.sendMessage("notifyKo", id, problem);
    }

    //connection phase updates
    @Override
    public synchronized void updateStart(){
        connection.sendMessage("notifyStart");
    }

    @Override
    public synchronized void updateID(int id){
        connection.sendMessage("notifyID", id);
    }

    @Override
    public synchronized void updateNumPlayers(int numPlayers){
        connection.sendMessage("notifyNumPlayers", numPlayers);
    }

    @Override
    public synchronized void updateAllPlayersConnected(){ connection.sendMessage("notifyAllPlayersConnected"); }

    @Override
    public synchronized void updateName(int id, String name){
        connection.sendMessage("notifyName", id, name);
    }


    //setup phase updates@Override
    public synchronized void updateDeck(Deck deck){
        connection.sendMessage("notifyDeck", deck);
    }

    @Override
    public synchronized void updateCards(int id, List<Card> cards){
        connection.sendMessage("notifyCards", id, cards);
    }

    @Override
    public synchronized void updateGod(int id, Card card) {connection.sendMessage("notifyGod", id, card); }


    //turn phase updates
    @Override
    public synchronized void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn) {connection.sendMessage("notifyCurrentPlayer", id, possibleActions, canEndOfTurn); }

    @Override
    public synchronized void updateEndOfTurnPlayer(int id) {connection.sendMessage("notifyEndOfTurnPlayer", id); }

    @Override
    public synchronized void updateAction(int id, Action action) {connection.sendMessage("notifyAction", id, action); }

    @Override
    public synchronized void updatePlayerWin(int id) {connection.sendMessage("notifyPlayerWin", id); }

    @Override
    public synchronized void updatePlayerLose(int id) {connection.sendMessage("notifyPlayerLose", id); }
}
