package it.polimi.ingsw.observation;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {
    protected List<T> observers;

    public Observable(){
        observers = new ArrayList<T>();
    }

    public synchronized void addObserver(T observer){
        if(!observers.contains(observer))
            observers.add(observer);
    }

    public boolean hasObservers(){
        return !observers.isEmpty();
    }

    public synchronized void removeObserver(T observer){
        observers.remove(observer);
    }

}
