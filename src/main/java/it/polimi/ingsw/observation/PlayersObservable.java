package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayersObservable extends Observable<PlayersObserver>{

    public PlayersObservable(){
        super();
    }

    public synchronized void notifyStart(){
        System.out.println("starting client");
        for(PlayersObserver obs:observers){
            obs.updateStart();
        }
    }

    public synchronized void notifyID(int id){
        System.out.println("notifyID with id: "+id);
        for(PlayersObserver obs:observers){
            obs.updateID(id);
        }
    }

    public synchronized void notifyName(int id, String name){
        for(PlayersObserver obs:observers){
            obs.updateName(id, name);
        }
    }

    public synchronized void notifyAllPlayersConnected(){
        for(PlayersObserver obs:observers){
            obs.updateAllPlayersConnected();
        }
    }
    public synchronized void notifyDeck(Deck deck) {
        for(PlayersObserver obs:observers){
            obs.updateDeck(deck);
        }
    }

    public synchronized void notifyCards(Integer id, List<Card> cards) {
        for(PlayersObserver obs:observers){
            obs.updateCards(id, cards);
        }
    }

    public synchronized void notifyGod(Integer id, Card card){
        for(PlayersObserver obs:observers){
            obs.updateGod(id, card);
        }
    }


    public synchronized void notifyOk(int id) {
        for(PlayersObserver obs:observers){
            obs.updateOk(id);
        }
    }

    public synchronized void notifyKo(int id){
        for(PlayersObserver obs:observers){
            obs.updateKo(id);
        }
    }
}
