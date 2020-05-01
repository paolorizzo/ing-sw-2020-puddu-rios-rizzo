package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.View;

import java.util.*;

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

    //adds the view to the controller, and univocally maps it to an integer
    //blocks further views from being added until an ack for this one is received
    //adds the view to the model as an observer of the feed
    //starts the client associated with the view
    //TODO handle player limit
    public synchronized void addView(View view)
    {
        //System.out.println("trying to add a view");
        if(accept){
            accept = false;             //makes it impossible to accept more views until an ack is received
            nextId++;
            viewMap.put(nextId, view);  //enumerates the views
            views.add(view);
            model.addObserver(view);    //adds the view as observer of the feed
        }
        else{
            throw new IncorrectStateException("Impossible to accept a new view now, must receive an ack first");
        }
    }

    //Connection phase methods

    //publishes the id of the last player that has joined
    //TODO this method should instead publish the id of the first player whose id hasn't been published yet
    //if the third player connects before the second can solve his id, there may be conflicts
    //perhaps enforcing sequential connections already solves the problem
    @Override
    public synchronized void generateId(){
        model.feed.notifyID(nextId);
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
    public synchronized void setNumPlayers(int id, int numPlayers) {
        if(acceptNumPlayers){
            if(id == 0){
                if(2 <= numPlayers && numPlayers <=3){
                    System.out.println("Setting the number of players as " + numPlayers);
                    model.feed.notifyOk(id);
                    model.setNumPlayers(numPlayers);

                    acceptNumPlayers = false;
                }
                else{
                    System.out.println("Number of players " + numPlayers + " rejected");
                    model.feed.notifyKo(id);
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
            model.feed.notifyNumPlayers(model.getNumPlayers());
        }
    }

    @Override
    public synchronized void requestAllPlayersConnected(){
        if(ackReceived+1 >= model.getNumPlayers())
            model.feed.notifyAllPlayersConnected();
    }

    //only reads the name if its id is valid
    //
    @Override
    public synchronized void setName(int id, String name) {
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
                    model.feed.notifyKo(id);
                }
                else{
                    System.out.println("name " + name + " accepted for player " + id);
                    model.feed.notifyOk(id);
                    model.addPlayer(new Player(id, name));
                }
            }
        }
    }

    @Override
    public synchronized void requestDeck() {
        model.feed.notifyDeck(model.game.getDeck());
    }

    @Override
    public synchronized void publishCards(int id, List<Integer> numCards){
        try{
            if(id != 0){
                throw new IllegalArgumentException(id+" can't choose card");
            }
            if(model.game.areCardsChosen()){
                throw new IllegalArgumentException("cards already chosen");
            }
            if(numCards.size() != model.getNumPlayers()){
                throw new IllegalArgumentException("number of cards chosen different from the number of players");
            }
            model.game.setChosenCards(numCards);

        }catch(IllegalArgumentException e){
            model.feed.notifyKo(id);
            return;
        }
        model.feed.notifyOk(id);
        this.notifyAll();
    }
    @Override
    public synchronized void requestCards(int id) throws InterruptedException {
        while(id != model.game.getCurrentPlayerId() || !model.game.areCardsChosen()) { // || chosenCards.size() == 0
            System.out.println(id+" mi fermo");
            this.wait();
        }
        model.feed.notifyCards(id, model.game.getChosenCards());
        this.notifyAll();
    }
    @Override
    public synchronized void setCard(int id, int numCard){
        if(id != model.game.getCurrentPlayerId() || !model.game.isPresentInChosenCards(numCard)){
            model.feed.notifyKo(id);
        }else{
            model.setCardPlayer(id, numCard);
            model.feed.notifyOk(id);

            this.notifyAll();
        }
    }

    @Override
    public synchronized void requestToSetupWorker(int id) throws InterruptedException {
        while(id != model.game.getCurrentPlayerId()){
            this.wait();
        }
        List<Action> possibleActions = model.game.getPossibleSetupActions(id);
        if(possibleActions != null)
            model.feed.notifyCurrentPlayer(id, possibleActions, false);

        this.notifyAll();
    }

    @Override
    public synchronized void setupWorker(int id, SetupAction setupAction){
        if(id != model.game.getCurrentPlayerId() || !model.game.possibleActionsContains(setupAction)){
            model.feed.notifyKo(id);
            return;
        }
        model.executeSetupAction(id, setupAction);
        model.feed.notifyOk(id);

        this.notifyAll();
    }

    @Override
    public synchronized void requestActions(int id) throws InterruptedException {
        while(id != model.game.getCurrentPlayerId()) {
            System.out.println(id+" mi fermo");
            this.wait();
        }
        List<Action> possibleActions = model.game.getPossibleActions(id);
        if(possibleActions != null)
            model.feed.notifyCurrentPlayer(id, possibleActions, model.game.getCanEndOfTurn());
        this.notifyAll();
    }
    @Override
    public synchronized void publishAction(int id, Action action){
        if(id != model.game.getCurrentPlayerId()) {
            return;
        }
        if(model.game.possibleActionsContains(action)){
            //ok
            model.executeAction(id, action);
        }
        this.notifyAll();
    }
    public synchronized void publishVoluntaryEndOfTurn(int id){
        if(model.game.getCanEndOfTurn()){
            model.game.nextTurn();
        }
        this.notifyAll();
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
