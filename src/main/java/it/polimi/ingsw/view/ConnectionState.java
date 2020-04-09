package it.polimi.ingsw.view;

public enum ConnectionState {

    REQUEST_ID,
    READ_ID,
    REQUEST_NUM_PLAYERS,
    READ_NUM_PLAYERS,
    PUBLISH_NUM_PLAYERS,
    PUBLISH_NAME,
    END;


    //default implementation of next, returns the next enum instance in order, or null if the FSM has terminated
    public ConnectionState next(){
        if(ordinal()==ConnectionState.values().length-1)
            return null;
        else
            return ConnectionState.values()[ this.ordinal()+1 ];
    }


    }
