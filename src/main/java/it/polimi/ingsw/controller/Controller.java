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

    final Object turnMutex = new Object();
    private List<Card> chosenCards;


    private List<Integer> idCurrentPlayers;
    private int pointerIdCurrentPlayers;

    private ActionTree actionTreeCurrentPlayer;
    private Turn actualTurn;
    private boolean canEndOfTurn;
    private List<Action> possibleActions;
    private boolean gameFinish = false;
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

        //setup game variables
        chosenCards = null;
        //game variables
        actionTreeCurrentPlayer = null;

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
    public synchronized void setNumPlayers(int id, int numPlayers) {
        if(acceptNumPlayers){
            if(id == 0){
                if(2 <= numPlayers && numPlayers <=3){
                    System.out.println("Setting the number of players as " + numPlayers);
                    model.playersFeed.notifyOk(id);
                    model.setNumPlayers(numPlayers);
                    idCurrentPlayers = new ArrayList<>();
                    for(int i=0;i<numPlayers;i++){
                        idCurrentPlayers.add(i);
                    }
                    pointerIdCurrentPlayers = 1;
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
    public synchronized void requestDeck() {
        model.playersFeed.notifyDeck(model.board.getDeck());
    }
    @Override
    public synchronized void publishCards(int id, List<Integer> numCards){
        try{
            if(chosenCards != null){
                throw new IllegalArgumentException("cards already chosen");
            }
            if(numCards.size() != model.getNumPlayers()){
                throw new IllegalArgumentException("number of cards chosen different from the number of players");
            }
            chosenCards = new ArrayList<>();
            for(Integer num: numCards) {
                chosenCards.add(model.board.getDeck().pickCard(num));
            }
        }catch(IllegalArgumentException e){
            model.playersFeed.notifyKo(id);
            if(!e.getMessage().equals("cards already chosen")){
                chosenCards = null;
            }
            return;
        }

        model.playersFeed.notifyOk(id);
        this.notifyAll();
    }
    @Override
    public synchronized void requestCards(int id) throws InterruptedException {
        while(id != idCurrentPlayers.get(pointerIdCurrentPlayers) || chosenCards == null || chosenCards.size() == 0) {
            System.out.println(id+" mi fermo");
            this.wait();
        }
        model.playersFeed.notifyCards(idCurrentPlayers.get(pointerIdCurrentPlayers), chosenCards);
        this.notifyAll();
    }
    @Override
    public synchronized void setCard(int id, int numCard){
        Card card = null;
        for(Card c: chosenCards){
            if(c.getNum() == numCard)
                card = c;
        }
        if(card == null || id != idCurrentPlayers.get(pointerIdCurrentPlayers)){
            model.playersFeed.notifyKo(id);
        }else{
            chosenCards.remove(card);
            model.getPlayers().get(id).setCard(card);
            model.playersFeed.notifyOk(id);
            model.playersFeed.notifyGod(id, card);
            pointerIdCurrentPlayers = (pointerIdCurrentPlayers+1)%idCurrentPlayers.size();

            this.notifyAll();
        }
    }

    @Override
    public synchronized void requestToSetupWorker(int id) throws InterruptedException {
        while(id != idCurrentPlayers.get(pointerIdCurrentPlayers)){
            this.wait();
        }
        possibleActions = null;
        if(model.getPlayers().get(id).getWorker(Sex.FEMALE).getSpace() == null){
            possibleActions = model.getPlayers().get(id).generateSetupActionsWorker(model.board, Sex.FEMALE);
            actualTurn = new Turn(model.getPlayers().get(id));
        }else if(model.getPlayers().get(id).getWorker(Sex.MALE).getSpace() == null){
            possibleActions = model.getPlayers().get(id).generateSetupActionsWorker(model.board, Sex.MALE);
        }

        if(possibleActions != null){
            model.gameFeed.notifyCurrentPlayer(id, possibleActions, false);
        }

        this.notifyAll();
    }

    @Override
    public synchronized void setupWorker(int id, SetupAction setupAction){
        if(possibleActions == null || !possibleActions.contains(setupAction) || id != idCurrentPlayers.get(pointerIdCurrentPlayers)){
            model.playersFeed.notifyKo(id);
            return;
        }
        actualTurn.add(setupAction);
        model.board.executeAction(setupAction);
        model.playersFeed.notifyOk(id);
        model.gameFeed.notifyAction(id, setupAction);
        if(model.getPlayers().get(id).getWorker(Sex.FEMALE).getSpace() != null && model.getPlayers().get(id).getWorker(Sex.MALE).getSpace() != null){
            model.turnArchive.addTurn(actualTurn);
            pointerIdCurrentPlayers = (pointerIdCurrentPlayers+1)%idCurrentPlayers.size();
        }
        this.notifyAll();
    }
    @Override
    public synchronized void requestActions(int id) throws InterruptedException {
        while(id != idCurrentPlayers.get(pointerIdCurrentPlayers)) {
            System.out.println(id+" mi fermo");
            this.wait();
        }
        if(actionTreeCurrentPlayer == null){
            //genererate ActionTree
            canEndOfTurn = false;
            actualTurn = new Turn(model.getPlayers().get(id));
            actionTreeCurrentPlayer = model.getPlayers().get(id).generateActionTree(model.board);
            for(int other: idCurrentPlayers){
                if(other != id && model.getPlayers().get(other).requirePruning(model.turnArchive))
                    model.getPlayers().get(other).pruneActionTree(actionTreeCurrentPlayer);
            }
        }

        if(actionTreeCurrentPlayer.isWin()){
            actionTreeCurrentPlayer = null;
            model.gameFeed.notifyPlayerWin(id);
            gameFinish = true;
        }else if(actionTreeCurrentPlayer.isLose()){
            actionTreeCurrentPlayer = null;
            model.board.removeWorkersPlayer(model.getPlayers().get(id));
            model.gameFeed.notifyPlayerLose(id);

            //find next player
            int nextPlayer = idCurrentPlayers.get((pointerIdCurrentPlayers+1)%idCurrentPlayers.size());
            idCurrentPlayers.remove(pointerIdCurrentPlayers);
            for(int pointer=0;pointer<idCurrentPlayers.size();pointer++){
                if(idCurrentPlayers.get(pointer) == nextPlayer)
                    pointerIdCurrentPlayers = pointer;
            }
            if(idCurrentPlayers.size() == 1){
                //last player win
                model.gameFeed.notifyPlayerWin(idCurrentPlayers.get(0));
                gameFinish = true;
            }
        }else{
            possibleActions = new ArrayList<>();
            for(ActionTree child: actionTreeCurrentPlayer.getChildren()){
                possibleActions.add(child.getAction());
            }
            if(possibleActions.size() == 0 && actionTreeCurrentPlayer.isEndOfTurn() && !actionTreeCurrentPlayer.isLose()){
                //endOfTurns;
                pointerIdCurrentPlayers = (pointerIdCurrentPlayers+1)%idCurrentPlayers.size();
                actionTreeCurrentPlayer = null;
                model.turnArchive.addTurn(actualTurn);
                model.gameFeed.notifyEndOfTurnPlayer(id);
                System.out.println("Next player id: "+idCurrentPlayers.get(pointerIdCurrentPlayers));
            }else if(possibleActions.size()>0 && !actionTreeCurrentPlayer.isLose()){
                model.gameFeed.notifyCurrentPlayer(id, possibleActions, canEndOfTurn);
            }
        }
        this.notifyAll();
    }
    @Override
    public synchronized void publishAction(int id, Action action){
        if(id != idCurrentPlayers.get(pointerIdCurrentPlayers)) {
            return;
        }
        if(possibleActions.contains(action)){
            //ok
            actualTurn.add(action);
            model.board.executeAction(action);
            model.gameFeed.notifyAction(id, action);

            ActionTree nextChild = null;
            for(ActionTree child: actionTreeCurrentPlayer.getChildren()){
                if(child.getAction().equals(action))
                    nextChild = child;
            }
            actionTreeCurrentPlayer = nextChild;
            canEndOfTurn |= actionTreeCurrentPlayer.isEndOfTurn();
        }
        this.notifyAll();
    }
    public synchronized void publishVoluntaryEndOfTurn(int id){
        if(canEndOfTurn){
            pointerIdCurrentPlayers = (pointerIdCurrentPlayers+1)%idCurrentPlayers.size();
            actionTreeCurrentPlayer = null;

            System.out.println("Next player id: "+idCurrentPlayers.get(pointerIdCurrentPlayers));
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
