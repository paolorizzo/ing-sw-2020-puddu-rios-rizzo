package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayersObservable extends Observable<PlayersObserver>{

    public PlayersObservable(){
        super();
    }

    public void notifyAllID(int id){
        for(PlayersObserver obs:observers){
            obs.updateID(id);
        }
    }

    public void notifyAllName(int id, String name){
        for(PlayersObserver obs:observers){
            obs.updateName(id, name);
        }
    }

}
