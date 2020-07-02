package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observation.UserInterfaceObservable;
import javafx.scene.Group;

/**
 * It is a super class for all menu. It extends the UserInterfaceObservable to notify the action on GUI to the ClientView.
 */
public class Menu extends UserInterfaceObservable {
    protected Group group;
    protected int widthResolution, heightResolution;

    /**
     * It's a standard constructor for all menu
     * @param wr the width resolution of window
     * @param hr the height resolution of window
     */
    public Menu(int wr, int hr){
        widthResolution = wr;
        heightResolution = hr;
        group = new Group();
        this.hide();
    }

    /**
     *
     * @return the group of the menu
     */
    public Group getGroup(){
        return group;
    }

    /**
     * It sets visible the group
     */
    public void show(){
        group.setVisible(true);
    }

    /**
     * It sets hidden the group
     */
    public void hide(){
        group.setVisible(false);
    }
}
