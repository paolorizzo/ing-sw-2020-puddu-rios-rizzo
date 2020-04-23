package it.polimi.ingsw.exception;

public class AlreadyFullException extends RuntimeException{
    public AlreadyFullException(String message){
        super(message);
    }
}
