package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class LimusPower extends PowerStrategy {
    public void pruneOtherActionTree(Board board, Player myself, Player other, Turn myLastTurn, ActionTree otherActionTree){
        prune(otherActionTree, board, myself);
    }

    public void prune(ActionTree curr, Board board, Player limus){
        List<ActionTree> childrenToRemove = new ArrayList<>();
        for(ActionTree child: curr.getChildren()){
            Action action = child.getAction();
            Space targetSpace = board.getSpaces()[action.getTargetX()][action.getTargetY()];
            if(action instanceof BuildAction){
                //I can build near limus only creating a complete tower with a dome
                if(!(((BuildAction) action).getPiece() != Piece.DOME && targetSpace.getLevel() == 3) && (targetSpace.getAdjacentSpaces().contains(limus.getWorker(Sex.FEMALE).getSpace()) || targetSpace.getAdjacentSpaces().contains(limus.getWorker(Sex.MALE).getSpace())))
                    childrenToRemove.add(child);
            }
            //go down only if I don't cut branch
            if(!childrenToRemove.contains(child))
                prune(child, board, limus);

        }
        for(ActionTree child: childrenToRemove){
            curr.removeChild(child);
        }
        if(childrenToRemove.size() > 0 && !curr.isWin() && !curr.isEndOfTurn() && curr.getChildren().size()==0){
            // lose
            curr.setLose(true);
            curr.setEndOfTurn(true);
        }
    }
}
