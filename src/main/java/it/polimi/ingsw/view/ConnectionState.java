package it.polimi.ingsw.view;

import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.Game;

import javax.swing.plaf.IconUIResource;
import java.util.Scanner;

//TODO implement remaining states
//TODO add ID_ACK state
public enum ConnectionState {
    REQUEST_ID {
        //publishes the request for an id
        public void execute(Object input) {
            System.out.println("Richiedo ID");
            view.viewRequestsFeed.notifyRequestID();
            view.currentConnectionState = view.currentConnectionState.next();
        }
    },
    READ_ID {
        //sets the id
        public void execute(Object input) {
            int id = (int) input;
            System.out.println("My id is: " + id);
            view.setID(id);
            if(id == 0) {
                view.currentConnectionState = view.currentConnectionState.next();
                view.currentConnectionState.execute(null);
            }else
                view.currentConnectionState = ConnectionState.READ_NUM_PLAYERS;


        }
    },
    ASK_NUM_PLAYERS{
        public void execute(Object input)  {
            view.numPlayersView();
        }
    },
    PUBLISH_NUM_PLAYERS{
        public void execute(Object input) {
            int numOfPlayer = (int)input;
            System.out.println("publish number of player "+numOfPlayer);
            view.viewGameFeed.notifyNumPlayers(numOfPlayer);
            view.currentConnectionState = view.currentConnectionState.next();
        }
    },
    //TODO add custom IncorrectStateException
    READ_NUM_PLAYERS{
        public void execute(Object input){
            if(view.game != null)
                throw new IncorrectStateException("game should not exist in state" + this.name());
            System.out.println("Creo il game da "+input+" giocatori");
            int numPlayers = (int) input;
            view.game = new Game(numPlayers);
            view.currentConnectionState = view.currentConnectionState.next();
            view.currentConnectionState.execute(input);
        }
    },
    PUBLISH_NAME,
    END;


    private static ClientView view = ClientView.instance();

    //default implementation of next, returns the next enum instance in order, or null if the FSM has terminated
    public ConnectionState next(){
        if(ordinal()==ConnectionState.values().length-1)
            return null;
        else
            return ConnectionState.values()[ this.ordinal()+1 ];
    }

    //default implementation of the body of the state, does nothing
    //TODO change to abstract when all the states are implemented
    public void execute(Object input){

    }

    }
