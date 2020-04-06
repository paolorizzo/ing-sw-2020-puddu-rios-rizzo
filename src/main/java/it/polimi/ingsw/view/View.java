package it.polimi.ingsw.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;

public abstract class View extends Observable implements Observer
{
    public abstract void startNameView();
    public abstract void startNumberOfPlayersView();
    public abstract void startOutOfGameView();
}
