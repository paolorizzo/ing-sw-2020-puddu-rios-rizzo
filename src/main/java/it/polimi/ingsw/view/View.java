package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.observation.*;

public abstract class View implements FeedObserver
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


}
