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

    public synchronized void notifyAckID()
    {
        for(RequestsObserver obs:observers){
            obs.updateAckID();
        }
    }

    public synchronized void notifyRequestNumPlayers(){
        for(RequestsObserver obs:observers){
            obs.updateRequestID();
        }
    }

}
