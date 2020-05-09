package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

//TODO test Game
//TODO this class should become a GameObservable, and other observables should be incorporated in other classes
//that actually modify what is later notified
//this class should always call notifies upon any modification
public class Game {
    Model model;
    private int numPlayers;

    private List<Integer> idCurrentPlayers;
    private int pointerIdCurrentPlayers;

    private Deck deck;
    private List<Card> chosenCards;


    private Turn currentTurn;
    private ActionTree actionTreeCurrentPlayer;
    private List<Action> possibleActions;
    private boolean canEndTurn;

    boolean finish;

    public TurnArchive turnArchive;

    public Game(Model model){
        this.model = model;
        numPlayers = -1;
        deck = new Deck();

        canEndTurn = false;
        possibleActions = null;
        actionTreeCurrentPlayer = null;

        turnArchive = new TurnArchive();
        finish = false;
    }

    public Game(Model model, int numPlayers){
        this(model);
        setNumPlayers(numPlayers);
    }

    public boolean numPlayersIsSet(){
        return numPlayers != -1;
    }

    public void setNumPlayers(int numPlayers) {
        //System.out.println("Setting a game for " + numPlayers + " players");
        this.numPlayers = numPlayers;
        //TODO move this somewhere more appropriate
        model.game.initializePlayerSuccession();
    }

    public boolean playerSuccessionUninitialized(){
        return idCurrentPlayers == null;
    }

    public void initializePlayerSuccession(){
        idCurrentPlayers = new ArrayList<>();
        for(int i=0;i<numPlayers;i++){
            idCurrentPlayers.add(i);
        }
        pointerIdCurrentPlayers = 1;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void nextTurn(){
        if(currentTurn != null)
            turnArchive.addTurn(currentTurn);
        pointerIdCurrentPlayers = (pointerIdCurrentPlayers+1)%idCurrentPlayers.size();
        currentTurn = null;
        actionTreeCurrentPlayer = null;
        possibleActions = null;
    }

    public int getCurrentPlayerId(){
        return idCurrentPlayers.get(pointerIdCurrentPlayers);
    }


    public Deck getDeck(){
        return deck;
    }

    public boolean cardsAlreadyChosen() {
        return chosenCards != null;
    }

    public void setChosenCards(List<Integer> numCards) {
        this.chosenCards = new ArrayList<>();
        for(Integer num: numCards) {
            this.chosenCards.add(deck.pickCard(num));
        }
    }

    public List<Card> getChosenCards() {
        return chosenCards;
    }

    public Card getChosenCard(int numCard){
        for(Card card: chosenCards)
            if(card.getNum() == numCard)
                return card;
        return null;
    }

    public void removeChosenCard(Card card) {
        chosenCards.remove(card);
    }

    public boolean isCardPickable(int numCard) {
        if(!cardsAlreadyChosen())
            return false;
        if(getChosenCard(numCard) == null)
            return false;
        return true;
    }

    public List<Action> getPossibleSetupActions(int id){
        if(id != getCurrentPlayerId())
            return null;
        possibleActions = null;
        if(model.getPlayers().get(id).getWorker(Sex.FEMALE).getSpace() == null){
            possibleActions = model.getPlayers().get(id).generateSetupActionsWorker(model.board, Sex.FEMALE);
            currentTurn = new Turn(model.getPlayers().get(id));
        }else if(model.getPlayers().get(id).getWorker(Sex.MALE).getSpace() == null){
            possibleActions = model.getPlayers().get(id).generateSetupActionsWorker(model.board, Sex.MALE);
        }
        return possibleActions;
    }
    //return the possible actions for an id.
    //views always ask for possible actions to do
    //if an id asks possible actions and he lost, win or finish the actions for this turn
    //this method set all for the next id
    /**
     * MAY ADVANCE THE TURN
     * always called when a view asks for its possible actions
     * if this is the first time the actions are being asked in the current turn, constructs the turn and generates the full actionTree
     * //TODO complete the javadoc for this method
     * @param id for which to calculate the possible actions
     * @return the actions that are currently possible for the given id
     */
    public List<Action> getPossibleActions(int id){
        if(finish)
            return null;
        if(id != getCurrentPlayerId())
        return null;

        if(currentTurn == null){
            //devo inizializzare e generare il turno
            currentTurn = new Turn(model.getPlayers().get(id));
            canEndTurn = false;
            //genererate ActionTree
            actionTreeCurrentPlayer = generateActionTree(id);
        }

        //win and lose
        if(actionTreeCurrentPlayer.isWin()){
            System.out.println(id+" WIN");
            addTurnInArchive();
            actionTreeCurrentPlayer = null;
            possibleActions = null;
            finish = true;
            model.feed.notifyPlayerWin(id);
            return null;
        }else if(actionTreeCurrentPlayer.isLose()){
            System.out.println(id+" LOSE");
            addTurnInArchive();
            actionTreeCurrentPlayer = null;
            possibleActions = null;
            currentTurn = null;

            model.board.removeWorkersPlayer(model.getPlayers().get(id));
            model.feed.notifyPlayerLose(id);

            //find next player
            int nextPlayer = idCurrentPlayers.get((pointerIdCurrentPlayers+1)%idCurrentPlayers.size());
            idCurrentPlayers.remove(pointerIdCurrentPlayers);
            for(int pointer=0;pointer<idCurrentPlayers.size();pointer++){
                if(idCurrentPlayers.get(pointer) == nextPlayer)
                    pointerIdCurrentPlayers = pointer;
            }
            if(idCurrentPlayers.size() == 1){
                //last player win
                finish = true;
                model.feed.notifyPlayerWin(idCurrentPlayers.get(0));
                System.out.println(idCurrentPlayers.get(0)+" WIN");
            }
            return null;
        }


        possibleActions = new ArrayList<>();
        for(ActionTree child: actionTreeCurrentPlayer.getChildren()){
            possibleActions.add(child.getAction());
        }
        if(possibleActions.size() == 0 && actionTreeCurrentPlayer.isEndOfTurn() && !actionTreeCurrentPlayer.isLose()){
            //endOfTurns;
            nextTurn();
            model.feed.notifyEndOfTurnPlayer(id);
            System.out.println("Next player id: "+idCurrentPlayers.get(pointerIdCurrentPlayers));
            return null;
        }
        return possibleActions;
    }
    public boolean possibleActionsContains(Action action) {
        if(possibleActions == null)
            return false;
        for(Action a: possibleActions)
            if(action.equals(a))
                return true;
        return false;
    }

    private ActionTree generateActionTree(int id){
        ActionTree actionTree = model.players.get(id).generateActionTree(model.board);
        for(int opponent: idCurrentPlayers){
            if(opponent != id)
                model.getPlayers().get(opponent).pruneOtherActionTree(model.board, model.getPlayer(id), turnArchive.getLastTurnOf(model.getPlayer(opponent)), actionTree);
        }
        return actionTree;
    }



    public void addSetupAction(Action action){
        currentTurn.add(action);
    }

    public void addAction(Action action){
        currentTurn.add(action);
        ActionTree nextChild = null;
        for(ActionTree child: actionTreeCurrentPlayer.getChildren()){
            if(child.getAction().equals(action))
                nextChild = child;
        }
        actionTreeCurrentPlayer = nextChild;
        canEndTurn |= actionTreeCurrentPlayer.isEndOfTurn();

    }
    public void addTurnInArchive(){
        turnArchive.addTurn(currentTurn);
    }

    public boolean isEndOfTurnPossible() {
        return canEndTurn;
    }

    public boolean isFinish(){
        return finish;
    }
}
