package it.polimi.ingsw.view;

import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observation.GameObservable;
import it.polimi.ingsw.observation.PlayersObservable;
import it.polimi.ingsw.view.cli.Cli;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

//TODO test basically everything in this class
public class ClientView extends View
{

    //finite state machine states
    ConnectionState currentConnectionState;

    //updates will modify these objects
    Game game;
    private List<Player> players;
    private int id;

    private UserInterface ui;

    public ClientView(){
        super();
        game=null;
        players = new ArrayList<Player>();
        id = -1;
        ui = new Cli();
    }

    public UserInterface getUi(){
        return ui;
    }

    public int getID(){
        return id;
    }

    //TODO should not be accepted if ID is already set, meaning it is still -1
    public void setID(int id){
        this.id = id;
    }

    public synchronized void updateID(int id){
        if(this.id != -1)
            System.out.println("ClientView.updateID with id "+id+", but was already "+this.id);
        else
            System.out.println("ClientView.updateID with new id "+id);
        if(currentConnectionState.equals(ConnectionState.READ_ID))
            currentConnectionState.execute(this, id);
    }

    public synchronized void updateNumPlayers(int numPlayers){
        System.out.println("received number of players: " + numPlayers);
        if(currentConnectionState.equals(ConnectionState.READ_NUM_PLAYERS))
            currentConnectionState.execute(this, numPlayers);
    }

    public synchronized void updateName(int id, String name){
        //TODO
    }

    //sets up the first state for the connection FSM and executes it
    private void startConnectionFSM(){
        currentConnectionState = ConnectionState.REQUEST_ID;
        currentConnectionState.execute(this, null);
    }

    //start the first FSM
    public void start()
    {
        ui.showLogo();
        startConnectionFSM();
    }
}
