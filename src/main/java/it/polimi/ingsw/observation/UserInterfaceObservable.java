package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class UserInterfaceObservable extends Observable<UserInterfaceObserver>{

    public UserInterfaceObservable(){
        super();
    }

    public synchronized void notifyReadNumPlayers(int numPlayers){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadNumPlayers(numPlayers);
        }
    }
    public synchronized void notifyReadName(String name){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadName(name);
        }
    }
}
