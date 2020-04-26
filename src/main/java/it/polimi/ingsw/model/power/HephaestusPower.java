package it.polimi.ingsw.model.power;

import it.polimi.ingsw.exception.InvalidActionTreeGenerationException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class HephaestusPower extends PowerStrategy {

    public ActionTree generateActionTree(Board board, Player player){
        ActionTree root = new ActionTree();

        super.addMoveLayer(root, player, board);
        super.addBuildLayer(root, player, board);
        this.addBuildLayer(root, player, board);

        return root;
    }

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
            if(curr.isRoot()) {
                throw new InvalidActionTreeGenerationException("Demeter: can't build first");
            }else{
                String workerID = curr.getAction().getWorkerID();
                if(workerID.charAt(workerID.length()-1) == 'M')
                    workers.add(player.getWorker(Sex.MALE));
                else if(workerID.charAt((workerID.length()-1)) == 'F')
                    workers.add(player.getWorker(Sex.FEMALE));
            }
            if(curr.getAction() instanceof BuildAction){
                int targetX = curr.getAction().getTargetX(), targetY = curr.getAction().getTargetY();
                for(Worker worker: workers){
                    Space currSpace = worker.getSpace();
                    for(Space adj: currSpace.getAdjacentSpaces()){
                        //can double build on the same old target
                        if(adj.isFreeSpace() && board.getPieceBag().hasPiece(adj.getLastPiece().nextPiece()) && adj == board.getSpaces()[targetX][targetY]){
                            Action action = new BuildAction(worker.toString(), adj.getPosX(), adj.getPosY(), adj.getLastPiece().nextPiece());
                            curr.addChild(new ActionTree(action, false, false, true, true));
                        }
                    }
                }
            }

        }

        //undo simulazione azione
        if(!curr.isRoot()){
            board.undoExecuteAction(curr.getAction());
        }
    }

}
