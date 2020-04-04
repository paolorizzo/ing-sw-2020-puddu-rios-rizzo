package it.polimi.ingsw.controller;

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
}

