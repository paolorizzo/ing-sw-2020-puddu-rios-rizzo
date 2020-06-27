package it.polimi.ingsw.model;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * A tree of Actions. This data structure is core to the model package,
 * and allows a simple representation of the actions that are allowed for a player.
 * This is especially useful to implement the god powers, since every special behaviour
 * can be broken down to a modification of a player's ActionTree
 */
public class ActionTree {
    private boolean win;
    private boolean lose;
    private boolean endOfTurn;
    private boolean appendedLayer;
    private boolean root;
    private Vector<ActionTree> children;
    private Action action;

    /**
     * constructs the root node of the ActionTree
     */
    public ActionTree() {
        this.root = true;
        this.win = false;
        this.lose = false;
        this.endOfTurn = false;
        this.appendedLayer = true;
        this.children = new Vector<>();
        this.action = null;
    }

    /**
     * constructs a child node of the ActionTree
     * @param action the action in the node
     * @param win flag representing whether this is a winning action
     * @param lose flag representing whether this is a losing action
     * @param endOfTurn flag representing whether a turn can be ended on this action
     * @param appendedLayer flag representing whether this node can have children at this time
     */
    public ActionTree(Action action, boolean win, boolean lose, boolean endOfTurn, boolean appendedLayer){
        this.root = false;
        this.win = win;
        this.lose = lose;
        this.endOfTurn = endOfTurn;
        this.appendedLayer = appendedLayer;
        this.children = new Vector<>();
        this.action = action;
    }

    /**
     * returns true if this is a winning action
     * @return true if this is a winning action
     */
    public boolean isWin() {
        return win;
    }

    /**
     * returns true if this is a losing action
     * @return true if this is a losing action
     */
    public boolean isLose() {
        return lose;
    }

    /**
     * returns true if a turn can end on this action
     * @return true if a turn can end on this action
     */
    public boolean isEndOfTurn() {
        return endOfTurn;
    }

    /**
     * sets whether this node is a winning node
     * @param win flag representing whether this node is a winning node
     */
    public void setWin(boolean win){
        this.win = win;
    }

    /**
     * sets whether this node is a losing node
     * @param lose flag representing whether this node is a losing node
     */
    public void setLose(boolean lose){
        this.lose = lose;
    }

    /**
     * sets whether a turn can end on this node
     * @param endOfTurn flag representing whether a turn can end on this node
     */
    public void setEndOfTurn(boolean endOfTurn) {
        this.endOfTurn = endOfTurn;
    }

    /**
     * returns the action in this node
     * @return the action in this node
     */
    public Action getAction() {
        return action;
    }

    /**
     * returns true if this node can have children at this time
     * @return true if this node can have children at this time
     */
    public boolean isAppendedLayer() {
        return appendedLayer;
    }

    /**
     * sets whether this node can have children at this time
     * @param appendLayer flag representing whether this node can have children at this time
     */
    public void setAppendedLayer(boolean appendLayer) {
        this.appendedLayer = appendLayer;
    }

    /**
     * returns the children of this node in the tree as a vector
     * @return the children of this node in the tree as a vector
     */
    public Vector<ActionTree> getChildren(){
        return children;
    }

    /**
     * adds a child node to this node
     * @param child the child node to add to this node
     */
    public void addChild(ActionTree child){
        children.add(child);
    }

    /**
     * removes a child node from this node
     * @param child the child node to be removed
     */
    public void removeChild(ActionTree child) {
        children.remove(child);
    }

    /**
     * returns true if this node is the root of the tree
     * @return a flag representing whether this node is the root of the tree
     */
    public boolean isRoot() {
        return root;
    }

    /**
     * checks whether the tree contains a branch composed
     * of a given list of actions
     * @param actions a list of actions
     * @return true if the tree contains a branch composed of all
     * the actions in the parameter actions, in that order
     */
    public boolean isPathPresent(List<Action> actions){
        return isPathPresent(actions, 0);
    }

    /**
     * checks whether this node contains a subtree made up of a given
     * sublist of actions. This sublist is the tail of the parameter
     * actions, starting from its position pos
     * @param actions the list of actions that we're searching for
     * @param pos the position that we're currently checking in this list
     * @return true if this node contains a subtree made of the
     * specified sublist of actions
     */
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

    /**
     * compares this to an object and returns true if this node's state
     * is identical to the other node's state
     * @param o the object against which to compare this
     * @return true if o is an ActionTree node and its state
     * is identical to this node's state
     */
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
