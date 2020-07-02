package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  Generate ActionTree and prune others ActionTrees for god Zeus
 */
public class ZeusPower extends PowerStrategy {
    /**
     * It uses an overrider build layer instead of default to generate the action tree
     * @param board the current game board
     * @param player the player requesting the action tree
     * @return the action tree generated by the god Atlas
     */
    public ActionTree generateActionTree(Board board, Player player){
        ActionTree root = new ActionTree();
        super.addMoveLayer(root, player, board);
        this.addBuildLayer(root, player, board);
        return root;
    }

    /**
     * It adds the possibility to build under the worker.
     * @param curr the current action tree
     * @param player the player requesting the action tree
     * @param board the current game board
     */
    protected void addBuildLayer(ActionTree curr, Player player, Board board){
        //inizialmente simulo le mosse
        if(!curr.isRoot()){
            //simulate
            board.executeAction(curr.getAction());
        }
        //parto dai nodi più in basso
        for(ActionTree child: curr.getChildren())
            addBuildLayer(child, player, board);

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
                    if(adj.isFreeSpace()){

                        if(board.getPieceBag().hasPiece(adj.getLastPiece().nextPiece())){
                            built = true;
                            Action action = new BuildAction(worker.toString(), adj.getPosX(), adj.getPosY(), adj.getLastPiece().nextPiece());
                            curr.addChild(new ActionTree(action, false, false, true, true));
                        }
                    }
                }
                //here zeus power, build under worker
                if(currSpace.getLevel()<3 && board.getPieceBag().hasPiece(currSpace.getLastPiece().nextPiece())){
                    built = true;
                    Action action = new BuildAction(worker.toString(), currSpace.getPosX(), currSpace.getPosY(), currSpace.getLastPiece().nextPiece());
                    curr.addChild(new ActionTree(action, false, false, true, true));
                }
                if(!built && !curr.isWin()){
                    //can't built with the worker selected
                    // lose
                    curr.setLose(true);
                    curr.setEndOfTurn(true);
                }
            }
        }

        //undo simulazione azione
        if(!curr.isRoot()){
            board.undoExecuteAction(curr.getAction());
        }
    }

}
