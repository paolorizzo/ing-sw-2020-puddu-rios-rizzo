package it.polimi.ingsw.observation;

import java.util.ArrayList;
import java.util.List;

public class RequestsObservable extends Observable<RequestsObserver>{

    public RequestsObservable(){
        super();
    }

    public synchronized void notifyRequestID(){
        System.out.println("notifyingRequestID");
        for(RequestsObserver obs:observers){
            obs.updateRequestID();
        }
    }

    public synchronized void notifyAckID(int id)
    {
        for(RequestsObserver obs:observers){
            obs.updateAckID(id);
        }
    }

    public synchronized void notifyRequestNumPlayers(){
        for(RequestsObserver obs:observers){
            obs.updateRequestID();
        }
    }

}
