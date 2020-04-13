package it.polimi.ingsw.view;

import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observation.GameObservable;
import it.polimi.ingsw.observation.PlayersObservable;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

//TODO test basically everything in this class
public class ClientView extends View
{
    private static ClientView instance;

    ConnectionState currentConnectionState;

    //TODO this should perhaps be its own class, or multiple specific classes?
    //updates will modify these objects
    Game game;
    private List<Player> players;
    private int id;

    private UserInterface ui;

    //private because ClientView is singleton. instance() should be called to get an object of this type
    private ClientView(){
        super();
        game=null;
        players = new ArrayList<Player>();
        id = -1;
        ui = Cli.instance();
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


    //this method should be called to get an object of this type
    public static ClientView instance(){
        if(instance==null)
            instance=new ClientView();
        return instance;
    }

    public void updateID(int id){
        System.out.println("ClientView.updateID with id "+id);
        if(currentConnectionState.equals(ConnectionState.READ_ID))
            currentConnectionState.execute(id);
    }

    public synchronized void updateNumPlayers(int numPlayers){
        System.out.println("received number of players: " + numPlayers);
        if(currentConnectionState.equals(ConnectionState.READ_NUM_PLAYERS))
            currentConnectionState.execute(numPlayers);
    }

    public synchronized void updateName(int id, String name){
        //TODO
    }

    //sets up the first state for the connection FSM and executes it
    private void startConnectionFSM(){
        currentConnectionState = ConnectionState.REQUEST_ID;
        currentConnectionState.execute(null);
    }

    //start the first FSM
    public void start(){
        startConnectionFSM();
    }
}
