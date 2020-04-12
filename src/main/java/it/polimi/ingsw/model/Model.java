package it.polimi.ingsw.model;

import it.polimi.ingsw.observation.GameObservable;
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
    List<Player> players;

    //feeds
    public GameObservable gameFeed;
    public PlayersObservable playersFeed;

    private Model(){
        game = new Game();
        players = new ArrayList<Player>();
        gameFeed = new GameObservable();
        playersFeed = new PlayersObservable();
    }

    //singleton
    public static Model instance(){
        if(instance==null)
            instance = new Model();
        return instance;
    }

    GameObservable getGameFeed(){
        return gameFeed;
    }


    //constructs a new player with the view, and adds it to the player list
    //also adds it as an observer and returns it
    public Player addPlayer(View view){
        int id = players.size()+1;
        Player newPlayer = new Player(Color.values()[((id-1))], id, view);
        players.add(newPlayer);
        addObserver(newPlayer);
        return newPlayer;
    }

    public void setNumPlayers(int numPlayers){
        game.setNumPlayers(numPlayers);
    }

    public void addObserver(Player p){
        addObserver(p.getView());
    }

    public void addObserver(View view){
        gameFeed.addObserver(view);
        playersFeed.addObserver(view);

    }


}
