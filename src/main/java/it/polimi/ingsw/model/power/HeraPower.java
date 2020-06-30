package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;


public class HeraPower extends PowerStrategy{
    /**
     * This method invokes the method prune to erase all wins on the peripheral spaces.
     * @param board the current game board
     * @param myself that player that use the power
     * @param other the opponent player that play the actual turn
     * @param myLastTurn the last turn played by myself player
     * @param otherActionTree the action tree generate by opponent player
     */
    public void pruneOtherActionTree(Board board, Player myself, Player other, Turn myLastTurn, ActionTree otherActionTree){
        prune(otherActionTree);
    }

    /**
     * This method sets all win flags on the peripheral spaces to false, checked also if the other player can loses if not wins.
     * @param curr current pointer on action tree
     */
    public void prune(ActionTree curr){

        for(ActionTree child: curr.getChildren()){
            int targetX = child.getAction().getTargetX();
            int targetY = child.getAction().getTargetY();
            if(child.getAction() instanceof MoveAction && child.isWin() && (targetX == 0 || targetX == 4 || targetY == 0 || targetY == 4)){
                child.setWin(false);
                if(child.getChildren().isEmpty()){
                    child.setLose(true);
                }else{
                    //win and lose always have endOfTurn true
                    child.setEndOfTurn(false);
                }

            }
            prune(child);
        }
    }
}
