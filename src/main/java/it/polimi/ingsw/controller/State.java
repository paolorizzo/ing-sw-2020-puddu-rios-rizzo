package it.polimi.ingsw.controller;

public enum State {
    PLAYER_SETUP,
    GET_ACTION_TREE,
    DISPLAY_SELECT_VIEW,
    //checks correctness of the worker selection received through the view
    VALIDATE_SELECT,
    //memorizes the worker selected
    ELABORATE_SELECT,
    DISPLAY_ACTION_VIEW,
    //checks correctness of the action received through the view, and checks win and loss
    VALIDATE_ACTION,
    //updates the model
    ELABORATE_ACTION,
    UPDATE_TURN_ARCHIVE;


    //standard implementation of the next state function
    //returns the state immediately after the caller
    //the last state returns the first state
    State next(){
        return State.values()[ (this.ordinal()+1) % (State.values().length) ];
    }
}
