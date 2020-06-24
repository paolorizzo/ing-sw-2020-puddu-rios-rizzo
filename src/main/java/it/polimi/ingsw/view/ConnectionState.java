package it.polimi.ingsw.view;

/**
 * Finite State Machine on the client side for the Connection Phase,
 * which goes from the actual connection to the Server,
 * to the choice or receival of the number of players,
 * to the choice of name for every player
 */
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
    ASK_NUM_PLAYERS{
        /**
         * calls on the UI to get the number of players for the game from user input
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentConnectionState = READ_NUM_PLAYERS;
            view.getUi().askNumPlayers();
        }
    },
    READ_NUM_PLAYERS{
        /**
         * checks the validity of the number of players, and either publishes it or
         * goes to get it again
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
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
        /**
         * sets the number of players on the local controller, which is the client
         * the client will then forward it through the connection
         * Sets up the receive_check state to receive the feedback for the communication
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
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
        /**
         * requests the number of players from the local controller, which is the Client
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input)  {
            //todo launch waiting view
            view.getController().getNumPlayers();
            view.currentConnectionState = RECEIVE_NUM_PLAYERS;
        }
    },
    RECEIVE_NUM_PLAYERS{
        /**
         * if the number of players excludes the possibility of the client
         * being legal for the game, it starts the process of killing itself.
         * Otherwise, the number of players is memorized and the connection continues
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
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
        /**
         * requests whether or not all players are connected to the local controller
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input){
            //System.out.println("Attendo tutti i giocatori ...");
            view.getController().requestAllPlayersConnected();
            view.currentConnectionState = RECEIVE_ALL_PLAYERS_CONNECTED;
        }
    },
    RECEIVE_ALL_PLAYERS_CONNECTED{
        /**
         * having acknowledged that all players are connected,
         * the ConnectionFSM continues to its next state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input){
            //System.out.println("Tutti i giocatori ora connessi.");
            view.currentConnectionState = ConnectionState.ASK_NAME;
            view.currentConnectionState.execute(view, input);
        }
    },
    ASK_NAME{
        /**
         * calls on the ui to get the name from user input
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            view.currentConnectionState = READ_NAME;
            view.getUi().askUsername();
        }
    },
    READ_NAME{
        /**
         * accepts the name and goes to the publishing state
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            String name = (String)input;
            //System.out.println("READ_NAME "+name);
            view.currentConnectionState = PUBLISH_NAME;
            view.currentConnectionState.execute(view, name);
        }
    },
    PUBLISH_NAME{
        /**
         * sets the name on the local controller
         * the Client, being the local controller, sends it through the connection to
         * the server
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
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
        /**
         * awaits the name of all the players
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("Wait all players name");
        }
    },
    PUBLISH_HARAKIRI{
        /**
         * publishes the fact that this client will kill itself
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("Communicating shutdown");
            view.getController().deleteId(view.getId());
            view.currentConnectionState = ConnectionState.HARAKIRI;
            view.currentConnectionState.execute(view, input);
        }
    },
    HARAKIRI{
        /**
         * calls on the local controller to kill the client
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
        public void execute(ClientView view, Object input) {
            //System.out.println("Ho preso il covid-19");
            view.getController().kill();
        }
    },
    END{
        /**
         * starts the next FSM, which is the Restore FSM
         * @param view the view on which to act
         * @param input the input for the execution of the state
         */
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
