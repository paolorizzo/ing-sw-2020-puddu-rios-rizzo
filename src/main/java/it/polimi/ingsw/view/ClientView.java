package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observation.GameObservable;
import it.polimi.ingsw.observation.PlayersObservable;
import it.polimi.ingsw.observation.UserInterfaceObserver;
import it.polimi.ingsw.view.middleware.Client;
import it.polimi.ingsw.view.cli.Cli;


import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

//TODO test basically everything in this class
public class ClientView extends View implements UserInterfaceObserver
{
    //finite state machine states
    ConnectionState currentConnectionState;


    //updates will modify these objects
    Game game;
    private int id;
    private UserInterface ui;

    //private because ClientView is singleton. instance() should be called to get an object of this type
    public ClientView(ControllerInterface controller){
        super(controller);
        game = null;
        id = -1;
    }

    public void setUi(UserInterface ui){
        this.ui = ui;
    }
    public UserInterface getUi(){
        return ui;
    }

    public int getId(){
        return id;
    }

    public void setNumPlayers(int numPlayers){
        game = new Game(numPlayers);
    }

    public ConnectionState getConnectionState(){
        return currentConnectionState;
    }

    //TODO should not be accepted if ID is already set, meaning it is still -1
    public void setID(int id){
        this.id = id;
    }
    //updates relative to UserInterface
    public synchronized void updateReadNumPlayers(int numPlayers){
        if(currentConnectionState.equals(ConnectionState.READ_NUM_PLAYERS))
            currentConnectionState.execute(this, numPlayers);
    }
    public synchronized void updateReadName(String name){
        if(currentConnectionState.equals(ConnectionState.READ_NAME))
            currentConnectionState.execute(this, name);
    }
    //updates relative to GameObserver

    public synchronized void updateNumPlayers(int numPlayers){
        System.out.println("received number of players: " + numPlayers);
        if(game != null)
            System.out.println("it was already known, state: "+currentConnectionState.toString());
        if(currentConnectionState.equals(ConnectionState.RECEIVE_NUM_PLAYERS))
            currentConnectionState.execute(this, numPlayers);
    }

    //updates relative to PlayersObserver

    public synchronized void updateID(int id){
        if(this.id != -1)
            System.out.println("ClientView.updateID with id "+id+", but was already "+this.id);
        else
            System.out.println("ClientView.updateID with new id "+id);
        if(currentConnectionState.equals(ConnectionState.RECEIVE_ID))
            currentConnectionState.execute(this, id);
    }

    public synchronized void updateAllPlayersConnected(){
        if(currentConnectionState.equals(ConnectionState.RECEIVE_ALL_PLAYERS_CONNECTED))
            currentConnectionState.execute(this, null);
    }

    public synchronized void updateStart(){
        if(currentConnectionState.equals(ConnectionState.READY))
            currentConnectionState.execute(this, null);
    }

    //this method supposes that only valid names are received
    public synchronized void updateName(int id, String name){
        getUi().registerPlayer(id, name);
        if(id == this.id){
            System.out.println("my name is " + name);
        }else
            System.out.println("player " + id + " chose " + name + " as their username");
        System.out.println("Player register "+getUi().getNumPlayersRegister());
        if(getUi().getNumPlayersRegister() == game.getNumPlayers() && currentConnectionState == ConnectionState.WAIT_ALL_PLAYERS_NAME){
            currentConnectionState = ConnectionState.END;
            currentConnectionState.execute(this, null);
        }
    }


    public synchronized void updateOk(int id){
        if(id == this.id){
            if(currentConnectionState.equals(ConnectionState.RECEIVE_CHECK)){
                currentConnectionState.execute(this, true);
            }
            else{
                throw new IncorrectStateException("Received an okay for some communication, but was not waiting for it. I am in state " + currentConnectionState.name());
            }
        }
    }

    public synchronized void updateKo(int id){
        if(id == this.id){
            if(currentConnectionState.equals(ConnectionState.RECEIVE_CHECK)){
                currentConnectionState.execute(this, false);
            }
            else{
                throw new IncorrectStateException("Received a ko for some communication, but was not waiting for it. I am in state " + currentConnectionState.name());
            }
        }
    }

    //sets up the first state for the connection FSM and does NOT execute it, since it will be awakened by
    //an update
    private void startConnectionFSM(){
        currentConnectionState = ConnectionState.READY;
    }

    //start the first FSM
    public void start(){
        ui.showLogo();
        startConnectionFSM();
    }
}
