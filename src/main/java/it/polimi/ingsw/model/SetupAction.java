package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 * represents the action of placing a worker on  the board for the first time,
 * as it happens during the setup of the game
 */
public class SetupAction extends Action{

    /**
     * constructs a complete SetupAction
     * @param workerID a string that univocally represents the worker
     * @param targetX the x coordinate of the space on which the worker is placed
     * @param targetY the x coordinate of the space on which the worker is placed
     */
    public SetupAction(String workerID, int targetX, int targetY) {
        super(workerID, targetX, targetY);
    }

    /**
     * constructs a SetupAction by decorating an action
     * In this case, the only decoration is the specification of the class Action into the subclass SetupAction
     * @param action the action to decorate
     */
    public SetupAction(Action action){
        super(action.workerID, action.targetX, action.targetY);
    }

    /**
     * returns true if given the coordinates of the target space of the action
     * @param x the x coordinate of the inquiry
     * @param y the y coordinate of the inquiry
     * @return true if the given coordinates match the target space of the action
     */
    public boolean matches(int x, int y){
        return targetX==x && targetY==y;
    }

    /**
     * this method will be called through a simulated overriding in Action
     * builds a SetupAction object from a map containing the necessary info, and the necessary type
     * @param map the map to convert into a SetupAction object
     * @return a SetupAction object as described in the map
     */
    static public SetupAction fromMap(Map<String, Object> map){
        checkTypeCorrectness(map, SetupAction.class);
        Action action = Action.parseMap(map);
        return new SetupAction(action);
    }

    /**
     * returns a string that represents this action
     * @return a string that represents this action
     */
    @Override
    public String toString() {
        return "SetupAction{" +
                "workerID='" + workerID + '\'' +
                ", targetX=" + targetX +
                ", targetY=" + targetY +
                '}';
    }

}
