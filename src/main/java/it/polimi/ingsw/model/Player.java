package it.polimi.ingsw.model;

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
        this.workers[0] = new Worker(Sex.MALE, this);
        this.workers[1] = new Worker(Sex.FEMALE, this);
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
}
