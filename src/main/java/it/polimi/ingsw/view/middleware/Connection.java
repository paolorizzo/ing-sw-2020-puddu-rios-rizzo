package it.polimi.ingsw.view.middleware;

import it.polimi.ingsw.view.View;

import java.net.Socket;
import java.io.*;

public class Connection implements Runnable
{
    private Socket socket;

    private Server server;
    private View view;

    private ObjectOutputStream outByte;
    private ObjectInputStream ByteIn;

    public Connection(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
        this.view = null;
    }

    // called by the server, when setting the relative virtual view
    public void setView(View view)
    {
        this.view = view;
    }

    // serialize the message and forwards it through the socket stream
    // handles communications from the server to the client
    public synchronized void send(Message message)
    {
        try
        {
            outByte = new ObjectOutputStream(socket.getOutputStream());
            outByte.writeObject(message);
            outByte.flush();
        }
        catch(IOException e)
        {
            System.err.println("Error in serialization");
        }
    }

    // this runs in a separate thread on the server's side
    // handles communications from the client to the server
    public void run()
    {
        // moved the declaration of ByteIn from here. Problems?

        // intercepts the updates coming from client view and mirrors a notify call on the relative virtual view
        // in the case of the setup phase, sets the desired number of players on server's side
        while(true)
        {
            try
            {
                ByteIn = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ByteIn.readObject();

                if(message.getMethodName().equals("update"))
                {
                    view.notify(message.getArg(0));
                    if (message.getArg(0).equals("2Players"))
                    {
                        server.setNumberOfPlayers(2);
                    }
                    else if (message.getArg(0).equals("3Players"))
                    {
                        server.setNumberOfPlayers(3);
                    }
                }

            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Error in reading the object");
            }
            catch (IOException e)
            {
                close();
            }

        }
    }

    private void close()
    {
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
