package it.polimi.ingsw.model;

public class Worker {
    private Sex sex;
    private Player player;
    private Space space;

    public Worker(Sex sex, Player player) {
        this.sex = sex;
        this.player = player;
        this.space = null;
    }

    public Sex getSex() {
        return sex;
    }

    public Player getPlayer() {
        return player;
    }

    public Space getSpace() {
        return space;
    }

    void setSpace(Space space) {
        this.space = space;
    }

    @Override
    public String toString(){
        return "P"+player.getPlayerNum()+"-"+sex.name().charAt(0);
    }
}