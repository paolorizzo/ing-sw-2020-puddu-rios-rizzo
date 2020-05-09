package it.polimi.ingsw.model.power;

import it.polimi.ingsw.exception.InvalidActionTreeGenerationException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class PowerStrategy {

    public ActionTree generateActionTree(Board board, Player player){
        ActionTree root = new ActionTree();
        this.addMoveLayer(root, player, board);
        this.addBuildLayer(root, player, board);
        return root;
    }
    protected void addMoveLayer(ActionTree curr, Player player, Board board){
        recursiveAddMoveLayer(curr, player, board);
    }
    protected void addBuildLayer(ActionTree curr, Player player, Board board){
        recursiveAddBuildLayer(curr, player, board);
    }
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

    public void pruneOtherActionTree(Board board, Player myself, Player other, Turn myLastTurn, ActionTree otherActionTree){
        return;
    }

}
