package it.polimi.ingsw.observation;

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
}
