package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Observable
{
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void notify(Object o){
        synchronized (observers) {
            for (Observer observer : observers) {
                observer.update(o);
            }
        }
    }

    public boolean hasObservers()
    {
        return observers.size()>0;
    }
}

