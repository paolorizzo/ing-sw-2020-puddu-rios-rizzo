package it.polimi.ingsw.model;

import java.util.Objects;

public class Player
{
    private String nickname;
    private int playerNum;
    private Color color;
    private Card card;
    private Worker[] workers;

    public Player(String nickname, Color color, int playerNum)
    {
        this.nickname = nickname;
        this.color = color;
        this.workers = new Worker[2];
        this.workers[1] = new Worker(Sex.MALE, this);
        this.workers[0] = new Worker(Sex.FEMALE, this);
        this.playerNum = playerNum;
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
