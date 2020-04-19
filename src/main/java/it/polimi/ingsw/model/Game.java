package it.polimi.ingsw.model;

//TODO test Game
//TODO this class should become a GameObservable, and other observables should be incorporated in other classes
//that actually modify what is later notified
//this class should always call notifies upon any modification
public class Game {
    private Model model;
    private int numPlayers;

    public Game(){
        numPlayers = -1;
    }

    public Game(int numPlayers){
        this();
        setNumPlayers(numPlayers);
    }

    public boolean numPlayersIsSet(){
        return numPlayers != -1;
    }

    public void setNumPlayers(int numPlayers) {
        //System.out.println("Setting a game for " + numPlayers + " players");
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
