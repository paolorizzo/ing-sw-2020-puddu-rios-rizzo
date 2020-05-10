package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class HypnusPower extends PowerStrategy {
    public void pruneOtherActionTree(Board board, Player myself, Player other, Turn myLastTurn, ActionTree otherActionTree){
        Worker m = other.getWorker(Sex.MALE);
        Worker f = other.getWorker(Sex.FEMALE);
        if(m.getSpace().getLevel() > f.getSpace().getLevel()){
            //male worker can't move
            prune(otherActionTree, m);
        }else if(f.getSpace().getLevel() > m.getSpace().getLevel()){
            //female worker can't move
            prune(otherActionTree, f);

        }
    }


    public void prune(ActionTree root, Worker blockedWorker){

        List<ActionTree> childrenToRemove = new ArrayList<>();
        for(ActionTree child: root.getChildren()){
            if(child.getAction().getWorkerID().equals(blockedWorker.toString())){
                //prune branch
                childrenToRemove.add(child);
            }
        }
        for(ActionTree child: childrenToRemove){
            root.removeChild(child);
        }
        if(!root.isWin() && root.getChildren().size()==0){
            // lose
            root.setLose(true);
            root.setEndOfTurn(true);
        }
    }
}
