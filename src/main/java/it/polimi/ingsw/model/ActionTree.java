package it.polimi.ingsw.model;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class ActionTree {
    private boolean win;
    private boolean lose;
    private boolean endOfTurn;
    private boolean appendedLayer;
    private boolean root;
    private Vector<ActionTree> children;
    private Action action;

    public ActionTree() {
        //build a root of tree
        this.root = true;
        this.win = false;
        this.lose = false;
        this.endOfTurn = false;
        this.appendedLayer = true;
        this.children = new Vector<>();
        this.action = null;
    }
    public ActionTree(Action action, boolean win, boolean lose, boolean endOfTurn, boolean appendedLayer){
        this.root = false;
        this.win = win;
        this.lose = lose;
        this.endOfTurn = endOfTurn;
        this.appendedLayer = appendedLayer;
        this.children = new Vector<>();
        this.action = action;
    }
    public boolean isWin() {
        return win;
    }

    public boolean isLose() {
        return lose;
    }

    public boolean isEndOfTurn() {
        return endOfTurn;
    }

    public void setLose(boolean lose){
        this.lose = lose;
    }

    public void setEndOfTurn(boolean endOfTurn) {
        this.endOfTurn = endOfTurn;
    }

    public Action getAction() {
        return action;
    }

    public boolean isAppendedLayer() {
        return appendedLayer;
    }

    public void setAppendedLayer(boolean appendLayer) {
        this.appendedLayer = appendLayer;
    }

    public Vector<ActionTree> getChildren(){
        return children;
    }

    public void addChild(ActionTree child){
        children.add(child);
    }

    public void removeChild(ActionTree child) {
        children.remove(child);
    }
    public boolean isRoot() {
        return root;
    }

    public boolean isPathPresent(List<Action> actions){
        return isPathPresent(actions, 0);
    }
    private boolean isPathPresent(List<Action> actions, int pos){
        //actions list of Action
        //pos count on actions
        if(this.endOfTurn && actions.size() == pos){
            return true;
        }
        if(children.size()==0)
            return false;
        for(ActionTree child: children){
            if(actions.get(pos).equals(child.getAction()))
                return child.isPathPresent(actions, pos+1);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionTree)) return false;
        ActionTree that = (ActionTree) o;
        return win == that.win &&
                lose == that.lose &&
                endOfTurn == that.endOfTurn &&
                appendedLayer == that.appendedLayer &&
                root == that.root &&
                children.equals(that.children) &&
                Objects.equals(action, that.action);
    }


}
