package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Observable that only accepts a specific type of observer
 * @param <T> the type of observer to be accepted
 */
public abstract class Observable<T> {
    protected List<T> observers;

    /**
     * constructs an empty observable for a specific class of observer
     */
    public Observable(){
        observers = new ArrayList<T>();
    }

    /**
     * adds an observer of the correct type
     * @param observer the observer to be added
     */
    public synchronized void addObserver(T observer){
        if(!observers.contains(observer))
            observers.add(observer);
    }

    /**
     * checks if an observable has any observers
     * @return true if the observable has at least an observer
     */
    public boolean hasObservers(){
        return !observers.isEmpty();
    }

    /**
     * removes an observer from the list, if it is present
     * @param observer the observer to be removed
     */
    public synchronized void removeObserver(T observer){
        observers.remove(observer);
    }

}
