package it.polimi.ingsw.model.power;

import it.polimi.ingsw.exception.InvalidActionTreeGenerationException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class PowerStrategy {
    /**
     * Default method to generate an action tree from the board using the player.
     * In default turn there is a first layer of move and the second of build.
     * @param board the current game board
     * @param player the player requesting the action tree
     * @return the action tree generated
     */
    public ActionTree generateActionTree(Board board, Player player){
        ActionTree root = new ActionTree();
        this.addMoveLayer(root, player, board);
        this.addBuildLayer(root, player, board);
        return root;
    }

    /**
     * calls the defaults private recursive method to construct a move layer
     * @param curr the current action tree
     * @param player the player requesting the action tree
     * @param board the current game board
     */

    protected void addMoveLayer(ActionTree curr, Player player, Board board){
        recursiveAddMoveLayer(curr, player, board);
    }
    /**
     * calls the default private recursive method to construct a build layer
     * @param curr the current action tree
     * @param player the player requesting the action tree
     * @param board the current game board
     */

    protected void addBuildLayer(ActionTree curr, Player player, Board board){
        recursiveAddBuildLayer(curr, player, board);
    }

    /**
     * This method is private to avoid to be called by subclasses and so to avoid problem of recursion by static binding.
     * It adds a child to the root of tree for every move that each worker can do.
     * The children created have the flag of appended layer true for allowing the next layers to append on its.
     * If worker go from a level 2 to a level 3, so the default win condition, the child of this move will have the flag of win true.
     * If there are no action to do starting from the root, this will have the flag of lose true.
     * @param curr the current action tree
     * @param player the player requesting the action tree
     * @param board the current game board
     */
    private void recursiveAddMoveLayer(ActionTree curr, Player player, Board board){
        if(!curr.isRoot())
            throw new InvalidActionTreeGenerationException("Default move: before move there is always root");

        if(curr.isAppendedLayer()){
            curr.setAppendedLayer(false);

            List<Worker> workers = new ArrayList<Worker>();

            //scelta libera del worker
            workers.add(player.getWorker(Sex.MALE));
            workers.add(player.getWorker(Sex.FEMALE));

            boolean moved = false;
            for(Worker worker: workers){
                Space currSpace = worker.getSpace();
                for(Space adj: currSpace.getAdjacentSpaces()){
                    if(adj.isFreeSpace() && adj.getLevel()<=currSpace.getLevel()+1){
                        boolean win = false, endOfTurn = false;
                        //winning condition
                        if(currSpace.getLevel() == 2 && adj.getLevel() == 3){
                            win = true;
                            endOfTurn = true;
                        }
                        Direction dir = Direction.compareTwoLevels(currSpace.getLevel(), adj.getLevel());
                        Action action = new MoveAction(worker.toString(), adj.getPosX(), adj.getPosY(), dir, currSpace.getPosX(), currSpace.getPosY());
                        moved = true;
                        curr.addChild(new ActionTree(action, win, false, endOfTurn, true));
                    }
                }
            }
            if(curr.isRoot() && !moved){
                //can't move with the workers
                curr.setLose(true);
                curr.setEndOfTurn(true);
            }
        }
    }
    /**
     * This method is private to avoid to be called by subclasses and so to avoid problem of recursion by static binding.
     * It simulates the previous actions on the board until the node of the tree is an appended layer, and so adds a child for every build that the worker of the current node can do.
     * The children created have the flag of appended layer true for allowing the next layers to append on its.
     * If there are no action to do starting from the current node of tree and doesn't have the flag win true, this will have the flag of lose true.
     * When all children have added to the leaf and the method return, all action performed on the board will be undo.
     * @param curr the current action tree
     * @param player the player requesting the action tree
     * @param board the current game board
     */
    private void recursiveAddBuildLayer(ActionTree curr, Player player, Board board){
        //inizialmente simulo le mosse
        if(!curr.isRoot()){
            //simulate
            board.executeAction(curr.getAction());
        }
        //parto dai nodi più in basso
        for(ActionTree child: curr.getChildren())
            this.recursiveAddBuildLayer(child, player, board);

        if(curr.isAppendedLayer()){
            curr.setAppendedLayer(false);

            List<Worker> workers = new ArrayList<Worker>();

            String workerID = curr.getAction().getWorkerID();
            if(workerID.charAt(workerID.length()-1) == 'M')
                workers.add(player.getWorker(Sex.MALE));
            else if(workerID.charAt((workerID.length()-1)) == 'F')
                workers.add(player.getWorker(Sex.FEMALE));

            boolean built = false;
            for(Worker worker: workers){
                Space currSpace = worker.getSpace();
                for(Space adj: currSpace.getAdjacentSpaces()){
                    if(adj.isFreeSpace() && board.getPieceBag().hasPiece(adj.getLastPiece().nextPiece())){
                        built = true;
                        Action action = new BuildAction(worker.toString(), adj.getPosX(), adj.getPosY(), adj.getLastPiece().nextPiece());
                        curr.addChild(new ActionTree(action, false, false, true, true));
                    }
                }
            }
            if(!built && !curr.isRoot() && !curr.isWin()){
                //can't built with the worker selected
                // lose
                curr.setLose(true);
                curr.setEndOfTurn(true);
            }
        }

        //undo simulazione azione
        if(!curr.isRoot()){
            board.undoExecuteAction(curr.getAction());
        }
    }

    /**
     * This is method is invoke to prune the action tree generate by the other player that actually play the turn to erase the action
     * that this power negate.
     * In default the power doesn't negate action. This method is override in subclasses power that have power in the opponent turn.
     * @param board the current game board
     * @param myself that player that use the power
     * @param other the opponent player that play the actual turn
     * @param myLastTurn the last turn played by myself player
     * @param otherActionTree the action tree generate by opponent player
     */
    public void pruneOtherActionTree(Board board, Player myself, Player other, Turn myLastTurn, ActionTree otherActionTree){
        return;
    }

}
