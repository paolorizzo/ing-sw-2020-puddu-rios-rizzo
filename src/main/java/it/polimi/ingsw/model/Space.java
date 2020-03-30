package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

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
    }

    void addAdjacentSpace(Space s){
        adjacentSpaces.add(s);
    }
    List<Space> getAdjacentSpaces(){
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
        else
            pieces.add(p);
    }

    //to undo action
    void removeLastPiece(){
        if(pieces.size() == 1)
            throw new IllegalArgumentException();
        pieces.remove(pieces.size()-1);
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
}
