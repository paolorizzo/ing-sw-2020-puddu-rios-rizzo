package it.polimi.ingsw.model;

import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player
{
    private final String nickname;
    private final int playerNum;
    private final Color color;
    private Card card;
    private Worker[] workers;

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

    public String getNickname()
    {
        return nickname;
    }

    public Color getColor()
    {
        return color;
    }

    public Worker getWorker(Sex sex)
    {
        return workers[sex.ordinal()];
    }

    public int getPlayerNum()
    {
        return playerNum;
    }

    //allows outside code to refer to playerNum as id in a coherent way
    public int getId(){
        return getPlayerNum();
    }

    public Card getCard(){ return card; }

    public void setCard(Card card){
        this.card = card;
    }

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

    public ActionTree generateActionTree(Board board){
        return card.getPowerStrategy().generateActionTree(board, this);
    }
    public void pruneOtherActionTree(Board board, Player other, Turn myLastTurn, ActionTree otherActionTree){
        card.getPowerStrategy().pruneOtherActionTree(board, this, other, myLastTurn, otherActionTree);
    }
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
