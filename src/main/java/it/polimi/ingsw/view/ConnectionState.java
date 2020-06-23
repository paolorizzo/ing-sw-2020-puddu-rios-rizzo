package it.polimi.ingsw.view;

public enum ConnectionState {
    READY{
        /**
         * entry point for the Connection FSM, simply goes to the next state and executes it
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("starting the fsm");
            view.currentConnectionState = REQUEST_ID;
            view.currentConnectionState.execute(view, null);
        }
    },
    REQUEST_ID {
        /**
         * requests an id to the controller of the view and advances the state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("Richiedo ID");
            view.getController().generateId();
            view.currentConnectionState = RECEIVE_ID;
        }
    },
    RECEIVE_ID {
        /**
         * memorizes the id and executes the next state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            int id = (int) input;
            //System.out.println("My id is: " + id);
            view.setID(id);
            view.currentConnectionState = ACK_ID;
            view.currentConnectionState.execute(view, null);
        }
    },
    ACK_ID {
        /**
         * sends ack of the reception of the id to the controller
         * this is used to synchronize the connection of the different clients
         * depending on the id, decides the branch of the FSM to take
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            int id = view.getId();
            view.getController().ackId(id);
            if(id == 0) {
                view.currentConnectionState = ASK_NUM_PLAYERS;
                view.currentConnectionState.execute(view, null);
            }
            else if(id <= 2){
                view.currentConnectionState = ConnectionState.REQUEST_NUM_PLAYERS;
                view.currentConnectionState.execute(view, null);
            }
            else{
                view.currentConnectionState = ConnectionState.PUBLISH_HARAKIRI;
                view.currentConnectionState.execute(view, null);
            }
        }
    },
    //TODO should actually read the input to know whether to display a "try again"
    ASK_NUM_PLAYERS{
        public void execute(ClientView view, Object input) {
            view.currentConnectionState = READ_NUM_PLAYERS;
            view.getUi().askNumPlayers();
        }
    },
    READ_NUM_PLAYERS{
        public void execute(ClientView view, Object input) {
            int numPlayers = (int)input;
            //System.out.println("READ_NUM_PLAYERS "+numPlayers);
            if(numPlayers < 2 || 3 < numPlayers){
                view.currentConnectionState = ConnectionState.ASK_NUM_PLAYERS;
                view.currentConnectionState.execute(view, null);
            }
            else{
                view.currentConnectionState = PUBLISH_NUM_PLAYERS;
                view.currentConnectionState.execute(view, numPlayers);
            }
        }
    },
    PUBLISH_NUM_PLAYERS{
        public void execute(ClientView view, Object input) {
            int numPlayers = (int) input;
            //System.out.println("publishing number of players: "+numPlayers);
            view.getController().setNumPlayers(view.getId(), numPlayers);

            koState = ConnectionState.ASK_NUM_PLAYERS;
            okState = ConnectionState.REQUEST_NUM_PLAYERS;
            koInput = null;
            okInput = null;
            view.currentConnectionState = ConnectionState.RECEIVE_CHECK;
        }
    },
    RECEIVE_CHECK{
        public void execute(ClientView view, Object input) {
            String problem = (String) input;
            boolean success = (problem == null);

            ConnectionState nextState;
            Object nextInput;

            if(success){
                //System.out.println("operation successful");
                nextState = okState;
                nextInput = okInput;
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

            view.currentConnectionState = nextState;
            view.currentConnectionState.execute(view,nextInput);
        }
    },
    REQUEST_NUM_PLAYERS{
        public void execute(ClientView view, Object input)  {
            //todo launch waiting view
            view.getController().getNumPlayers();
            view.currentConnectionState = RECEIVE_NUM_PLAYERS;
        }
    },
    RECEIVE_NUM_PLAYERS{
        public void execute(ClientView view, Object input){
            //System.out.println("RECEIVE_NUM_PLAYERS state execute "+(int)input);
            int numPlayers = (int) input;
            if(view.getId() < numPlayers){
                //System.out.println("Creo il game da "+input+" giocatori.");
                view.setNumPlayers(numPlayers);
                view.getUi().setNumPlayers(numPlayers);
                view.currentConnectionState = ConnectionState.REQUEST_ALL_PLAYERS_CONNECTED;
                view.currentConnectionState.execute(view, null);
            }
            else{
                view.currentConnectionState = ConnectionState.PUBLISH_HARAKIRI;
                view.currentConnectionState.execute(view, input);
            }
        }
    },
    REQUEST_ALL_PLAYERS_CONNECTED{
        public void execute(ClientView view, Object input){
            //System.out.println("Attendo tutti i giocatori ...");
            view.getController().requestAllPlayersConnected();
            view.currentConnectionState = RECEIVE_ALL_PLAYERS_CONNECTED;
        }
    },
    RECEIVE_ALL_PLAYERS_CONNECTED{
        public void execute(ClientView view, Object input){
            //System.out.println("Tutti i giocatori ora connessi.");
            view.currentConnectionState = ConnectionState.ASK_NAME;
            view.currentConnectionState.execute(view, input);
        }
    },
    ASK_NAME{
        public void execute(ClientView view, Object input) {
            view.currentConnectionState = READ_NAME;
            view.getUi().askUsername();
        }
    },
    READ_NAME{
        public void execute(ClientView view, Object input) {
            String name = (String)input;
            //System.out.println("READ_NAME "+name);
            view.currentConnectionState = PUBLISH_NAME;
            view.currentConnectionState.execute(view, name);
        }
    },
    PUBLISH_NAME{
        public void execute(ClientView view, Object input) {
            String name = (String) input;
            //System.out.println("publishing name: " + name);
            view.getController().setName(view.getId(), name);

            koState = ConnectionState.ASK_NAME;
            okState = ConnectionState.WAIT_ALL_PLAYERS_NAME;
            koInput = null;
            okInput = null;
            view.currentConnectionState = ConnectionState.RECEIVE_CHECK;
        }
    },
    WAIT_ALL_PLAYERS_NAME{
        public void execute(ClientView view, Object input) {
            //System.out.println("Wait all players name");
        }
    },
    PUBLISH_HARAKIRI{
        public void execute(ClientView view, Object input) {
            //System.out.println("Communicating shutdown");
            view.getController().deleteId(view.getId());
            view.currentConnectionState = ConnectionState.HARAKIRI;
            view.currentConnectionState.execute(view, input);
        }
    },
    HARAKIRI{
        public void execute(ClientView view, Object input) {
            //System.out.println("Ho preso il covid-19");
            view.getController().kill();
        }
    },
    //this state must leave the client dead in order to allow late messages to be received
    //the next phase will begin with a specific notify from the controller
    END{
        public void execute(ClientView view, Object input){
            //System.out.println("Connection phase has ended for client " + view.getId());
            //view.startSetupFSM();
            view.startRestoreFSM();
        }
    };

    private static ConnectionState koState = null;
    private static ConnectionState okState = null;
    private static Object koInput = null;
    private static Object okInput = null;

    /**
     * default implementation of the method for the execution of a state
     * @param view the view on which to act
     * @param input the input for the execution of the state
     */
    public abstract void execute(ClientView view, Object input);

}
