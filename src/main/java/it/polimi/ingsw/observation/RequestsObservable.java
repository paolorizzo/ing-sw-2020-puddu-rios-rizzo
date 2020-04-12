package it.polimi.ingsw.observation;

import java.util.ArrayList;
import java.util.List;

public class RequestsObservable extends Observable<RequestsObserver>{

    public RequestsObservable(){
        super();
    }

    public synchronized void notifyRequestID(){
        for(RequestsObserver obs:observers){
            obs.updateRequestID();
        }
    }

    public synchronized void notifyRequestNumPlayers(){
        for(RequestsObserver obs:observers){
            obs.updateRequestID();
        }
    }

}