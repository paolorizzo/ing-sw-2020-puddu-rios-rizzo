package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;

public class Controller implements Observer
{
    private List<View> views;

    public Controller()
    {
        this.views = new ArrayList<View>();
    }

    public void addView(View view)
    {
        views.add(view);
    }

    public void start()
    {
        for(View v : views)
        {
            v.startNameView();
        }
    }

    public void setup()
    {
        views.get(0).startNumberOfPlayersView();
    }

    @Override
    public void update(Object o)
    {
        System.out.println("Update received: " + o);
    }
}
