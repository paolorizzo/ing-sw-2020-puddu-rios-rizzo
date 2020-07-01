package it.polimi.ingsw.view.middleware;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 *  Contains all the methods related to the exchange of messages between the agents of the middleware.
 */
public abstract class Messenger
{
    /**
     * Retrieves the reference to the subject of the method call.
     * @param methodName the triggered method name.
     * @return the instance of the observable object.
     */
    protected abstract Object getObservable(String methodName);

    /**
     * High level interface to achieve the sending of a message.
     * @param methodName the triggered method name.
     * @param arg a variable number of generic arguments.
     */
    public abstract void sendMessage(String methodName, Object ...arg);

    /**
     * Low level sendMessage, simply serializes and sends a Message object through an output stream.
     * @param outByte the socket output stream associated with the communication.
     * @param msg the message to be sent.
     */
    public void sendMessage(ObjectOutputStream outByte, Message msg) throws IOException
    {
            outByte.writeObject(msg);
            outByte.flush();
    }

    /**
     * Masks complexity in sending messages.
     * @param outByte the socket output stream associated with the communication.
     * @param methodName the triggered method name.
     * @param args the list of arguments of the call.
     * @throws IOException in case of errors while writing on the socket.
     */
    public void sendMessage(ObjectOutputStream outByte, String methodName, List<Object> args) throws IOException
    {
        sendMessage(outByte, new Message(methodName, args));
    }

    /**
     * Masks complexity in sending messages.
     * @param outByte the socket output stream associated with the communication.
     * @param methodName the triggered method name.
     * @param arg a variable amount of arguments of the call.
     * @throws IOException in case of errors while writing on the socket.
     */
    public void sendMessage(ObjectOutputStream outByte, String methodName, Object ...arg) throws IOException
    {
        List<Object> args = Arrays.asList(arg);
        sendMessage(outByte, new Message(methodName, args));
    }

    /**
     * Retrieves the object on which to call the method using the methodMap, and delegates the actual call to the low
     * level callMethod.
     * @param methodName the triggered method name.
     * @param args the list of arguments of the call.
     */
    public void callMethod(String methodName, List<Object> args)
    {
        Object target = getObservable(methodName);
        callMethod(target, methodName, args);
    }

    /**
     * High level interface to achieve the call method stored in the message through reflection.
     * Unpacks the message and delegates the operation to the lower level callMethod.
     * @param msg the message storing the requested method call.
     */
    public void callMethod(Message msg)
    {
        callMethod(msg.getMethodName(), msg.getArgsList());
    }

    /**
     * Low level implementation of reflection to call a method on a target object.
     * @param target the object to call the method on.
     * @param methodName the name of the method to call.
     * @param args the list of arguments of the call.
     */
    private void callMethod(Object target, String methodName, List<Object> args)
    {
        Method[] possibleMethods = target.getClass().getMethods();
        for(Method method:possibleMethods){
            if(method.getName().equals(methodName)){
                Object[] methodArgs = args.toArray();
                try
                {
                    method.invoke(target, methodArgs);
                }
                catch(IllegalAccessException e)
                {
                    System.err.println("Error in invoking the method, IllegalAccessException");
                }
                catch(InvocationTargetException e)
                {
                    Throwable InterruptedException = e.getCause();
                    System.err.println("Error in invoking the method, InvocationTargetException ");
                    e.printStackTrace();
                }
                catch(NullPointerException e)
                {
                    System.err.println("Error in invoking the method, NullPointerException");
                }
            }
        }
    }
}
