package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class ArtemisPower extends PowerStrategy {

    protected ActionTree generateActionTree(Board board, Player player){
        ActionTree root = new ActionTree();
        super.addMoveLayer(root, player, board);
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
            addMoveLayer(child, player, board);

        if(curr.isAppendedLayer()){

            List<Worker> workers = new ArrayList<Worker>();

            String workerID = curr.getAction().getWorkerID();
            if(workerID.charAt(workerID.length()-1) == 'M')
                workers.add(player.getWorker(Sex.MALE));
            else if(workerID.charAt((workerID.length()-1)) == 'F')
                workers.add(player.getWorker(Sex.FEMALE));
            int startX = ((MoveAction)curr.getAction()).getStartX();
            int startY = ((MoveAction)curr.getAction()).getStartY();
            for(Worker worker: workers){
                Space currSpace = worker.getSpace();
                for(Space adj: currSpace.getAdjacentSpaces()){
                    if(adj.isFreeSpace() && adj.getLevel()<=currSpace.getLevel()+1 && (startX != adj.getPosX() || startY != adj.getPosY())){
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
}