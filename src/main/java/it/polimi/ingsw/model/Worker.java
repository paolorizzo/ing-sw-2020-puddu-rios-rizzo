package it.polimi.ingsw.model;

public class Worker {
    private Sex sex;
    private int playerId;
    private Space space;

    public Worker(Sex sex, Player player) {
        this.sex = sex;
        this.playerId = player.getId();
        this.space = null;
    }

    public Sex getSex() {
        return sex;
    }

    public int getPlayer() {
        return playerId;
    }

    public Space getSpace() {
        return space;
    }

    void setSpace(Space space) {
        this.space = space;
    }

    @Override
    public String toString(){
        return "P"+playerId+"-"+sex.name().charAt(0);
    }

    protected Worker lightClone(Player copyPlayer){
        Worker copy = new Worker(sex, copyPlayer);
        if(space != null)
            copy.setSpace(space.lightClone());
        return copy;
    }
}
