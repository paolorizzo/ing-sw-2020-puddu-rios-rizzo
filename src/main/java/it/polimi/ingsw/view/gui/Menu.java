package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observation.UserInterfaceObservable;
import javafx.scene.Group;

public class Menu extends UserInterfaceObservable {
    protected Group group;
    protected int widthResolution = 1200, heightResolution = 700;
    public Menu(int wr, int hr){
        widthResolution = wr;
        heightResolution = hr;
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
