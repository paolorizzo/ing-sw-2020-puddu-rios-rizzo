package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayersObservable extends Observable<PlayersObserver>{

    public PlayersObservable(){
        super();
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

}
