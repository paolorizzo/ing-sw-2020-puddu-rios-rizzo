package it.polimi.ingsw.view;


import it.polimi.ingsw.model.Action;

import java.util.List;

public enum GameState {
    START_GAME{
        public void execute(ClientView view, Object input) {
            //System.out.println("START_GAME");
            view.currentGameState = REQUEST_ACTIONS;
            view.currentGameState.execute(view, null);
        }
    },
    REQUEST_ACTIONS{
        public void execute(ClientView view, Object input){
            //System.out.println("REQUEST_ACTIONS");
            view.currentGameState = RECEIVE_ACTIONS;
            try{
                view.getController().requestActions(view.getId());
            }catch (InterruptedException e){
                System.out.println(e.getStackTrace());
            }
        }
    },
    RECEIVE_ACTIONS{
        public void execute(ClientView view, Object input) {
            //System.out.println("RECEIVE_ACTIONS");
            List<Action> possibleActions = (List<Action>)input;
            view.currentGameState = ASK_ACTION;
            view.currentGameState.execute(view, possibleActions);
        }
    },
    ASK_ACTION{
        public void execute(ClientView view, Object input) {
            //System.out.println("ASK_ACTION");
            view.currentGameState = READ_ACTION;
            List<Action> possibleActions = (List<Action>)input;
            System.out.println(possibleActions);
            view.getUi().askAction(possibleActions, false);
        }
    },
    ASK_OPTIONAL_ACTION{
        public void execute(ClientView view, Object input) {
            //System.out.println("ASK_ACTION");
            view.currentGameState = READ_ACTION;
            List<Action> possibleActions = (List<Action>)input;
            view.getUi().askAction(possibleActions, true);
        }
    },
    READ_ACTION{
        public void execute(ClientView view, Object input) {
            //System.out.println("READ_SETUP_WORKER");
            view.currentGameState = PUBLISH_ACTION;
            view.currentGameState.execute(view, input);
        }
    },
    PUBLISH_ACTION{
        public void execute(ClientView view, Object input) {
            Action action = (Action) input;
            view.currentGameState = REQUEST_ACTIONS;
            view.getController().publishAction(view.getId(), action);
            view.currentGameState.execute(view, null);
        }
    },
    PUBLISH_VOLUNTARY_END_OF_TURN{
        public void execute(ClientView view, Object input) {
            view.currentGameState = REQUEST_ACTIONS;
            view.getController().publishVoluntaryEndOfTurn(view.getId());
            view.currentGameState.execute(view, null);
        }
    },
    WIN_STATE{
        public void execute(ClientView view, Object input)
        {

            //System.out.println("WIN");
        }
    },
    LOSE_STATE{
        public void execute(ClientView view, Object input)
        {

            //System.out.println("LOSE");
        }
    };

    //default implementation of the body of the state, does nothing
    //TODO change to abstract when all the states are implemented
    public void execute(ClientView view, Object input) {

    }
}

