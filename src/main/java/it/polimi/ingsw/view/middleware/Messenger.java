package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.observation.Observable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//TODO test
//TODO many aspects common to subclasses should be moved here socket

public abstract class Messenger
{
    protected Map<String, Observable> methodMap;

    protected Object getObservable(String methodName)
    {
        try{
            return methodMap.get(methodName);
        }catch(Exception e){
            System.err.println("Can't find the method called "+methodName);
        }
        return null;
    }

    /**
     * Low level sendMessage, simply serializes and sends a Message object through an output stream
     * @param outByte the socket output stream associated with the communication.
     * @param msg the message to be sent.
     */
    public void sendMessage(ObjectOutputStream outByte, Message msg)
    {
        try
        {
            outByte.writeObject(msg);
            outByte.flush();
        }
        catch(IOException e)
        {
            System.err.println("Errore IOE nell'update del Client");
        }
    }

    //masks complexity, generic for a list of many arguments
    public void sendMessage(ObjectOutputStream outByte, String methodName, List<Object> args)
    {
        sendMessage(outByte, new Message(methodName, args));
    }

    //useful to send messages with a variable amount of arguments
    public void sendMessage(ObjectOutputStream outByte, String methodName, Object ...arg)
    {
        List<Object> args = Arrays.asList(arg);
        sendMessage(outByte, new Message(methodName, args));
    }

    //messages travel in the form of updates
    //since the method we want to call on the observable is a notify
    //we have to convert from the name of an update
    //to the name of a notify
    //this system relies on the consistence of naming patterns between observers and observables
    public String toNotify(String update)
    {
        return update.replace("update", "notify");
    }

    //sanitizes the methodName to make sure that it is a notify, before calling callNotify
    //retrieves the object on which to call the method using the methodMap
    //through the service method getObservable
    public void callMethod(String methodName, List<Object> args)
    {
        if(methodName.contains("update"))
            methodName = toNotify(methodName);

        Object target = getObservable(methodName);
        callNotify(target, methodName, args);
    }

    //wrapper on callMethod, makes it easier to use it by encapsulating the logic needed to unpack the Message
    public void callMethod(Message msg)
    {
        callMethod(msg.getMethodName(), msg.getArgsList());
    }

    //actually calls the notify
    //strongly relies on someone else to sanitize the methodName to make sure it is a notify
    //also relies on wrapper functions to get the target object
    //TODO: check the correspondence of arguments
    //TODO perhaps there is a more efficient way to get the method from the name? some Class.getMethod(String name)?
    private void callNotify(Object target, String methodName, List<Object> args)
    {
        Method[] possibleMethods = target.getClass().getMethods();
        for(Method method:possibleMethods){
            if(method.getName().equals(methodName)){
                Object[] methodArgs = args.toArray();
                try
                {
                    //System.out.println("Invoke "+methodName+" on "+target);
                    method.invoke(target, methodArgs);
                }
                catch(IllegalAccessException e)
                {
                    System.err.println("Error in invoking the method, IllegalAccessException");
                }
                catch(InvocationTargetException e)
                {
                    System.err.println("Error in invoking the method, IllegalTargetException");
                }
                catch(NullPointerException e)
                {
                    System.err.println("Error in invoking the method, NullPointerException");
                }
            }

        }
    }

    protected abstract Map<String, Observable> constructMethodMap();
}
