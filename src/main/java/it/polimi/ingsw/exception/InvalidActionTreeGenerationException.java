package it.polimi.ingsw.exception;

/**
 * A runtime exception dealing with encountered in the creation of the action tree.
 */
public class InvalidActionTreeGenerationException extends RuntimeException
{
    /**
     * Exception informing the caller that a problem occurred in the generation of the action tree.
     * @param message the detailed exception message.
     */
    public InvalidActionTreeGenerationException(String message)
    {

        super(message);
    }
}
