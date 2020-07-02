package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

//this class should always call notifies upon any modification

/**
 * stores information about the general state of the game, such as what cards compose the deck,
 * whose turn it is and what are the possible actions for the current player's turn
 */
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

    /**
     * constructs an empty Game
     * @param model the model that the game is contextual to
     */
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

    /**
     * constructs an empty game with a given number of players
     * @param model the model that the game is contextual to
     * @param numPlayers the number of players
     */
    public Game(Model model, int numPlayers){
        this(model);
        setNumPlayers(numPlayers);
    }

    /**
     * returns true if the number of players has been set
     * @return true if the number of players has been set
     */
    public boolean numPlayersIsSet(){
        return numPlayers != -1;
    }

    /**
     * sets the number of players and initializes the rotation of turns
     * @param numPlayers the number of players
     */
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
        model.game.initializePlayerSuccession();
    }

    /**
     * returns true if the succession of player has been initialized
     * @return true if the succession of player has been initialized
     */
    public boolean playerSuccessionUninitialized(){
        return idCurrentPlayers == null;
    }

    /**
     * initializes the rotation of players, starting by
     * player 1 (ids start at 0)
     */
    public void initializePlayerSuccession(){
        idCurrentPlayers = new ArrayList<>();
        for(int i=0;i<numPlayers;i++){
            idCurrentPlayers.add(i);
        }
        pointerIdCurrentPlayers = 1;
    }

    /**
     * returns the number of players
     * @return the number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * advances the turn
     */
    public void nextTurn(){
        System.out.println("advancing turn");
        if(currentTurn != null){
            turnArchive.addTurn(currentTurn);
            if(model.allWorkersPlaced())
                model.save();
        }
        pointerIdCurrentPlayers = (pointerIdCurrentPlayers+1)%idCurrentPlayers.size();
        currentTurn = null;
        actionTreeCurrentPlayer = null;
        possibleActions = null;
    }

    /**
     * returns the id of the current player
     * @return the id of the current player
     */
    public int getCurrentPlayerId(){
        return idCurrentPlayers.get(pointerIdCurrentPlayers);
    }

    /**
     * returns the deck of cards
     * @return the deck of cards
     */
    public Deck getDeck(){
        return deck;
    }

    /**
     * returns true if the set of cards has already been picked from the deck
     * @return true if the set of cards has already been picked from the deck
     */
    public boolean cardsAlreadyChosen() {
        return chosenCards != null;
    }

    /**
     * picks a set of cards from the deck, and sets them as chosen
     * @param numCards the list of numbers of the chosen cards
     */
    public void setChosenCards(List<Integer> numCards) {
        this.chosenCards = new ArrayList<>();
        for(Integer num: numCards) {
            this.chosenCards.add(deck.pickCard(num));
        }
    }

    /**
     * returns the cards that have been chosen for the game,
     * but not yet picked for a player
     * @return the cards that have been chosen for the game,
     * but not yet picked for a player
     */
    public List<Card> getChosenCards() {
        return chosenCards;
    }

    /**
     * returns the card corresponding to the given number,
     * if it has been chosen from the deck
     * @param numCard the number of the card
     * @return the card corresponding to the given number,
     * if it has been chosen from the deck
     */
    public Card getChosenCard(int numCard){
        for(Card card: chosenCards)
            if(card.getNum() == numCard)
                return card;
        return null;
    }

    /**
     * removes a card from the set of chosen cards
     * @param card the card to be removed
     */
    public void removeChosenCard(Card card) {
        chosenCards.remove(card);
    }

    /**
     * checks whether a card can be picked from the set
     * of possible cards
     * @param numCard the number of the given card
     * @return true if the card can be chosen by a player
     */
    public boolean isCardPickable(int numCard) {
        if(!cardsAlreadyChosen())
            return false;
        if(getChosenCard(numCard) == null)
            return false;
        return true;
    }

    /**
     * returns the list of possible setup actions for a given player,
     * if it is their turn
     * @param id the id of the player related to the query
     * @return the list of possible setup actions for a given player
     */
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

    /**
     * prints the list of possible actions
     */
    public void printPossibleSetupActions(){
        System.out.println("possible actions:");
        List<Action> possibleActions = this.possibleActions;
        for (Action a:possibleActions){
            System.out.println("\t" + a.toString());
        }
    }

    /**
     * MAY ADVANCE THE TURN
     * always called when a view asks for its possible actions
     * if this is the first time the actions are being asked in the current turn, constructs the turn and generates the full actionTree
     * returns the possible actions for an id
     * Views must always ask for possible actions before publishing their chosen action
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
            model.deleteFiles();
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
                model.deleteFiles();
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

    /**
     * Checks whether an action is possible at this time
     * @param action the action to check
     * @return true if the given action is possible at this time
     */
    public boolean possibleActionsContains(Action action) {
        if(possibleActions == null)
            return false;
        for(Action a: possibleActions)
            if(action.equals(a))
                return true;
        return false;
    }

    /**
     * Generates the action tree for the given player
     * @param id the id of the given player
     * @return the tree of possible actions
     */
    private ActionTree generateActionTree(int id){
        ActionTree actionTree = model.players.get(id).generateActionTree(model.board);
        for(int opponent: idCurrentPlayers){
            if(opponent != id)
                model.getPlayers().get(opponent).pruneOtherActionTree(model.board, model.getPlayer(id), turnArchive.getLastTurnOf(model.getPlayer(opponent)), actionTree);
        }
        return actionTree;
    }

    /**
     * adds a setup action to the current turn
     * @param action the action to add to the turn
     */
    public void addSetupAction(Action action){
        currentTurn.add(action);
    }

    /**
     * adds a general action to the current turn
     * @param action the action to be added
     */
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

    /**
     * Adds the current turn to the turn archive
     */
    public void addTurnInArchive(){
        turnArchive.addTurn(currentTurn);
    }

    /**
     * checks whether it is possible to end the current turn at this time
     * @return true if it is possible to end the current turn at this time
     */
    public boolean isEndOfTurnPossible() {
        return canEndTurn;
    }

    /**
     * returns true if the game is finished
     * @return true if the game is finished
     */
    public boolean isFinish(){
        return finish;
    }

    /**
     * deeply compares two Game objects
     * @param that the other Game
     * @return true if their relevant fields are equal
     */
    public boolean fullEquals(Game that){
        /*
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
         */
        boolean equality = true;
        equality &= this.numPlayers == that.numPlayers;
        equality &= this.idCurrentPlayers.equals(that.idCurrentPlayers);
        equality &= this.pointerIdCurrentPlayers == that.pointerIdCurrentPlayers;
        equality &= this.deck.equals(that.deck);
        equality &= this.chosenCards.equals(that.chosenCards);
        equality &= ( this.currentTurn == null && that.currentTurn == null ||
                        this.currentTurn.equals(that.currentTurn)
                    );
        equality &= this.turnArchive.equals(that.turnArchive);
        return equality;
    }
}
