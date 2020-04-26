package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Deck;

import java.util.ArrayList;
import java.util.List;

public class GameObservable extends Observable<GameObserver>{

    public GameObservable(){
        super();
    }

    public synchronized void notifyNumPlayers(int numPlayers){
        for(GameObserver obs:observers){
            obs.updateNumPlayers(numPlayers);
        }
    }
    public synchronized void notifyCurrentPlayer(int id, List<Action> possibleActions) throws InterruptedException {
        for(GameObserver obs:observers){
            obs.updateCurrentPlayer(id, possibleActions);
        }
    }
    public synchronized void notifyEndOfTurnPlayer(int id) {
        for(GameObserver obs:observers){
            obs.updateEndOfTurnPlayer(id);
        }
    }
    public synchronized void notifyAction(int id, Action action){
        for(GameObserver obs:observers){
            obs.updateAction(id, action);
        }
    }
}
