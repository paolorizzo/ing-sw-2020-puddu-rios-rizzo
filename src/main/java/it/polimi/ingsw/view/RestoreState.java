package it.polimi.ingsw.view;

/**
 * FSM on the client side that handles the possible restoration of a saved game
 */
public enum RestoreState {
    START_RESTORE{
        /**
         * entry point for the Restore FSM, goes to and executes
         * the actual first state
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            view.currentRestoreState = RestoreState.REQUEST_GAME_AVAILABLE;
            view.currentRestoreState.execute(null);
        }
    },
    REQUEST_GAME_AVAILABLE{
        /**
         * requests the local controller to send info regarding whether
         * there is a saved game for the current set of players
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            view.getController().isGameAvailable();
            view.currentRestoreState = RestoreState.RECEIVE_GAME_AVAILABLE;
        }
    },
    RECEIVE_GAME_AVAILABLE{
        /**
         * if there is an available saved game, depending on the id
         * either moves to ask the user about their intentions,
         * or moves to receive another user's intention
         * if there is no available saved game, moves to skip the Restore Phase
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            boolean available = (boolean) input;
            if (available){
                if (view.getId() == 0){
                    view.currentRestoreState = RestoreState.ASK_RESTORE;
                    view.currentRestoreState.execute(null);
                }
                else{
                    view.currentRestoreState = RestoreState.REQUEST_RESTORE;
                    view.currentRestoreState.execute(null);
                }
            }
            else{       //ends the fsm if there is no game available
                view.currentRestoreState = RestoreState.SKIP_RESTORE;
                view.currentRestoreState.execute(null);
            }
        }
    },
    ASK_RESTORE{
        /**
         * calls on the ui to get the intention of the player
         * about whether to restore a saved game from user input
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            view.currentRestoreState = RestoreState.READ_RESTORE;
            view.getUi().askRestore();
        }
    },
    READ_RESTORE{
        /**
         * moves to publish the intention regards the restoration of a game
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            boolean restore = (boolean) input;
            //restore = true;
            //System.out.println("Reading intent to restore: " + restore);
            view.currentRestoreState = RestoreState.PUBLISH_RESTORE;
            view.currentRestoreState.execute(restore);
        }
    },
    PUBLISH_RESTORE{
        /**
         * communicates to the local controller the intention
         * regarding the restoration of a saved game
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            boolean intentToRestore = (boolean) input;
            //System.out.println("Publishing intent to restore: " + intentToRestore);

            koState = RestoreState.ASK_RESTORE;
            okState = RestoreState.REQUEST_RESTORE;
            koInput = null;
            okInput = null;
            view.currentRestoreState = RestoreState.RECEIVE_CHECK;

            view.getController().restore(view.getId(), intentToRestore);
        }
    },
    RECEIVE_CHECK{
        /**
         * depending on the input, goes to and executes a preset state.
         * Before moving to this state, the states and inputs for
         * success and failure should be defined
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            String problem = (String) input;
            boolean success = (problem == null);

            RestoreState nextState;
            Object nextInput;

            if(success){
                nextState = okState;
                nextInput = okInput;
            }
            else{
                nextState = koState;
                nextInput = koInput;
            }

            koState = null;
            okState = null;
            koInput = null;
            okInput = null;

            view.currentRestoreState = nextState;
            view.currentRestoreState.execute(nextInput);
        }
    },
    RECEIVE_RESTORE{
        /**
         * if there is the intent to restore, moves to accept info regarding the saved game
         * else moves to skip the Restore Phase
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            boolean restorationHappens = (boolean) input;
            if(restorationHappens){
                //System.out.println("The saved game will be restored");
                view.currentRestoreState = RestoreState.RECEIVE_ALL;
            }
            else{
                //System.out.println("The saved game won't be restored");
                view.currentRestoreState = RestoreState.SKIP_RESTORE;
                view.currentRestoreState.execute(null);
            }
        }
    },
    REQUEST_RESTORE{
        /**
         * requests to the local controller whether a saved game will be restored
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            view.currentRestoreState = RestoreState.RECEIVE_RESTORE;
            view.getController().willRestore();
        }
    },
    RECEIVE_ALL{
        /**
         *
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            // when the appropriate message is received, ends the restore phase
            //System.out.println("Resuming the game");
            view.currentRestoreState = RestoreState.END_RESTORE;
            view.currentRestoreState.execute(null);
        }
    },
    SKIP_RESTORE{
        /**
         * skips the Restore Phase and goes to the Setup Phase
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            //System.out.println("Ending restore phase, starting setup phase");
            view.startSetupFSM();
        }
    },
    END_RESTORE{
        /**
         * ends a successful restoration and resumes from the Game FSM
         * @param input any information that the state requires to execute
         */
        public void execute(Object input){
            //System.out.println("Ending restore phase, resuming game phase");
            view.startGameFSM();
        }
    };

    static ClientView view;

    private static RestoreState koState = null;
    private static RestoreState okState = null;
    private static Object koInput = null;
    private static Object okInput = null;

    /**
     * method for the execution of a state.
     * @param input any information that the state requires to execute
     */
    public abstract void execute(Object input);
}
