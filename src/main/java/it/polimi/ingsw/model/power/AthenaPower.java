package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class AthenaPower extends PowerStrategy {

    protected boolean requirePruning(Turn lastTurn){
        if(lastTurn == null)
            return false;
        else{
            for(Action action: lastTurn.getActions()){
                if(action instanceof MoveAction){
                    if(((MoveAction) action).getDirection() == Direction.UP)
                        return true;
                }
            }
        }
        return false;
    }

    protected void pruneActionTree(ActionTree curr){
        List<ActionTree> childrenToRemove = new ArrayList<>();
        for(ActionTree child: curr.getChildren()){
            if(child.getAction() instanceof MoveAction && ((MoveAction) child.getAction()).getDirection() == Direction.UP){
                childrenToRemove.add(child);
            }else
                pruneActionTree(child);
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
