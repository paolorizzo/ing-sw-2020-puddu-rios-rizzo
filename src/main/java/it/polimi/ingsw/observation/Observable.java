package it.polimi.ingsw.observation;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {
    protected List<T> observers;

    public Observable(){
        observers = new ArrayList<T>();
    }

    public void addObserver(T observer){
        if(!observers.contains(observer))
            observers.add(observer);
    }
}
