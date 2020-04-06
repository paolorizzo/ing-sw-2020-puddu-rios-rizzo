package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class TurnArchive {
    List<Turn> turns;
    public TurnArchive(){
        turns = new ArrayList<>();
    }

    public void addTurn(Turn turn){
        turns.add(turn);
    }
    public Turn getLastTurnOf(Player player){
        for(int i=turns.size()-1;i>=0;i--){
            if(turns.get(i).getPlayer().equals(player))
                return turns.get(i);
        }
        //no found
        return null;
    }
}
