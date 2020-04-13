package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observation.ViewObserver;
import it.polimi.ingsw.view.ConnectionState;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;


//TODO test everything
public class Controller implements ViewObserver
{
    private static Controller instance;

    private List<Player> players;
    private List<View> views;
    private Model model;

    private Controller()
    {
        players = new ArrayList<Player>();
        views = new ArrayList<View>();
        model = Model.instance();
    }

    public static Controller instance(){
        if(instance==null)
            instance = new Controller();
        return instance;
    }

    //adds player inside the model
    //also adds the controller itself as an observer of the view
    //TODO handle player limit
    public void addView(View view)
    {
        players.add(model.addPlayer(view));
        view.addObserver(this);
    }

    //RequestsObserver updates

    //publishes the id of the last player that has joined
    //TODO this method should instead publish the id of the first player whose id hasn't been published yet
    //if the third player connects before the second can solve his id, there may be conflicts
    //perhaps enforcing sequential connections already solves the problem
    public synchronized void updateRequestID()
    {
        model.playersFeed.notifyID(players.size()-1);
    }

    public synchronized void updateAckID()
    {
        //TODO: we need this method to justify the ACK travelling, but what do we do here?
    }

    public synchronized void updateRequestNumPlayers()
    {
        //TODO
    }

    //GameObserver updates
    public synchronized void updateNumPlayers(int numPlayers)
    {
        model.setNumPlayers(numPlayers);
    }

    //PlayersObservable updates
    public synchronized void updateID(int id)
    {
        //TODO
    }

    public synchronized void updateName(int id, String name)
    {
        //TODO
    }
}
