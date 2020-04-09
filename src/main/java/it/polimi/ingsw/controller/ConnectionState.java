package it.polimi.ingsw.controller;

public enum ConnectionState {
    MEMORIZE_VIEW{

    },
    DISPLAY_NUM_PLAYERS_VIEW,
    VALIDATE_NUM_PLAYERS,
    ELABORATE_NUM_PLAYERS,
    DISPLAY_NAME_VIEW,
    VALIDATE_NAME,
    ELABORATE_NAME,
    CREATE_PLAYER,
    DISPLAY_CONNECTION_WAIT_VIEW;

    ConnectionState next(){
        if(this.ordinal()==ConnectionState.values().length-1)
            return null;
        else
            return ConnectionState.values()[this.ordinal()+1];
    }
}
