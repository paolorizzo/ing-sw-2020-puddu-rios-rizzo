package it.polimi.ingsw.model.power;

import it.polimi.ingsw.exception.InvalidActionTreeGenerationException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class MinotaurPower extends PowerStrategy {

    public ActionTree generateActionTree(Board board, Player player){
        ActionTree root = new ActionTree();
        this.addMoveLayer(root, player, board);
        super.addBuildLayer(root, player, board);
        return root;
    }


    protected void addMoveLayer(ActionTree curr, Player player, Board board){
        //inizialmente simulo le mosse
        if(!curr.isRoot())
            throw new InvalidActionTreeGenerationException("Minotaur: before move there is always root");

        curr.setAppendedLayer(false);

        List<Worker> workers = new ArrayList<Worker>();

        //scelta libera del worker
        workers.add(player.getWorker(Sex.MALE));
        workers.add(player.getWorker(Sex.FEMALE));

        boolean moved = false;
        for(Worker worker: workers){
            Space currSpace = worker.getSpace();
            for(Space adj: currSpace.getAdjacentSpaces()){
                if(adj.getLevel()<=3 && adj.getLevel()<=currSpace.getLevel()+1 && canMoveAndForce(board, currSpace, adj, player)){
                    boolean win = false, endOfTurn = false;
                    //winning condition
                    if(currSpace.getLevel() == 2 && adj.getLevel() == 3){
                        win = true;
                        endOfTurn = true;
                    }
                    Direction dir = Direction.compareTwoLevels(currSpace.getLevel(), adj.getLevel());
                    Action action;
                    if(adj.hasWorkerOnIt())
                        action = new MoveAndForceAction(worker.toString(), adj.getPosX(), adj.getPosY(), dir, currSpace.getPosX(), currSpace.getPosY(), adj.getWorkerOnIt().toString(), adj.getPosX(), adj.getPosY(), 2*adj.getPosX()-currSpace.getPosX(), 2*adj.getPosY()-currSpace.getPosY());
                    else
                        action = new MoveAction(worker.toString(), adj.getPosX(), adj.getPosY(), dir, currSpace.getPosX(), currSpace.getPosY());
                    moved = true;
                    curr.addChild(new ActionTree(action, win, false, endOfTurn, true));
                }
            }
        }
        if(!moved){
            //can't move with the workers
            curr.setLose(true);
            curr.setEndOfTurn(true);
        }
    }

    private boolean canMoveAndForce(Board board, Space curr, Space adj, Player player){
        return (!adj.hasWorkerOnIt() || (adj.hasWorkerOnIt() && 2*adj.getPosX()-curr.getPosX()<5 && 0<=2*adj.getPosX()-curr.getPosX() && 2*adj.getPosY()-curr.getPosY()<5 && 0<=2*adj.getPosY()-curr.getPosY() && board.getSpaces()[2*adj.getPosX()-curr.getPosX()][2*adj.getPosY()-curr.getPosY()].isFreeSpace() && adj.getWorkerOnIt() != player.getWorker(Sex.MALE) && adj.getWorkerOnIt() != player.getWorker(Sex.FEMALE)));
    }

}
