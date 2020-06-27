package it.polimi.ingsw.model;
import it.polimi.ingsw.exception.AlreadyFullException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * aggregates multiple actions that belong to the same player, representing the more complex idea of turn
 */
public class Turn extends MapConvertible{
    ArrayList<Action> actions;
    int playerId;

    /**
     * constructs an empty turn
     * @param player the player that plays the turn
     */
    public Turn(Player player){
        this.playerId = player.getId();
        actions=new ArrayList();
    }

    /**
     * constructs an empty turn
     * @param id the id of the player that plays the turn
     */
    public Turn(int id){
        this.playerId = id;
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
    public int getPlayerId() {
        return playerId;
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

    /**
     * compares this Turn to another Object
     * @param o the other object
     * @return true if the other object is a Turn, and their toStrings match
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Turn)) return false;
        Turn that = (Turn) o;
        return this.toString().equals(that.toString());
    }

    /**
     * puts into the map all the info regarding the action
     * @param map the map in which to put the info
     */
    public void putEntries(Map<String, Object> map){
        super.putEntries(map);;
        map.put("playerId", this.playerId);
        int size = this.actions.size();
        map.put("size", size);
        for(int i=0;i<size;i++){
            map.put(String.valueOf(i), this.actions.get(i).toMap());
        }
    }

    /**
     * converts a map into a valid Turn object
     * @param map the map to convert
     * @return a Turn object as described in the map
     */
    static public Turn fromMap(Map<String, Object> map){
        checkTypeCorrectness(map, Turn.class);
        Turn turn = new Turn(getInt(map, "playerId"));
        int size = getInt(map, "size");
        for(int i=0;i<size;i++){
            Map<String, Object> actionMap = (Map<String, Object>) map.get(String.valueOf(i));
            Action action = Action.fromMap(actionMap);
            turn.add(action);
        }
        return turn;
    }
}


