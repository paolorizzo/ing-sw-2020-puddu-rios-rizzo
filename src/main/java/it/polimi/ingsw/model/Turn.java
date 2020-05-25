package it.polimi.ingsw.model;
import it.polimi.ingsw.exception.AlreadyFullException;

import java.util.ArrayList;

//Turn currently implements no logic to check whether the actions that make it up have any coherence
//this task is deferred to the controller, which should check for coherence when taking user input.
//This check is supposed to happen in the VALIDATE_SELECT and VALIDATE_ACTION states of its FSM

/**
 * aggregates multiple actions that belong to the same player, representing the more complex idea of turn
 */
public class Turn{
    ArrayList<Action> actions;
    Player player;

    /**
     * constructs an empty turn
     * @param player the player that plays the turn
     */
    public Turn(Player player){
        this.player = player;
        actions=new ArrayList();
    }

    /**
     * constructs a turn containing its first action
     * @param player the player that plays this turn
     * @param firstAction the first action taken in this turn
     */
    public Turn(Player player, Action firstAction){
        this(player);
        actions.add(firstAction);
    }

    //adds action and checks that no more than 3 actions are added
    //does not accept null arguments
    /**
     * adds an action to the turn, if it passes basic non-semantic checks
     * @param action the action to add to the turn
     * @throws AlreadyFullException if the turn already contains 3 or more actions
     * @throws NullPointerException if the action for which the add is requested is null
     */
    public void add(Action action) throws AlreadyFullException, NullPointerException{
        if(actions.size()>=3)
            throw new AlreadyFullException("This turn contains 3 actions. You cannot add " + action.toString());
        else if(action==null)
            throw new NullPointerException("You cannot add a null action to a turn");
        else{
            actions.add(action);
        }
    }

    /**
     * returns the player that plays this turn
     * @return the player that plays this turn
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * returns the amount of actions logged in the turn
     * @return the amount of actions logged in the turn
     */
    public int size(){
        return actions.size();
    }

    /**
     * returns true if a given action has been taken in the turn
     * @param action the action to check
     * @return whether the action has already been taken
     */
    public boolean contains(Action action){
        return actions.contains(action);
    }

    /**
     * returns the actions taken in the turn as a list
     * @return the actions taken in the turn
     */
    public ArrayList<Action> getActions(){
        return actions;
    }

    /**
     * checks whether this turn has any actions
     * @return true if the turn has no actions as of yet
     */
    public boolean isEmpty(){
        return actions.isEmpty();
    }

    /**
     * builds a string that represents the turn by concatenating the strings representing the actions
     * @return a string that represents the turn
     */
    @Override
    public String toString(){
        String s = "";
        for(Action a:actions){
            s += a.toString() + " ";
        }
        s += "\n";
        return s;
    }
}


