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

    //updates relative to GameObserver

    public synchronized void updateNumPlayers(int numPlayers){
        connection.sendMessage("notifyNumPlayers", numPlayers);
    }

    //updates relative to PlayersObserver

    public synchronized void updateStart(){
        connection.sendMessage("notifyStart");
    }

    public synchronized void updateID(int id){
        connection.sendMessage("notifyID", id);
    }

    public synchronized void updateName(int id, String name){
        connection.sendMessage("notifyName", id, name);
    }

    public synchronized void updateOk(int id){
        connection.sendMessage("notifyOk", id);
    }

    public synchronized void updateKo(int id){
        connection.sendMessage("notifyKo", id);
    }

    public synchronized void updateAllPlayersConnected(){ connection.sendMessage("notifyAllPlayersConnected"); }

    public synchronized void updateDeck(Deck deck){
        connection.sendMessage("notifyDeck", deck);
    }

    public synchronized void updateCards(int id, List<Card> cards){
        connection.sendMessage("notifyCards", id, cards);
    }

    public synchronized void updateGod(int id, Card card) {connection.sendMessage("notifyGod", id, card); }

    public synchronized void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn) {connection.sendMessage("notifyCurrentPlayer", id, possibleActions, canEndOfTurn); }

    public synchronized void updateEndOfTurnPlayer(int id) {connection.sendMessage("notifyEndOfTurnPlayer", id); }

    public synchronized void updateAction(int id, Action action) {connection.sendMessage("notifyAction", id, action); }

    public synchronized void updatePlayerWin(int id) {connection.sendMessage("notifyPlayerWin", id); }

    public synchronized void updatePlayerLose(int id) {connection.sendMessage("notifyPlayerLose", id); }
}
