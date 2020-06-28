package it.polimi.ingsw.model;

public class Worker {
    private Sex sex;
    private int playerId;
    private Space space;

    /**
     * Constructs a worker, given its sex and the player it belongs to
     * @param sex the sex of the worker
     * @param player the player that the worker belongs to
     */
    public Worker(Sex sex, Player player) {
        this.sex = sex;
        this.playerId = player.getId();
        this.space = null;
    }

    /**
     * returns the sex of the worker
     * @return the sex of the worker
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * returns the player that the worker belongs to
     * @return the player that the worker belongs to
     */
    public int getPlayer() {
        return playerId;
    }

    /**
     * returns the space that the worker is on
     * @return the space that the worker is on
     */
    public Space getSpace() {
        return space;
    }

    /**
     * sets the worker on a given space
     * @param space the space on which to set the worker
     */
    void setSpace(Space space) {
        this.space = space;
    }

    /**
     * returns a string representing the worker,
     * composed of the id of its player and the sex of the worker
     * @return a string representing the worker
     */
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

    /**
     * compares all the fields of the workers and returns true if they all match
     * @param o the other worker
     * @return true if all the fields of the workers are equal
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Worker)) return false;
        Worker that = (Worker) o;
        return this.playerId == that.playerId &&
                this.sex.equals(that.sex) &&
                ( (this.space == null && that.space == null) ||
                        this.space.equals(that.space)
                );
    }
}
