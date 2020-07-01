package it.polimi.ingsw.view.middleware;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The mean of communication in the propagation of method calls through the architecture.
 */
public class Message implements Serializable
{
    private String methodName;
    private List<Object> args;

    /**
     * Constructs the message.
     * @param methodName the triggered method's name.
     */
    public Message(String methodName)
    {
        this.methodName = methodName;
        this.args = new ArrayList<>();
    }

    /**
     * Constructs the message.
     * @param methodName the triggered method's name.
     * @param args the arguments of the call.
     */
    public Message(String methodName, List<Object> args)
    {
        this.methodName = methodName;
        this.args = args;
    }

    /**
     * Adds a generic object as argument of the message.
     * @param o a generic object.
     */
    public void addArg(Object o)
    {

        args.add(o);
    }

    /**
     * Checks if the message has any arguments.
     * @return a boolean value that is true when the message has arguments.
     */
    public boolean hasArgs()
    {

        return args.size() > 0;
    }

    /**
     * Retrieves the number of arguments of the message.
     * @return the number of arguments of the message.
     */
    public int numberOfArgs()
    {

        return args.size();
    }

    /**
     * Retrieves the name of the method associated to the call stored in the message.
     * @return the method's name.
     */
    public String getMethodName()
    {

        return methodName;
    }

    /**
     * Gets a specific argument of the message.
     * @param i the index of the requested argument.
     * @return the requested argument as object.
     */
    public Object getArg(int i)
    {

        return args.get(i);
    }

    /**
     * Returns the list of the arguments added to the message.
     * @return the arguments list.
     */
    public List<Object> getArgsList()
    {
        List<Object> args = new ArrayList<Object>();
        if(hasArgs())
        {
            for(int i = 0; i<numberOfArgs(); i++)
            {
                args.add(getArg(i));
            }
        }
        return args;
    }

    /**
     * Standard override of the toString method for the message.
     * @return the string equivalent of the message.
     */
    @Override
    public String toString()
    {
        return "Message{" +
                "methodName='" + methodName + '\'' +
                ", args=" + args +
                '}';
    }
}

