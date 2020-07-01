package it.polimi.ingsw.exception;

/**
 * A runtime exception dealing with the state of an object being full.
 */
public class AlreadyFullException extends RuntimeException
{
    /**
     * Exception informing the caller that the a generic object is already full.
     * @param message the detailed exception message.
     */
    public AlreadyFullException(String message)
    {
        super(message);
    }
}
