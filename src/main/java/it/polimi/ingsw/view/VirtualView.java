package it.polimi.ingsw.view;

import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Message;

public class VirtualView extends View
{
    private Connection connection;

    public VirtualView(Connection c)
    {
        this.connection = c;
    }

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
}
