package it.polimi.ingsw.model;

public class AlreadyFullException extends RuntimeException{
    public AlreadyFullException(String message){
        super(message);
    }
}
