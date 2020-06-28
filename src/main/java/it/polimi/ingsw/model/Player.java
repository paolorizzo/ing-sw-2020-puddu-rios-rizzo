package it.polimi.ingsw.model;

import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * represents a player. Can have a name, id, color, workers and a card
 */
public class Player
{
    private final String nickname;
    private final int playerNum;
    private final Color color;
    private Card card;
    private Worker[] workers;

    /**
     * constructs a player given its name, id and color of its workers
     * @param nickname the name of the player
     * @param color the color of the workers of the player
     * @param playerNum the id of the player
     */
    public Player(String nickname, Color color, int playerNum)
    {
        if(playerNum < 0 || 2 < playerNum){
            throw new IllegalArgumentException("cannot construct this player because id " + playerNum + "is unacceptable");
        }
            else{
            this.nickname = nickname;
            this.color = color;
            this.workers = new Worker[2];
            this.playerNum = playerNum;
            this.workers[1] = new Worker(Sex.MALE, this);
            this.workers[0] = new Worker(Sex.FEMALE, this);
        }
    }

    /**
     * constructs a player given their id and name
     * @param playerNum the id of the player
     * @param nickname the name if the player
     */
    public Player(int playerNum, String nickname){
        if(playerNum < 0 || 2 < playerNum){
            throw new IllegalArgumentException("cannot construct this player because id " + playerNum + "is unacceptable");
        }
        else{
            this.nickname = nickname;
            this.color = Color.values()[playerNum];
            this.workers = new Worker[2];
            this.playerNum = playerNum;
            this.workers[1] = new Worker(Sex.MALE, this);
            this.workers[0] = new Worker(Sex.FEMALE, this);
        }
    }

    /**
     * returns the name of the worker
     * @return the name of the worker
     */
    public String getNickname()
    {
        return nickname;
    }

    /**
     * returns the color of the workers of the player
     * @return the color of the workers of the player
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * returns the worker of the given sex that belongs to the player
     * @param sex the sex of the wanted worker
     * @return the worker of the given sex
     */
    public Worker getWorker(Sex sex)
    {
        return workers[sex.ordinal()];
    }

    /**
     * returns the id of the player
     * @return the id of the player
     */
    public int getPlayerNum()
    {
        return playerNum;
    }

    /**
     * returns the id of the player
     * identical to getPlayerNum, exists to allow
     * outside code to refer to the field playerNum as an id,
     * since that terminology is prevalent outisde of the model package
     * @return the id of the player
     */
    public int getId(){
        return getPlayerNum();
    }

    /**
     * returns the god card that the player has chosen
     * @return the god card that the player has chosen
     */
    public Card getCard(){ return card; }

    /**
     * sets a card as belonging to this player
     * @param card the card to be set
     */
    public void setCard(Card card){
        this.card = card;
    }

    /**
     * generates the possible setup actions for the worker
     * of the given sex
     * @param board the board of the game
     * @param sex the sex of the wanted worker
     * @return the list of possible setup actions for the requested worker
     */
    public List<Action> generateSetupActionsWorker(Board board, Sex sex){
        List<Action> possibleActions = new ArrayList<>();

        if(getWorker(sex).getSpace() != null)
            return null;

        Space [][] spaces = board.getSpaces();
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(!spaces[i][j].hasWorkerOnIt()){
                    possibleActions.add(new SetupAction(getWorker(sex).toString(), i, j));
                }
            }
        }
        return possibleActions;
    }

    /**
     * generates the action tree for this player at this time
     * @param board the board of the game
     * @return the tree of possible actions for this player
     */
    public ActionTree generateActionTree(Board board){
        return card.getPowerStrategy().generateActionTree(board, this);
    }

    /**
     * prunes other player's action trees, based on the effects
     * of the god this player possesses
     * @param board the game board
     * @param other the other player whose action tree must be pruned
     * @param myLastTurn the last turn of this player
     * @param otherActionTree the action tree of the other player
     */
    public void pruneOtherActionTree(Board board, Player other, Turn myLastTurn, ActionTree otherActionTree){
        card.getPowerStrategy().pruneOtherActionTree(board, this, other, myLastTurn, otherActionTree);
    }

    /**
     * compares this player to another object
     * @param o the other object
     * @return true id, name and color match
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return playerNum == player.playerNum &&
                Objects.equals(nickname, player.nickname) &&
                color == player.color;
    }

    /**
     * checks every field of that against every field of this
     * @param that the other player
     * @return true if all the fields match
     */
    public boolean fullEquals(Player that){
        boolean equality = this.equals(that);
        equality &= (this.card.equals(that.card));
        for(int i=0;i<2;i++){
            equality &= ( this.workers[i].equals(that.workers[i]) );
        }
        return equality;
    }

    /**
     * clones this player, producing a copy identical to this one
     * except for some of the references of the fields
     * @return a copy of this player, identical except for some references
     * inside the objects that the player object references
     */
    protected Player lightClone(){
        Player copy = new Player(nickname, color, playerNum);
        if(card != null)
            copy.setCard(card.clone());
        copy.workers = new Worker[2];
        if(this.workers[0] != null)
            copy.workers[0] = this.workers[0].lightClone(copy);
        if(this.workers[1] != null)
            copy.workers[1] = this.workers[1].lightClone(copy);
        return copy;
    }

}
