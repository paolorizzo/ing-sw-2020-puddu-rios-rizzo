package it.polimi.ingsw.model;
import java.util.ArrayList;

//Turn currently implements no logic to check whether the actions that make it up have any coherence
//this task is deferred to the controller, which should check for coherence when taking user input.
//This check is supposed to happen in the VALIDATE_SELECT and VALIDATE_ACTION states of its FSM
public class Turn{
    ArrayList<Action> actions;

    public Turn(){
        actions=new ArrayList();
    }

    public Turn(Action firstAction){
        this();
        actions.add(firstAction);
    }

    //adds action and checks that no more than 3 actions are added
    //does not accept null arguments
    public void add(Action action) throws AlreadyFullException, NullPointerException{
        if(actions.size()>=3)
            throw new AlreadyFullException("This turn contains 3 actions. You cannot add " + action.toString());
        else if(action==null)
            throw new NullPointerException("You cannot add a null action to a turn");
        else{
            actions.add(action);
        }
    }

    public int size(){
        return actions.size();
    }

    public boolean contains(Action action){
        return actions.contains(action);
    }

    public ArrayList<Action> getActions(){
        return actions;
    }


    public boolean isEmpty(){
        return actions.isEmpty();
    }
}


