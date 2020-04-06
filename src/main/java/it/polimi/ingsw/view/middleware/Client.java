package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.*;
import java.lang.reflect.Method;

public class Client implements Observer, Runnable
{
    private Socket socket;
    private String ip;
    private int port;

    private ClientView cw;

    private ObjectInputStream ByteIn;
    private ObjectOutputStream outByte;

    public Client(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        this.cw = new ClientView();
        this.cw.addObserver(this);
    }

    // called by run()
    // gets the method name and a list or arguments, invokes the corrisponding method on the relative client view
    private void methodHandler(String name, List<Object> arg)
    {
        Method[] viewMethods = cw.getClass().getMethods();

        for(Method m : viewMethods)
        {
            if(m.getName().equals(name)) {

                //TODO: check the correspondence of arguments

                Object[] methodArgs = new Object[arg.size()];
                for (int i = 0; i < arg.size(); i++)
                    methodArgs[i] = arg.get(i);

                try
                {
                    m.invoke(cw, methodArgs);
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

    // client gets the updates coming from client view and directed to the controller
    // un update is represented by a Message calling the update method on the controller
    // the message object is serialized and sent through the socket stream
    public void update(Object o)
    {
        // moved the declaration of outByte from here. problems?

        Message m = new Message("update");
        m.addArg(o);
        try
        {
            outByte = new ObjectOutputStream(socket.getOutputStream());
            outByte.writeObject(m);
            outByte.flush();
        }
        catch(IOException e)
        {
            System.err.println("Errore IOE nell'update del Client");
        }
    }

    // handles the messages coming from the stream, de-serializing and calling methodHandler
    public void run()
    {
        try
        {
            socket = new Socket(ip, port);
            System.out.println("Connected to the server");

            // moved the declaration of byteIn from here. Problems?

            while(true)
            {

                try
                {
                    ByteIn  = new ObjectInputStream(socket.getInputStream());
                    List<Object> args = new ArrayList<Object>();;

                    Message message = (Message) ByteIn.readObject();
                    String s = message.getMethodName();

                    if(message.hasArgs())
                    {
                        for(int i = 0; i<message.numberOfArgs(); i++)
                        {
                            args.add(message.getArg(i));
                        }
                    }

                    methodHandler(s, args);
                }
                catch (ClassNotFoundException e)
                {
                    System.err.println("Error in reading the object");
                }
            }
        }
        catch(IOException e)
        {
            System.err.println("Error in creating the client socket");
        }

    }
}