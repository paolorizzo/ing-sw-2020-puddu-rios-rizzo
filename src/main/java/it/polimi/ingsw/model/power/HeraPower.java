package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;


public class HeraPower extends PowerStrategy{
    public boolean requirePruning(Turn lastTurn){
        return true;
    }

    public void pruneActionTree(ActionTree curr){

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
            pruneActionTree(child);
        }
    }
}
