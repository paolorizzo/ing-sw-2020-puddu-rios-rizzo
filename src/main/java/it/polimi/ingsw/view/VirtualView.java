package it.polimi.ingsw.view;

import it.polimi.ingsw.observation.*;
import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Message;

//TODO test
public class VirtualView extends View
{
    //inherits attributes from View, notably the feeds
    private Connection connection;

    public VirtualView(Connection c)
    {
        super();
        this.connection = c;
    }

    //updates relative to GameObserver

    public synchronized void updateNumPlayers(int numPlayers){
        connection.sendMessage("notifyNumPlayers", numPlayers);
    }

    //updates relative to PlayersObserver

    public synchronized void updateStart(){
        connection.sendMessage("notifyStart");
    }

    public synchronized void updateID(int id){
        connection.sendMessage("notifyID", id);
    }

    public synchronized void updateName(int id, String name){
        connection.sendMessage("notifyName", id, name);
    }

    public synchronized void updateOk(int id){
        connection.sendMessage("notifyOk", id);
    }

    public synchronized void updateKo(int id){
        connection.sendMessage("notifyKo", id);
    }

}
