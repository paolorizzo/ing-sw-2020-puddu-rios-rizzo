package it.polimi.ingsw.view;


import it.polimi.ingsw.model.Action;

import java.util.List;

/**
 * FSM for the main phase of game, the one in which workers move and build
 */
public enum GameState {
    START_GAME{
        /**
         * entry point for the Game FSM
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentGameState = REQUEST_ACTIONS;
            view.currentGameState.execute(view, null);
        }
    },
    REQUEST_ACTIONS{
        /**
         * requests the list of possible actions from the local controller
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input){
            view.currentGameState = RECEIVE_ACTIONS;
            try{
                view.getController().requestActions(view.getId());
            }catch (InterruptedException e){
                System.out.println(e.getStackTrace());
            }
        }
    },
    RECEIVE_ACTIONS{
        /**
         * passes the possible actions to the next state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            List<Action> possibleActions = (List<Action>)input;
            view.currentGameState = ASK_ACTION;
            view.currentGameState.execute(view, possibleActions);
        }
    },
    ASK_ACTION{
        /**
         * calls on the ui to get the chosen action from user input
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentGameState = READ_ACTION;
            List<Action> possibleActions = (List<Action>)input;
            view.getUi().askAction(possibleActions, false);
        }
    },
    ASK_OPTIONAL_ACTION{
        /**
         * calls on the ui to get the chosen action from user input,
         * in the case in which the action is optional
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentGameState = READ_ACTION;
            List<Action> possibleActions = (List<Action>)input;
            view.getUi().askAction(possibleActions, true);
        }
    },
    READ_ACTION{
        /**
         * passes the chosen action to the next state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentGameState = PUBLISH_ACTION;
            view.currentGameState.execute(view, input);
        }
    },
    PUBLISH_ACTION{
        /**
         * publishes the chosen action to the local controller
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            Action action = (Action) input;
            view.currentGameState = REQUEST_ACTIONS;
            view.getController().publishAction(view.getId(), action);
            view.currentGameState.execute(view, null);
        }
    },
    PUBLISH_VOLUNTARY_END_OF_TURN{
        /**
         * publishes the choice to end the turn prematurely to the local controller
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentGameState = REQUEST_ACTIONS;
            view.getController().publishVoluntaryEndOfTurn(view.getId());
            view.currentGameState.execute(view, null);
        }
    },
    WIN_STATE{
        /**
         * endpoint of the FSM in case of a win
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input)
        {

            //System.out.println("WIN");
        }
    },
    LOSE_STATE{
        /**
         * endpoint of the FSM in case of a loss
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input)
        {

            //System.out.println("LOSE");
        }
    };

    /**
     * default implementation of the method for the execution of a state
     * @param view the view on which to act
     * @param input the input for the execution of the state
     */
    public abstract void execute(ClientView view, Object input);
}

