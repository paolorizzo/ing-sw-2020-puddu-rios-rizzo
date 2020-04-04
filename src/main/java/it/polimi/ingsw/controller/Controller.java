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

    @Override
    public void update(Object o)
    {
        System.out.println("Update received: " + o);
    }
}
