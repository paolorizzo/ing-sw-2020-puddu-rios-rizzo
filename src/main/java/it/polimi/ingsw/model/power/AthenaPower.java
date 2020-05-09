package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class AthenaPower extends PowerStrategy {

    public void pruneOtherActionTree(Board board, Player myself, Player other, Turn myLastTurn, ActionTree otherActionTree){
        boolean prune = false;
        if(myLastTurn == null)
            prune = false;
        else{
            for(Action action: myLastTurn.getActions()){
                if(action instanceof MoveAction){
                    if(((MoveAction) action).getDirection() == Direction.UP)
                        prune = true;
                }
            }
        }
        if(prune)
            prune(otherActionTree);
    }

    public void prune(ActionTree curr){
        List<ActionTree> childrenToRemove = new ArrayList<>();
        for(ActionTree child: curr.getChildren()){
            if(child.getAction() instanceof MoveAction && ((MoveAction) child.getAction()).getDirection() == Direction.UP){
                childrenToRemove.add(child);
            }else
                prune(child);
        }
        for(ActionTree child: childrenToRemove){
            curr.removeChild(child);
        }
        if(childrenToRemove.size() > 0 && !curr.isWin() && curr.getChildren().size()==0){
            // lose
            curr.setLose(true);
            curr.setEndOfTurn(true);
        }
    }
}
