package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
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
    public synchronized void notifyReadRestore(boolean restore){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadRestore(restore);
        }
    }
    public synchronized void notifyReadNumCard(int numCard){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadNumCard(numCard);
        }
    }
    public synchronized void notifyReadGod(int numCard){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadGod(numCard);
        }
    }
    public synchronized void notifyReadAction(Action action){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadAction(action);
        }
    }
    public synchronized void notifyReadVoluntaryEndOfTurn(){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadVoluntaryEndOfTurn();
        }
    }

}
