package it.polimi.ingsw.model;

//TODO test Game
//TODO this class should become a GameObservable, and other observables should be incorporated in other classes
//that actually modify what is later notified
//this class should always call notifies upon any modification
public class Game {
    private Model model;
    private int numPlayers;

    public Game(){
    }

    public Game(int numPlayers){
        this();
        setNumPlayers(numPlayers);
    }

    public void setNumPlayers(int numPlayers) {
        System.out.println("The number of players has been set on the model: " + numPlayers);
        this.numPlayers = numPlayers;
        Model.instance().getGameFeed().notifyNumPlayers(numPlayers);
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
