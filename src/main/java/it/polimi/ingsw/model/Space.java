package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents each tile on the board
 * Can be built upon with Pieces, and can have a Worker on
 */
public class Space {

    private int posX, posY;
    private Worker worker;
    private List<Piece> pieces;
    private List<Space> adjacentSpaces;

    /**
     * constructs a Space object given its position on the board
     * @param posX the x coordinate of the space
     * @param posY the y coordinate of the space
     */
    public Space(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        adjacentSpaces = new ArrayList<>();
        pieces = new ArrayList<>();
        addPiece(Piece.LEVEL0);
        worker = null;
    }

    /**
     * adds another space as adjacent to this one
     * @param s the other space
     */
    void addAdjacentSpace(Space s){
        adjacentSpaces.add(s);
    }

    /**
     * returns the list of spaces that are adjacent to this one
     * @return the list of spaces that are adjacent to this one
     */
    public List<Space> getAdjacentSpaces(){
        return adjacentSpaces;
    }

    /**
     * checks whether there is a worker on this space
     * @return true if there is a worker on this space
     */
    public boolean hasWorkerOnIt(){
        return worker != null;
    }

    /**
     * returns the worker on this space
     * returns null if there are no workers
     * @return the worker on this space. Can return null
     */
    public Worker getWorkerOnIt(){
        return worker;
    }

    /**
     * sets a worker on this space
     * @param w the worker to set
     */
    void setWorkerOnIt(Worker w){
        this.worker = w;
    }

    /**
     * removes the worker from the space, if it is there
     */
    void removeWorkerOnIt(){
        this.worker = null;
    }

    /**
     * adds a building level on top of this space, if
     * it is logically coherent to do so
     * @param p the piece to add
     */
    void addPiece(Piece p){
        if(pieces.contains(p))
            throw new IllegalArgumentException("This space already contains" + p.toString());
        else if(UpperLevelInSpace(p))
            throw new IllegalArgumentException("Can't build over an upper level");
        else
            pieces.add(p);
    }

    /**
     * checks whether this space already contains a building
     * of higher level than the given one
     * @param p the given building level
     * @return true if this space contains a building of higher
     * level than the given one p
     */
    private boolean UpperLevelInSpace(Piece p) {
        if (p != Piece.DOME && pieces.size() > 0)
        {
            for (Piece piece : Piece.values()) {
                if (piece.getLevel() > p.getLevel() && pieces.contains(piece))
                    return true;
            }
        }
        return false;
    }

    /**
     * removes the highest piece from the space
     */
    void removeLastPiece(){
        if(pieces.size() == 1)
            throw new IllegalArgumentException("Can't undo removing piece level 0");
        pieces.remove(pieces.size()-1);
    }

    /**
     * returns the highest piece on the space
     * @return the highest piece on the space
     */
    public Piece getLastPiece(){
        return pieces.get(pieces.size()-1);
    }

    /**
     * returns the level of the highest piece on the space
     * @return the level of the highest piece on the space
     */
    public int getLevel(){
        Piece last = pieces.get(pieces.size()-1);
        return last.getLevel();
    }

    /**
     * returns the x coordinate of the space
     * @return the x coordinate of the space
     */
    public int getPosX() {
        return posX;
    }

    /**
     * returns the y coordinate of the space
     * @return the y coordinate of the space
     */
    public int getPosY() {
        return posY;
    }

    /**
     * checks whether a worker can move on this space
     * @return true if a worker can move on this space
     */
    public boolean isFreeSpace(){
        return (!this.hasWorkerOnIt()) && (this.getLevel() != 4);
    }

    /**
     * checks whether this is a peripheral space
     * @return true if this is a peripheral space
     */
    public boolean isPeripheralSpace(){
        return posX == 0 || posX == 4 || posY == 0 || posY == 4;
    }

    /**
     * clones and returns another space, identical
     * except with regards to the adjacent space
     * @return a copy of this space, identical except
     * without its adjacent spaces
     */
    protected Space lightClone(){
        Space space = new Space(posX, posY);
        for(Piece piece: pieces)
            space.addPiece(piece);
        return space;
    }

    /**
     * compares another object to this space
     * @param o the other object
     * @return true if the other object is a Space
     * and its position matches that of this space
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Space)) return false;
        Space space = (Space) o;
        return posX == space.posX &&
                posY == space.posY;
    }

    /**
     * compares all the fields of the 2 spaces
     * @param that the other space against which to coampare this one
     * @return true if all the fields are equal, false otherwise
     */
    public boolean fullEquals(Space that){
        boolean equality = this.equals(that);
        equality &= (this.pieces.equals(that.pieces));
        equality &= (this.adjacentSpaces.equals(that.adjacentSpaces));
        if(this.worker == null)
            equality &= (that.worker == null);
        else
            equality &= (this.worker.equals(that.worker));
        return equality;
    }

    /**
     * returns the worker string if there is one,
     * or 4 spaces if there isn't
     * @return the worker identifier if there is a worker, 4 empty spaces otherwise
     */
    @Override
    public String toString(){
        if(hasWorkerOnIt())
            return getWorkerOnIt().toString();
        else
            return "    ";
    }

}
