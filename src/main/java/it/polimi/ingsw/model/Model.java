package it.polimi.ingsw.model;

import it.polimi.ingsw.observation.GameObservable;
import it.polimi.ingsw.observation.ModelObserver;
import it.polimi.ingsw.observation.PlayersObservable;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO create singleton superclass
//TODO test whole class
//TODO refactor into 3 models, one for every feed
public class Model {
    public static Model instance;

    Game game;
    HashMap<Integer, Player> players;
    public TurnArchive turnArchive;
    //feeds
    public GameObservable gameFeed;
    public PlayersObservable playersFeed;
    public Board board;

    public Model(){
        game = new Game();
        board = new Board();
        players = new HashMap<>();
        turnArchive = new TurnArchive();
        gameFeed = new GameObservable();
        playersFeed = new PlayersObservable();
    }

    GameObservable getGameFeed(){
        return gameFeed;
    }

    public HashMap<Integer, Player> getPlayers(){
        return players;
    }

    public void setNumPlayers(int numPlayers){
        game.setNumPlayers(numPlayers);
        gameFeed.notifyNumPlayers(numPlayers);
    }

    //relies on the controller to only add valid players
    //adds the player in its right place in the ArrayList
    //publishes the notify for all known names so that the last client can know every other client
    //that has already posted their name, possibly even before the last one joined
    public void addPlayer(Player player){
        int id = player.getId();
        players.put(id, player);
        board.createPlayerWorkers(player);
        //important to notify every name to allow late clients to know all other players before
        //the end of the late client's connection phase
        for(Player p: players.values()){
            playersFeed.notifyName(p.getId(), p.getNickname());
        }
    }

    //returns true if someone already claimed the nickname
    public boolean nicknamePresent(String name){
        boolean alreadyPresent = false;
        for(Player p: players.values()) {
            if (p.getNickname().equals(name))
                alreadyPresent = true;
        }
        return alreadyPresent;
    }

    //returns true if there is already a player with that id
    //if the get fails because it is impossible to get a player with that id
    //it catches the relative exception and returns false because
    //the fact that it is impossible to get it means that it is not present
    public boolean playerPresent(int id){
        return players.get(id) != null;
    }

    public int getNumPlayers(){
        return game.getNumPlayers();
    }

    public boolean numPlayersIsSet(){
        return game.numPlayersIsSet();
    }

    public void addObserver(ModelObserver obs){
        gameFeed.addObserver(obs);
        playersFeed.addObserver(obs);
    }

    public void removeObserver(ModelObserver obs){
        gameFeed.removeObserver(obs);
        playersFeed.removeObserver(obs);
    }

}
