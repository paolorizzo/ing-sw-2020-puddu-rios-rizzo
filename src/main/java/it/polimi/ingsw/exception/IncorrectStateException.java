package it.polimi.ingsw.exception;

/**
 * A runtime exception dealing with the state of an object being corrupted.
 */
public class IncorrectStateException extends RuntimeException
{
    /**
     * Exception informing the caller that the state of the object is not correct.
     * @param message the detailed exception message.
     */
    public IncorrectStateException(String message)
    {
        super(message);
    }
}
