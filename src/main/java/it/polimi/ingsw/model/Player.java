package it.polimi.ingsw.model;

import it.polimi.ingsw.view.View;

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
            this.workers[1] = new Worker(Sex.MALE, this);
            this.workers[0] = new Worker(Sex.FEMALE, this);
            this.playerNum = playerNum;
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
            this.workers[1] = new Worker(Sex.MALE, this);
            this.workers[0] = new Worker(Sex.FEMALE, this);
            this.playerNum = playerNum;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return playerNum == player.playerNum &&
                Objects.equals(nickname, player.nickname) &&
                color == player.color;
    }

}
