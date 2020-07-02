package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  Generate ActionTree and prune others ActionTrees for god Limus
 */
public class LimusPower extends PowerStrategy {
    /**
     * This method invokes the method prune to erase all build actions near of the worker of this player.
     * @param board the current game board
     * @param myself that player that use the power
     * @param other the opponent player that play the actual turn
     * @param myLastTurn the last turn played by myself player
     * @param otherActionTree the action tree generate by opponent player
     */
    public void pruneOtherActionTree(Board board, Player myself, Player other, Turn myLastTurn, ActionTree otherActionTree){
        prune(otherActionTree, board, myself);
    }

    /**
     * This method erases from the tree all build actions with target spaces adjacent to Limus's worker.
     * @param curr the action tree of other player to prune
     * @param board the current board of the game
     * @param limus the player with the god Limus
     */
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
