package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable class for the interaction between the User Interfaces and the ClientView
 */
public class UserInterfaceObservable extends Observable<UserInterfaceObserver>{

    /**
     * simple constructor that reflects on the superclass
     */
    public UserInterfaceObservable(){
        super();
    }

    /**
     * notifies observers regarding the ip chosen by the user
     * @param ip the chosen ip
     */
    public synchronized void notifyIp(String ip){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadIp(ip);
        }
    }

    /**
     * notifies observers regarding the port chosen by the user
     * @param port the chosen port
     */
    public synchronized void notifyPort(String port){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadPort(port);
        }
    }

    /**
     * notifies observers regarding the number of players chosen by the user
     * @param numPlayers the chosen number of players
     */
    public synchronized void notifyReadNumPlayers(int numPlayers){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadNumPlayers(numPlayers);
        }
    }

    /**
     * notifies observers regarding the name chosen by the user
     * @param name the chosen name
     */
    public synchronized void notifyReadName(String name){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadName(name);
        }
    }

    /**
     * notifies observers regarding the choice of the user about whether to restore
     * a saved game or not
     * @param restore the user's choice
     */
    public synchronized void notifyReadRestore(boolean restore){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadRestore(restore);
        }
    }

    /**
     * notifies observers regarding the number of the card chosen from the deck by the player
     * @param numCard the number of the card
     */
    public synchronized void notifyReadNumCard(int numCard){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadNumCard(numCard);
        }
    }

    /**
     * notifies observers regarding the number of the card chosen from the possible ones
     * by the player
     * @param numCard the number of the card
     */
    public synchronized void notifyReadGod(int numCard){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadGod(numCard);
        }
    }

    /**
     * notifies observers regarding the number action chosen by the user
     * @param action the chosen action
     */
    public synchronized void notifyReadAction(Action action){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadAction(action);
        }
    }

    /**
     * notifies observers regarding the choice of the user to end their turn prematurely
     */
    public synchronized void notifyReadVoluntaryEndOfTurn(){
        for(UserInterfaceObserver obs:observers){
            obs.updateReadVoluntaryEndOfTurn();
        }
    }

}
