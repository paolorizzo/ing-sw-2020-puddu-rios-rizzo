package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.List;

public class FeedObservable extends Observable<FeedObserver> {

    public FeedObservable(){
        super();
    }

    public synchronized void notifyNumPlayers(int numPlayers){
        for(FeedObserver obs:observers){
            obs.updateNumPlayers(numPlayers);
        }
    }
    public synchronized void notifyCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn) {
        for(FeedObserver obs:observers){
            obs.updateCurrentPlayer(id, possibleActions, canEndOfTurn);
        }
    }
    public synchronized void notifyEndOfTurnPlayer(int id) {
        for(FeedObserver obs:observers){
            obs.updateEndOfTurnPlayer(id);
        }
    }
    public synchronized void notifyAction(int id, Action action){
        for(FeedObserver obs:observers){
            obs.updateAction(id, action);
        }
    }
    public synchronized void notifyPlayerWin(int id){
        for(FeedObserver obs:observers){
            obs.updatePlayerWin(id);
        }
    }
    public synchronized void notifyPlayerLose(int id){
        for(FeedObserver obs:observers){
            obs.updatePlayerLose(id);
        }
    }



    public synchronized void notifyStart(){
        //System.out.println("starting client");
        for(FeedObserver obs:observers){
            obs.updateStart();
        }
    }

    public synchronized void notifyID(int id){
        //System.out.println("notifyID with id: "+id);
        for(FeedObserver obs:observers){
            obs.updateID(id);
        }
    }

    public synchronized void notifyName(int id, String name){
        for(FeedObserver obs:observers){
            obs.updateName(id, name);
        }
    }

    public synchronized void notifyAllPlayersConnected(){
        for(FeedObserver obs:observers){
            obs.updateAllPlayersConnected();
        }
    }
    public synchronized void notifyDeck(Deck deck) {
        for(FeedObserver obs:observers){
            obs.updateDeck(deck);
        }
    }

    public synchronized void notifyCards(Integer id, List<Card> cards) {
        for(FeedObserver obs:observers){
            obs.updateCards(id, cards);
        }
    }

    public synchronized void notifyGod(Integer id, Card card){
        for(FeedObserver obs:observers){
            obs.updateGod(id, card);
        }
    }


    public synchronized void notifyOk(int id) {
        for(FeedObserver obs:observers){
            obs.updateOk(id);
        }
    }

    public synchronized void notifyKo(int id){
        for(FeedObserver obs:observers){
            obs.updateKo(id);
        }
    }
}
