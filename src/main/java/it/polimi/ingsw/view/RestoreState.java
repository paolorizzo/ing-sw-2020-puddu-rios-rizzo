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
            //TODO implement logic based onn availability
            view.currentRestoreState = RestoreState.END_RESTORE;
            view.currentRestoreState.execute(null);
        }
    },
    END_RESTORE{
        public void execute(Object input){
            view.startSetupFSM();
        }
    };

    static ClientView view;
    public abstract void execute(Object input);
}
