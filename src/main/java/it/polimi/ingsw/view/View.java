package it.polimi.ingsw.view;

import it.polimi.ingsw.observation.*;

public abstract class View implements ModelObserver
{
    RequestsObservable viewRequestsFeed;
    GameObservable viewGameFeed;
    PlayersObservable viewPlayersFeed;

    public View(){
        viewRequestsFeed = new RequestsObservable();
        viewGameFeed = new GameObservable();
        viewPlayersFeed = new PlayersObservable();
    }

    public void addObserver(ViewObserver obs){
        viewRequestsFeed.addObserver(obs);
        viewGameFeed.addObserver(obs);
        viewPlayersFeed.addObserver(obs);
    }



    public RequestsObservable getViewRequestsFeed(){
        return viewRequestsFeed;
    }

    public GameObservable getViewGameFeed(){
        return viewGameFeed;
    }

    public PlayersObservable getViewPlayersFeed(){
        return viewPlayersFeed;
    }

    /*
    public abstract void startNameView();
    public abstract void startNumberOfPlayersView();
    public abstract void startOutOfGameView();

     */
}
