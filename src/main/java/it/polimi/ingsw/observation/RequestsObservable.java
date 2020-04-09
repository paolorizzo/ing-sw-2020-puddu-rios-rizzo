package it.polimi.ingsw.observation;

import java.util.ArrayList;
import java.util.List;

public abstract class RequestsObservable extends Observable<RequestsObserver>{

    public RequestsObservable(){
        super();
    }

    public void notifyRequestID(){
        for(RequestsObserver obs:observers){
            obs.updateRequestID();
        }
    }

    public void notifyRequestNumPlayers(){
        for(RequestsObserver obs:observers){
            obs.updateRequestID();
        }
    }

}
