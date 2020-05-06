package it.polimi.ingsw.view;

import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.middleware.Connection;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//TODO implement remaining states
//TODO add ID_ACK state
public enum ConnectionState {
    READY{
        public void execute(ClientView view, Object input) {
            //System.out.println("starting the fsm");
            view.currentConnectionState = REQUEST_ID;
            view.currentConnectionState.execute(view, null);
        }
    },
    REQUEST_ID {
        //publishes the request for an id
        public void execute(ClientView view, Object input) {
            //System.out.println("Richiedo ID");
            view.getController().generateId();
            view.currentConnectionState = RECEIVE_ID;
        }
    },
    RECEIVE_ID {
        //sets the id
        public void execute(ClientView view, Object input) {
            int id = (int) input;
            //System.out.println("My id is: " + id);
            view.setID(id);
            view.currentConnectionState = ACK_ID;
            view.currentConnectionState.execute(view, id);
        }
    },
    //decides the branch of the fsm to take based on the id
    ACK_ID {
        //sends ack of the reception of the id to the controller
        //this is used to synchronize the connection of the different clients
        public void execute(ClientView view, Object input) {
            int id = (int) input;
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
            boolean success = (boolean) input;

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
            view.startSetupFSM();
        }
    };

    private static ConnectionState koState = null;
    private static ConnectionState okState = null;
    private static Object koInput = null;
    private static Object okInput = null;

    //default implementation of the body of the state, does nothing
    //TODO change to abstract when all the states are implemented
    public void execute(ClientView view, Object input){

    }

}
