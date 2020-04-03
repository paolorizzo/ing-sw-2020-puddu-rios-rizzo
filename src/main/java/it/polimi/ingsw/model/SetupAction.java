package it.polimi.ingsw.model;

public class SetupAction extends Action{

    public SetupAction(String workerID, int targetX, int targetY) {
        super(workerID, targetX, targetY);
    }

    @Override
    public String toString() {
        return "SetupAction{" +
                "workerID='" + workerID + '\'' +
                ", targetX=" + targetX +
                ", targetY=" + targetY +
                '}';
    }

}
