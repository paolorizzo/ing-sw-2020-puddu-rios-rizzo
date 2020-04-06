package it.polimi.ingsw.model.power;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.InvalidActionTreeGenerateException;

import java.util.ArrayList;
import java.util.List;

public class PrometheusPower extends PowerStrategy {
    protected ActionTree generateActionTree(Board board, Player player){
        ActionTree root = new ActionTree();
        this.addBuildLayer(root, player, board);
        this.addMoveLayer(root, player, board);
        super.addBuildLayer(root, player, board);
        return root;
    }


    protected void addMoveLayer(ActionTree curr, Player player, Board board){
        //inizialmente simulo le mosse
        if(!curr.isRoot()){
            //simulate
            board.executeAction(curr.getAction());
        }
        //parto dai nodi più in basso
        for(ActionTree child: curr.getChildren())
            this.addMoveLayer(child, player, board);

        if(curr.isAppendedLayer()) {
            curr.setAppendedLayer(false);

            List<Worker> workers = new ArrayList<Worker>();

            boolean moved = false;

            if (curr.isRoot()) {
                //scelta libera del worker
                //normale mossa muovi

                workers.add(player.getWorker(Sex.MALE));
                workers.add(player.getWorker(Sex.FEMALE));

                for (Worker worker : workers) {
                    Space currSpace = worker.getSpace();
                    for (Space adj : currSpace.getAdjacentSpaces()) {
                        if (adj.isFreeSpace() && adj.getLevel() <= currSpace.getLevel() + 1) {
                            boolean win = false, endOfTurn = false;
                            //winning condition
                            if (currSpace.getLevel() == 2 && adj.getLevel() == 3) {
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
            } else {
                if (!(curr.getAction() instanceof BuildAction))
                    throw new InvalidActionTreeGenerateException("Prometheus: not build action before move action!");
                String workerID = curr.getAction().getWorkerID();
                if (workerID.charAt(workerID.length() - 1) == 'M')
                    workers.add(player.getWorker(Sex.MALE));
                else if (workerID.charAt((workerID.length() - 1)) == 'F')
                    workers.add(player.getWorker(Sex.FEMALE));


                for (Worker worker : workers) {
                    Space currSpace = worker.getSpace();
                    for (Space adj : currSpace.getAdjacentSpaces()) {
                        //can't go up with a build action before
                        if (adj.isFreeSpace() && adj.getLevel() <= currSpace.getLevel()) {
                            boolean win = false, endOfTurn = false;
                            //no winning condition
                            Direction dir = Direction.compareTwoLevels(currSpace.getLevel(), adj.getLevel());
                            Action action = new MoveAction(worker.toString(), adj.getPosX(), adj.getPosY(), dir, currSpace.getPosX(), currSpace.getPosY());
                            moved = true;
                            curr.addChild(new ActionTree(action, win, false, endOfTurn, true));
                        }
                    }
                }

            }


            if(!curr.isWin() && !moved){
                //can't move with the workers
                curr.setLose(true);
                curr.setEndOfTurn(true);
            }
        }
        //undo simulazione azione
        if(!curr.isRoot()){
            board.undoExecuteAction(curr.getAction());
        }
    }

    protected void addBuildLayer(ActionTree curr, Player player, Board board){
        if(!curr.isRoot()){
            throw new InvalidActionTreeGenerateException("Prometheus: no root in the first build!");
        }

        if(curr.isAppendedLayer()){

            List<Worker> workers = new ArrayList<Worker>();

            workers.add(player.getWorker(Sex.MALE));
            workers.add(player.getWorker(Sex.FEMALE));

            for(Worker worker: workers){
                Space currSpace = worker.getSpace();
                for(Space adj: currSpace.getAdjacentSpaces()){
                    if(adj.isFreeSpace() && board.getPieceBag().hasPiece(adj.getLastPiece().nextPiece())){
                        Action action = new BuildAction(worker.toString(), adj.getPosX(), adj.getPosY(), adj.getLastPiece().nextPiece());
                        curr.addChild(new ActionTree(action, false, false, false, true)); //no endOfTurn
                    }
                }
            }
        }

    }

}
