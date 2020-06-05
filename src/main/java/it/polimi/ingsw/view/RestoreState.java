package it.polimi.ingsw.view;

public enum RestoreState {
    START_RESTORE{
        public void execute(Object input){
            view.currentRestoreState = RestoreState.REQUEST_GAME_AVAILABLE;
            view.currentRestoreState.execute(null);
        }
    },
    REQUEST_GAME_AVAILABLE{
        public void execute(Object input){
            view.getController().isGameAvailable();
            view.currentRestoreState = RestoreState.RECEIVE_GAME_AVAILABLE;
        }
    },
    RECEIVE_GAME_AVAILABLE{
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
        public void execute(Object input){
            view.currentRestoreState = RestoreState.READ_RESTORE;
            view.getUi().askRestore();
        }
    },
    READ_RESTORE{
        public void execute(Object input){
            boolean restore = (boolean) input;
            //restore = true;
            //System.out.println("Reading intent to restore: " + restore);
            view.currentRestoreState = RestoreState.PUBLISH_RESTORE;
            view.currentRestoreState.execute(restore);
        }
    },
    PUBLISH_RESTORE{
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
        public void execute(Object input){
            view.currentRestoreState = RestoreState.RECEIVE_RESTORE;
            view.getController().willRestore();
        }
    },
    RECEIVE_ALL{
        public void execute(Object input){
            // when the appropriate message is received, ends the restore phase
            //System.out.println("Resuming the game");
            view.currentRestoreState = RestoreState.END_RESTORE;
            view.currentRestoreState.execute(null);
        }
    },
    SKIP_RESTORE{
        public void execute(Object input){
            //System.out.println("Ending restore phase, starting setup phase");
            view.startSetupFSM();
        }
    },
    END_RESTORE{
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

    public abstract void execute(Object input);
}
