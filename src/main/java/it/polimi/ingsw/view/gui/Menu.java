package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observation.UserInterfaceObservable;
import javafx.scene.Group;

public class Menu extends UserInterfaceObservable {
    protected Group group;

    public Menu(){
        group = new Group();
        this.hide();
    }
    public Group getGroup(){
        return group;
    }
    public void show(){
        group.setVisible(true);
    }
    public void hide(){
        group.setVisible(false);
    }
}
