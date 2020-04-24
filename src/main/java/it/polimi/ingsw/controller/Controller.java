package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO test everything
public class Controller implements ControllerInterface
{
    private List<View> views;
    private Model model;

    //these variables maintain a correspondence between IDs and views
    //this is used when deleting an id to know which view to remove
    //IDs are assigned based on nextId, in a strictly increasing way
    private Map<Integer, View> viewMap;
    private int nextId;

    //variabili di gestione della fsm di connessione del model
    boolean accept;
    boolean acceptNumPlayers;
    int ackReceived;

    public Controller()
    {
        views = new ArrayList<View>();
        model = new Model();
        viewMap = new HashMap<Integer, View>();
        nextId = -1;

        //sets up the connection phase variables
        accept = true;              //only accepts a player through an addView if this is true
        acceptNumPlayers = false;   //accepts numPlayers only after connecting the first client, and only before it has been set
        ackReceived = -1;           //only accepts acks sequentially
    }

    //adds player inside the model
    //also adds the controller itself as an observer of the view
    //TODO handle player limit
    public synchronized void addView(View view)
    {
        if(accept){
            accept = false;
            nextId++;
            viewMap.put(nextId, view);
            views.add(view);
            model.addObserver(view);
            startClient();
        }
        else{
            throw new IncorrectStateException("Impossible to accept a new view now, must receive an ack first");
        }
    }

    //Connection phase methods

    public void startClient(){
        model.playersFeed.notifyStart();
    }

    //publishes the id of the last player that has joined
    //TODO this method should instead publish the id of the first player whose id hasn't been published yet
    //if the third player connects before the second can solve his id, there may be conflicts
    //perhaps enforcing sequential connections already solves the problem
    @Override
    public synchronized void generateId(){
        model.playersFeed.notifyID(nextId);
    }

    //checks that the ack received is the correct one
    //this means that it checks that it has received acks for all previous ids
    //also allows for another view to be added by setting accept to true
    @Override
    public synchronized void ackId(int id)
    {
        if(ackReceived == id-1){
            System.out.println("received ack for id " + id);
            accept = true;
            if(id == 0)
                acceptNumPlayers = true;
            ackReceived++;
        }
        else{
            throw new IncorrectStateException("cannot accept an ack for id " + id + "because not all previous acks have been received");
        }
    }

    //if the numPlayers is acceptable at the time (which happens after the ack for the id 0
    //and the numPlayers has an acceptable value, it sets it
    //notifies the caller of the success or failure of the operation
    //if the setting is successful, sets acceptNumPlayers as false so that it cannot be set again
    @Override
    public synchronized void setNumPlayers(int id, int numPlayers)
    {
        if(acceptNumPlayers){
            if(id == 0){
                if(2 <= numPlayers && numPlayers <=3){
                    System.out.println("Setting the number of players as " + numPlayers);
                    model.playersFeed.notifyOk(id);
                    model.setNumPlayers(numPlayers);
                    acceptNumPlayers = false;
                }
                else{
                    System.out.println("Number of players " + numPlayers + " rejected");
                    model.playersFeed.notifyKo(id);
                }
            }
        }
        else{
            throw new IncorrectStateException("Cannot set numPlayers at this time, acceptNumPlayers = " + acceptNumPlayers);
        }
    }

    @Override
    public synchronized void getNumPlayers() {
        if(model.numPlayersIsSet()){
            model.gameFeed.notifyNumPlayers(model.getNumPlayers());
        }
    }

    //only reads the name if its id is valid
    //
    @Override
    public synchronized void setName(int id, String name)
    {
        if(0 <= id && id <= 2){
            if(model.playerPresent(id))
                throw new IllegalArgumentException("there is already a player with this id");
            else if(id > ackReceived)
                throw new IncorrectStateException("Cannot accept name from client " + id + " before receiveng its ack");
            else if(!model.numPlayersIsSet())
                throw new IncorrectStateException("Cannot accept name from client " + id + " before receiving the number of players");
            else{

                if(model.nicknamePresent(name)){
                    System.out.println("name not accepted because already present");
                    model.playersFeed.notifyKo(id);
                }
                else{
                    System.out.println("name " + name + " accepted for player " + id);
                    model.playersFeed.notifyOk(id);
                    model.addPlayer(new Player(id, name));
                }
            }
        }
    }
    @Override
    public synchronized void requestAllPlayersConnected(){
        if(ackReceived+1 >= model.getNumPlayers())
            model.playersFeed.notifyAllPlayersConnected();
    }
    @Override
    public void kill(){
        //TODO save the game state and shut down the server
        //TODO should not be called from outside the server
    }

    @Override
    public synchronized void deleteId(int id){
        System.out.println("client " + id + " died");
        View toBeRemoved = viewMap.get(id);
        model.removeObserver(toBeRemoved);
        views.remove(toBeRemoved);
        viewMap.remove(id);
    }

}
