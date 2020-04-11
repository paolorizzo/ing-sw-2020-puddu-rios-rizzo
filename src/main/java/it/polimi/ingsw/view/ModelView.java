package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;


//TODO test
//TODO this is a stub of the class
public class ModelView {
    private Player player;
    private List<Player> allPlayers;

    public ModelView(){
        allPlayers = new ArrayList<Player>();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(List<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }
}
