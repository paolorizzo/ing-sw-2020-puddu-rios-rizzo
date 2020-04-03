package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class PowerStrategy {

    protected ActionTree generateActionTree(Board board, Player player){
        ActionTree root = new ActionTree();
        this.addMoveLayer(root, player, board, null);
        this.addBuildLayer(root, player, board, null);
        return root;
    }


    protected void addMoveLayer(ActionTree curr, Player player, Board board, String workerID){
        //inizialmente simulo le mosse
        if(!curr.isRoot()){
            //simulate
            board.executeAction(curr.getAction());
            workerID = curr.getAction().getWorkerID();
        }
        //parto dai nodi più in basso
        for(ActionTree child: curr.getChildren())
            addMoveLayer(child, player, board, workerID);

        if(curr.isAppendedLayer()){
            curr.setAppendedLayer(false);

            List<Worker> workers = new ArrayList<Worker>();
            if(workerID == null) {
                //scelta libera del worker
                workers.add(player.getWorker(Sex.MALE));
                workers.add(player.getWorker(Sex.FEMALE));
            }else{
                if(workerID.charAt(workerID.length()-1) == 'M')
                    workers.add(player.getWorker(Sex.MALE));
                else if(workerID.charAt((workerID.length()-1)) == 'F')
                    workers.add(player.getWorker(Sex.FEMALE));
            }
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
                        curr.addChild(new ActionTree(action, win, false, endOfTurn, true));
                    }
                }
            }
        }

        //undo simulazione azione
        if(!curr.isRoot()){
            board.undoExecuteAction(curr.getAction());
        }
    }

    protected void addBuildLayer(ActionTree curr, Player player, Board board, String workerID){
        //inizialmente simulo le mosse
        if(!curr.isRoot()){
            //simulate
            board.executeAction(curr.getAction());
            workerID = curr.getAction().getWorkerID();
        }
        //parto dai nodi più in basso
        for(ActionTree child: curr.getChildren())
            addBuildLayer(child, player, board, workerID);

        if(curr.isAppendedLayer()){
            curr.setAppendedLayer(false);

            List<Worker> workers = new ArrayList<Worker>();
            if(workerID == null) {
                //scelta libera del worker
                workers.add(player.getWorker(Sex.MALE));
                workers.add(player.getWorker(Sex.FEMALE));
            }else{
                if(workerID.charAt(workerID.length()-1) == 'M')
                    workers.add(player.getWorker(Sex.MALE));
                else if(workerID.charAt((workerID.length()-1)) == 'F')
                    workers.add(player.getWorker(Sex.FEMALE));
            }
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
            if(!built && !curr.isRoot()){
                //can't built with the worker selected
                // lose
                curr.setLose(true);
            }
        }

        //undo simulazione azione
        if(!curr.isRoot()){
            board.undoExecuteAction(curr.getAction());
        }
    }

    //nothing to prune in base case
    protected boolean requirePruning(){
        return false;
    }
    //nothing change in base case
    protected void pruneActionTree(ActionTree root){
        return;
    }

}
