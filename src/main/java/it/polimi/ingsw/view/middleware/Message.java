package it.polimi.ingsw.view.middleware;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable
{
    private String methodName;
    private List<Object> args;

    public Message(String methodName)
    {
        this.methodName = methodName;
        this.args = new ArrayList<Object>();
    }

    public Message(String methodName, List<Object> args){
        this.methodName = methodName;
        this.args = args;
    }

    public void addArg(Object o)
    {
        args.add(o);
    }

    public boolean hasArgs()
    {
        return args.size() > 0;
    }

    public int numberOfArgs()
    {
        return args.size();
    }

    public String getMethodName()
    {
        return methodName;
    }

    public Object getArg(int i)
    {
        return args.get(i);
    }

    public List<Object> getArgsList(){
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
}

