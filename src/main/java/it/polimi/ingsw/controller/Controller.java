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

    /**
     * these variables are used to check the correctness of the methods called on the controller
     * by the views during the connection phase
     */
    boolean accept;
    boolean acceptNumPlayers;
    int ackReceived;

    /**
     * constructs the Model
     * sets up the internal variables of the controller
     */
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

    //getters and setters
    public List<View> getViews() {
        return views;
    }
    public Map<Integer, View> getViewMap() {
        return viewMap;
    }
    public int getNextId() {
        return nextId;
    }
    public Model getModel() {
        return model;
    }

    //connection phase methods

    /**
     * adds the view to the controller, and univocally maps it to an integer
     * blocks further views from being added until an ack for this one is received
     * adds the view to the model as an observer of the feed
     * starts the client associated with the view
     * @param view that is being added
     */
    //TODO handle player limit
    public synchronized void addView(View view) throws InterruptedException {
        System.out.println("trying to add a view");
        while(!accept){
            this.wait();
        }
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


    /**
     * publishes the next free id
     */
    @Override
    public synchronized void generateId(){
        System.out.println("generating id " + nextId);
        model.feed.notifyID(nextId);
    }

    /**
     * checks that the ack received is the correct one
     * this means that it checks that it has received acks for all previous ids
     * also allows for another view to be added by setting accept to true
     * @param id of the client that is sending the ack
     */
    @Override
    public synchronized void ackId(int id)
    {
        if(ackReceived == id-1){
            System.out.println("received ack for id " + id);
            accept = true;
            if(id == 0)
                acceptNumPlayers = true;
            ackReceived++;
            this.notifyAll();
        }
        else{
            throw new IncorrectStateException("cannot accept an ack for id " + id + "because not all previous acks have been received");
        }
    }

    /**
     * if the numPlayers is acceptable at the time (which happens after the ack for the id 0
     * and the numPlayers has an acceptable value, it sets it
     * notifies the caller of the success or failure of the operation
     * if the setting is successful, sets acceptNumPlayers as false so that it cannot be set again
     * @param id of the client that is trying to set the numPlayers
     * @param numPlayers number of the players that the client wishes to have in the game
     */
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
                    model.feed.notifyKo(id, "number of players must be 2 or 3");
                }
            }
        }
        else{
            throw new IncorrectStateException("Cannot set numPlayers at this time, acceptNumPlayers = " + acceptNumPlayers);
        }
    }

    /**
     * if it has been decided, returns the number of players that will be allowed to participate
     * in the game through the feed
     */
    @Override
    public synchronized void getNumPlayers() {
        if(model.numPlayersIsSet()){
            model.feed.notifyNumPlayers(model.getNumPlayers());
        }
    }

    /**
     * if the number of players is set
     * and if all the clients that are allowed to connect have actually connected, notifies this
     * news through the feed
     */
    @Override
    public synchronized void requestAllPlayersConnected(){
        if(model.numPlayersIsSet()){
            if(ackReceived+1 >= model.getNumPlayers())
                model.feed.notifyAllPlayersConnected();
        }
    }

    /**
     * checks that the name is valid, and if it is adds the relative Player to the model
     * in particular, it checks:
     * that the id belongs to a client that is allowed to join the game,
     * that there is no registered name for that id
     * that an ack has been received from that id
     * that the number of players has been set
     * that the name has not been claimed already
     * @param id of the client that is requesting the name
     * @param name that is being requested
     */
    //TODO should also check that if id==2 and numPlayers==2 the name is not accepted
    @Override
    public synchronized void setName(int id, String name) {
        if(0 <= id && id <= 2){
            if(model.playerPresent(id))
                throw new IllegalArgumentException("there is already a player with this id");
            else if(id > ackReceived)
                throw new IncorrectStateException("Cannot accept name from client " + id + " before receiving its ack");
            else if(!model.numPlayersIsSet())
                throw new IncorrectStateException("Cannot accept name from client " + id + " before receiving the number of players");
            else if(id == 2 && model.getNumPlayers()==2)
                throw new IllegalArgumentException("Cannot set a name for the third player in a two player game");
            else{

                if(model.nicknamePresent(name)){
                    System.out.println("name not accepted because already present");
                    model.feed.notifyKo(id, "this name is already taken");
                }
                else{
                    System.out.println("name " + name + " accepted for player " + id);
                    model.feed.notifyOk(id);
                    model.addPlayer(new Player(id, name));
                }
            }
        }
    }

    /**
     * gets the view related to the id using the map,
     * removes it from the model and the controller
     * and finally deletes the entry of the map that relates to the id
     * @param id of the client that wants to be deleted
     */
    @Override
    public synchronized void deleteId(int id){
        System.out.println("client " + id + " died");
        View toBeRemoved = viewMap.get(id);
        model.removeObserver(toBeRemoved);
        views.remove(toBeRemoved);
        viewMap.remove(id);
    }

    //setup phase methods

    /**
     * sends the deck through the feed
     */
    @Override
    public synchronized void requestDeck() {
        model.feed.notifyDeck(model.game.getDeck());
    }

    /**
     * checks that the request to publish the cards is legit, by checking:
     * that the chooser is the first player
     * that the cards have not yet been chosen
     * that the number of cards being chosen matches the number of players
     *
     * if the request passes these checks, memorizes the chosen cards and notifies
     * the choice through the feed
     * Moreover, if the request is accepted awakes a thread that was waiting
     * This is useful because when these cards are requested the requesting threads go in wait
     * if the cards haven't been chosen yet
     * @param id of the client sending his choice of cards
     * @param numCards list of the cards chosen, identified by their number
     * @see #requestCards(int id)
     */
    @Override
    public synchronized void publishCards(int id, List<Integer> numCards){
        try{
            if(id != 0){
                model.feed.notifyKo(id, "You do not possess the right to choose the cards");
                throw new IllegalArgumentException(id+" can't choose card");
            }
            if(model.game.cardsAlreadyChosen()){
                model.feed.notifyKo(id, "The cards have already been chosen");
                throw new IllegalArgumentException("cards already chosen");
            }
            if(numCards.size() != model.getNumPlayers()){
                model.feed.notifyKo(id, "You must choose a number of cards equal to the number of players");
                throw new IllegalArgumentException("number of cards chosen different from the number of players");
            }
            model.game.setChosenCards(numCards);

        }catch(IllegalArgumentException e){
            //model.feed.notifyKo(id, "");
            return;
        }
        model.feed.notifyOk(id);
        this.notifyAll();
    }

    /**
     * waits until it is the requesting client's turn and the cards have been chosen
     * once that happens, notifies the choice of cards to the requester on the feed
     * moreover, wakes another thread, which may have been waiting on the same condition
     * 
     * @param id of the client that requests the cards
     * @throws InterruptedException if the wait is interrupted
     */
    @Override
    public synchronized void requestCards(int id) throws InterruptedException {
        while(id != model.game.getCurrentPlayerId() || !model.game.cardsAlreadyChosen()) { // || chosenCards.size() == 0
            System.out.println(id+" mi fermo");
            this.wait();
        }
        model.feed.notifyCards(id, model.game.getChosenCards());
        this.notifyAll();
    }

    /**
     * checks that the action is allowed, by checking that:
     * it is the requesting client's turn
     * the card has not yet been taken by someone else
     * if the checks pass, saves the association between the client and the card
     * and wakes threads that may be waiting on their turn
     * Given how the fsm on the client works, this method should only be called
     * after the client has confirmed that it is their turn by receiving an answer for a requestCards
     * @param id
     * @param numCard
     */
    @Override
    public synchronized void setCard(int id, int numCard){
        if(id != model.game.getCurrentPlayerId() || !model.game.isCardPickable(numCard)){
            model.feed.notifyKo(id, "God choice unacceptable: either out of turn or not amongst picked cards");
        }else{
            model.setCardPlayer(id, numCard);   //ADVANCES THE TURN
            model.feed.notifyOk(id);

            this.notifyAll();
        }
    }

    /**
     * waits until it is the requesting client's turn
     * once it is, gets the possible setup actions from the controller and sends them through the feed
     * moreover, wakes threads that may be waiting
     * @param id of the requesting client
     * @throws InterruptedException if the wait is interrupted
     */
    @Override
    public synchronized void requestToSetupWorker(int id) throws InterruptedException {
        while(id != model.game.getCurrentPlayerId()){
            this.wait();
        }
        System.out.println("Richiedo piazzare worker");
        List<Action> possibleActions = model.game.getPossibleSetupActions(id);
        if(possibleActions != null)
            model.feed.notifyCurrentPlayer(id, possibleActions, false);

        this.notifyAll();
    }

    /**
     * MAY ADVANCE THE TURN through model.executeSetupAction
     * checks that it is the requesting client's turn and that the proposed setup action is possible
     * if all is well, executes the action and wakes possible waiting threads
     * @param id of the requesting client
     * @param setupAction an action that places a worker on the board
     */
    @Override
    public synchronized void setupWorker(int id, SetupAction setupAction){
        if(id != model.game.getCurrentPlayerId() || !model.game.possibleActionsContains(setupAction)){
            model.feed.notifyKo(id, "Worker placement unacceptable: either out of turn of not possible");
            return;
        }
        System.out.println("Piazzo worker");
        model.executeSetupAction(id, setupAction);
        model.feed.notifyOk(id);

        this.notifyAll();
    }

    //turn phase methods

    /**
     * MAY ADVANCE THE TURN THROUGH Game.getPossibleActions()
     * waits until it is the requesting player's turn
     * once it is, sends the possible actions through the feed, if there are any
     * moreover, wakes possible waiting threads
     * @param id of the requesting client
     * @throws InterruptedException if the wait is interrupted
     */
    @Override
    public synchronized void requestActions(int id) throws InterruptedException {

        while(id != model.game.getCurrentPlayerId()) {
            System.out.println(id+" mi fermo");
            this.wait();
        }
        System.out.println("Richiedo azioni");
        List<Action> possibleActions = model.game.getPossibleActions(id);
        if(possibleActions != null)
            model.feed.notifyCurrentPlayer(id, possibleActions, model.game.isEndOfTurnPossible());
        this.notifyAll();
    }

    /**
     * checks that it is the requesting client's turn
     * and that the proposed action is possible
     * if the checks go well, executes it and wakes a waiting thread
     * @param id of the requesting client
     * @param action an action that the player wants to perform in their turn
     */
    @Override
    public synchronized void publishAction(int id, Action action){

        if(id != model.game.getCurrentPlayerId()) {
            return;
        }
        System.out.println("pubblico azione "+action);
        if(model.game.possibleActionsContains(action)){
            //ok
            model.executeAction(id, action);
        }
        this.notifyAll();
    }

    /**
     * MAY ADVANCE THE TURN
     * if it is possible to end the turn, initiates the next turn
     * @param id of the requesting client
     */
    @Override
    public synchronized void publishVoluntaryEndOfTurn(int id){
        if(model.game.isEndOfTurnPossible()){
            model.game.nextTurn();
        }
        this.notifyAll();
    }


    @Override
    public void kill(){
        //TODO save the game state and shut down the server
        //TODO should not be called from outside the server
    }


}
