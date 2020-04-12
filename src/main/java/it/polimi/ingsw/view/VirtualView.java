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

    public void updateID(int id){
        connection.sendMessage("notifyID", id);
    }

    public void updateNumPlayers(int numPlayers){
        connection.sendMessage("notifyNumPlayers", numPlayers);
    }

    public void updateName(int id, String name){
        //TODO
    }

/*
    @Override
    public void startNameView()
    {
        Message m = new Message("startNameView");

        connection.send(m);
    }

    @Override
    public void startNumberOfPlayersView()
    {
        Message m = new Message("startNumberOfPlayersView");

        connection.send(m);
    }

    @Override
    public void update(Object o)
    {
        Message m = new Message("update");
        m.addArg("update from VirtualView");

        connection.send(m);
    }

    @Override
    public void startOutOfGameView()
    {
        Message m = new Message("startOutOfGameView");

        connection.send(m);
    }
 */
}
