package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Space {

    private int posX, posY;
    private Worker worker;
    private List<Piece> pieces;
    private List<Space> adjacentSpaces;

    public Space(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        adjacentSpaces = new ArrayList<>();
        pieces = new ArrayList<>();
        addPiece(Piece.LEVEL0);
        worker = null;
    }

    void addAdjacentSpace(Space s){
        adjacentSpaces.add(s);
    }

    public List<Space> getAdjacentSpaces(){
        return adjacentSpaces;
    }

    public boolean hasWorkerOnIt(){
        return worker != null;
    }

    public Worker getWorkerOnIt(){
        return worker;
    }

    void setWorkerOnIt(Worker w){
        this.worker = w;
    }

    void removeWorkerOnIt(){
        this.worker = null;
    }

    void addPiece(Piece p){
        if(pieces.contains(p))
            throw new IllegalArgumentException("This space already contains" + p.toString());
        else if(UpperLevelInSpace(p))
            throw new IllegalArgumentException("Can't build over an upper level");
        else
            pieces.add(p);
    }

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

    //to undo action
    void removeLastPiece(){
        if(pieces.size() == 1)
            throw new IllegalArgumentException("Can't undo removing piece level 0");
        pieces.remove(pieces.size()-1);
    }
    public Piece getLastPiece(){
        return pieces.get(pieces.size()-1);
    }

    public int getLevel(){
        Piece last = pieces.get(pieces.size()-1);
        return last.getLevel();
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isFreeSpace(){
        return (!this.hasWorkerOnIt()) && (this.getLevel() != 4);
    }

    public boolean isPeripheralSpace(){
        return posX == 0 || posX == 4 || posY == 0 || posY == 4;
    }

    protected Space lightClone(){
        Space space = new Space(posX, posY);
        for(Piece piece: pieces)
            space.addPiece(piece);
        return space;
    }

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
