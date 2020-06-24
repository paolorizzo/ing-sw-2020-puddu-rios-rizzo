package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * FSM for the Setup Phase, which covers the choice of cards and the positioning of
 * the workers on the board
 */
public enum SetupState {
    START_SETUP{
        /**
         * entry point to the Setup FSM, depending on the id either
         * moves to get the deck, or to request a list of cards from which to pick
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("START_SETUP");
            if(view.getId() == 0){
                view.currentSetupState = REQUEST_DECK;
            }else{
                view.currentSetupState = REQUEST_CARDS;
            }
            view.currentSetupState.execute(view, null);
        }
    },
    REQUEST_DECK{
        /**
         * initializes enum-level variables and asks the local
         * controller for the deck
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input){
            //System.out.println("REQUEST_DECK");
            deck = null;
            numCards = new ArrayList<Integer>();
            view.currentSetupState = RECEIVE_DECK;
            view.getController().requestDeck();
        }
    },
    RECEIVE_DECK{
        /**
         * memorizes the deck and moves to ask the user to pick from the deck
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            deck = (Deck)input;
            //System.out.println("Deck ricevuto");
            view.currentSetupState = ASK_CARD;
            view.currentSetupState.execute(view, null);
        }
    },
    ASK_CARD{
        /**
         * calls on the ui to get the user to pick cards from the deck
         * using user input
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input){
            view.currentSetupState = READ_CARD;
            view.getUi().askCard(deck);
        }
    },
    READ_CARD{
        /**
         * checks the legality of the pick, and if legal adds the cards to the list
         * of possible cards
         * If all the necessary cards have been picked, publishes the list of card numbers
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            int numCard = (int)input;
            try{
                deck.pickCard(numCard);
            }catch(IllegalArgumentException e){
                view.currentSetupState = ASK_CARD;
                view.currentSetupState.execute(view, null);
                return;
            }
            numCards.add(numCard);
            //System.out.println("Num card chosen :"+numCard);
            if(numCards.size() == view.getUi().getNumPlayers())
                view.currentSetupState = PUBLISH_CARDS;
            else
                view.currentSetupState = ASK_CARD;
            view.currentSetupState.execute(view, null);
        }
    },
    PUBLISH_CARDS{
        /**
         * publishes the card numbers to the local controller,
         * and sets up the feedback check state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input){
            okState = REQUEST_CARDS;
            koState = REQUEST_DECK;
            okInput = null;
            koInput = null;
            view.currentSetupState = RECEIVE_CHECK;
            view.getController().publishCards(view.getId(), numCards);
        }
    },
    RECEIVE_CHECK{
        /**
         * depending on the input, goes to and executes a preset state.
         * Before moving to this state, the states and inputs for
         * success and failure should be defined
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            String problem = (String) input;
            boolean success = (problem == null);

            SetupState nextState;
            Object nextInput;

            if(success){
                //System.out.println("operation successful");
                nextState = okState;
                nextInput = okInput;
                if(koState == REQUEST_SETUP_WORKER)
                    countOk++;
            }
            else{
                //System.out.println("operation failed");
                nextState = koState;
                nextInput = koInput;
            }

            koState = null;
            okState = null;
            koInput = null;
            okInput = null;

            view.currentSetupState = nextState;
            view.currentSetupState.execute(view,nextInput);
        }
    },
    REQUEST_CARDS{
        /**
         * requests the list of possible cards from the local controller
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("REQUEST_CARDS");
            try{
                view.getController().requestCards(view.getId());
            }catch (InterruptedException e){
                System.out.println(e.getStackTrace());
            }
            view.currentSetupState = RECEIVE_CARDS;
        }
    },
    RECEIVE_CARDS{
        /**
         * moves to ask the user to choose their card
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentSetupState = ASK_GOD;
            view.currentSetupState.execute(view,input);
        }
    },
    ASK_GOD{
        /**
         * calls on the ui to get a card number from user input
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentSetupState = READ_GOD;
            List<Card> cards = (List<Card>) input;
            view.getUi().askGod(cards);
        }
    },
    READ_GOD{
        /**
         * sets the chosen card on the local controller
         * and sets up the feedback check state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            int numCard = (int)input;
            view.currentSetupState = RECEIVE_CHECK;
            okState = REQUEST_SETUP_WORKER;
            koState = REQUEST_CARDS;
            okInput = null;
            koInput = null;
            view.getController().setCard(view.getId(), numCard);
        }
    },
    REQUEST_SETUP_WORKER{
        /**
         * requests the list of legal setup action to the local controller
         * if all workers have benn positioned, ends the setup phase
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("countOK "+countOk);
            if(countOk == 2){
                view.currentSetupState = END_SETUP;
                view.currentSetupState.execute(view, input);
                return;
            }
            //System.out.println("SETUP_WORKER");
            view.currentSetupState = ASK_SETUP_WORKER;
            try {
                view.getController().requestToSetupWorker(view.getId());
            }catch (InterruptedException e){
                System.out.println(e.getStackTrace());
            }
        }
    },
    ASK_SETUP_WORKER{
        /**
         * calls on the ui to get the setup action from user input
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("ASK_SETUP_WORKER");
            view.currentSetupState = READ_SETUP_WORKER;
            List<Action> possbileActions = (List<Action>)input;
            view.getUi().askSetupWorker(possbileActions);
        }
    },
    READ_SETUP_WORKER{
        /**
         * moves to publish the chosen setup action
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("READ_SETUP_WORKER");
            view.currentSetupState = PUBLISH_SETUP_WORKER;
            view.currentSetupState.execute(view, input);
        }
    },
    PUBLISH_SETUP_WORKER{
        /**
         * publishes the setup action to the local controller
         * and sets up the feedback check state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            SetupAction setupAction = (SetupAction) input;
            view.currentSetupState = RECEIVE_CHECK;
            okState = REQUEST_SETUP_WORKER;
            koState = REQUEST_SETUP_WORKER;
            okInput = null;
            koInput = null;
            view.getController().setupWorker(view.getId(), setupAction);
        }
    },
    END_SETUP{
        /**
         * starts the next FSM, the Game FSM
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("ok ho finito il mio setup");
            view.startGameFSM();
        }
    };


    private static SetupState koState = null;
    private static SetupState okState = null;
    private static Object koInput = null;
    private static Object okInput = null;
    private static int countOk = 0;
    private static Deck deck = null;
    private static List<Integer> numCards = null;
    //utility general next state function, returns the next enum instance in order, or null if the FSM has terminated
    private ConnectionState next(){
        if(ordinal()==ConnectionState.values().length-1)
            return null;
        else
            return ConnectionState.values()[ this.ordinal()+1 ];
    }

    //default implementation of the body of the state, does nothing

    /**
     * default implementation of the method for the execution of a state
     * @param view the view on which to act
     * @param input the input for the execution of the state
     */
    public abstract void execute(ClientView view, Object input);
}
