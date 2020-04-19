package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.observation.*;

public abstract class View implements ModelObserver
{
    ControllerInterface controller;

    public View(){
    }

    public View(ControllerInterface controller){
        this();
        setController(controller);
    }

    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    public ControllerInterface getController(){
        return controller;
    }

    /*
    public abstract void startNameView();
    public abstract void startNumberOfPlayersView();
    public abstract void startOutOfGameView();

     */
}
