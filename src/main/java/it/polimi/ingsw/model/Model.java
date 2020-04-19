package it.polimi.ingsw.model;

import it.polimi.ingsw.observation.GameObservable;
import it.polimi.ingsw.observation.ModelObserver;
import it.polimi.ingsw.observation.PlayersObservable;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;

//TODO create singleton superclass
//TODO test whole class
//TODO refactor into 3 models, one for every feed
public class Model {
    public static Model instance;

    Game game;
    ArrayList<Player> players;

    //feeds
    public GameObservable gameFeed;
    public PlayersObservable playersFeed;


    public Model(){
        game = new Game();
        players = new ArrayList<Player>();
        gameFeed = new GameObservable();
        playersFeed = new PlayersObservable();
    }

    GameObservable getGameFeed(){
        return gameFeed;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public void setNumPlayers(int numPlayers){
        game.setNumPlayers(numPlayers);
        for (int i=0;i<numPlayers;i++)
            players.add(null);
        gameFeed.notifyNumPlayers(numPlayers);
    }

    //relies on the controller to only add valid players
    //adds the player in its right place in the ArrayList
    //publishes the notify for all known names so that the last client can know every other client
    //that has already posted their name, possibly even before the last one joined
    public void addPlayer(Player player){
        int id = player.getId();
        players.add(id, player);

        //important to notify every name to allow late clients to know all other players before
        //the end of the late client's connection phase
        for(Player p:players){
            if(p != null){
                playersFeed.notifyName(p.getId(), p.getNickname());
            }
        }
    }

    //returns true if someone already claimed the nickname
    public boolean nicknamePresent(String name){
        boolean alreadyPresent = false;
        for(Player p:players) {
            if(p != null){
                if (p.getNickname().equals(name))
                    alreadyPresent = true;
            }
        }
        return alreadyPresent;
    }

    //returns true if there is already a player with that id
    //if the get fails because it is impossible to get a player with that id
    //it catches the relative exception and returns false because
    //the fact that it is impossible to get it means that it is not present
    public boolean playerPresent(int id){
        try{
            return players.get(id) != null;
        }
        catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    public int getNumPlayers(){
        return game.getNumPlayers();
    }

    public boolean numPlayersIsSet(){
        return game.numPlayersIsSet();
    }

    public void addObserver(ModelObserver obs){
        addObserver(obs);
    }

    public void addObserver(View view){
        gameFeed.addObserver(view);
        playersFeed.addObserver(view);
    }

    public void removeObserver(ModelObserver obs){
        gameFeed.removeObserver(obs);
        playersFeed.removeObserver(obs);
    }


}
