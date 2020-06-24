package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.observation.*;

/**
 * Superclass for all the types of view, which implements the basic logic
 * of the view having a controller
 */
public abstract class View implements FeedObserver
{
    ControllerInterface controller;

    /**
     * base empty constructor
     */
    public View(){
    }

    /**
     * constructor that sets the controller on the view
     * @param controller the controller of the view
     */
    public View(ControllerInterface controller){
        this();
        setController(controller);
    }

    /**
     * setter for the controller
     * @param controller the controller of the view
     */
    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    /**
     * getter for the controller
     * @return the controller of the view
     */
    public ControllerInterface getController(){
        return controller;
    }

    /**
     * handles the loss of connection
     */
    public abstract void connectionLost();


}
