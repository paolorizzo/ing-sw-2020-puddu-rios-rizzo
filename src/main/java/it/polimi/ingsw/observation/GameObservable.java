package it.polimi.ingsw.observation;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObservable {
    private List<GameObserver> observers;

    public GameObservable(){
        observers=new ArrayList<GameObserver>();
    }

    public void addObserver(GameObserver observer){
        if(!observers.contains(observer))
            observers.add(observer);
    }

    public void notifyAllNumPlayers(int numPlayers){
        for(GameObserver obs:observers){
            obs.updateNumPlayers(numPlayers);
        }
    }
}
