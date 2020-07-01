package it.polimi.ingsw.view.middleware;

/**
 * Interface to be implemented on all network handlers of the middleware.
 * Contains methods related to the opening and closing of a connection.
 */
public interface NetworkInterface
{
    /**
     * Set the ip address of the requested server to start the connection.
     * @param ip the string storing the ip address.
     */
    void setIp(String ip);

    /**
     * Set the port of the requested server to start the connection.
     * @param port the number of the port.
     */
    void setPort(int port);

    /**
     * Retrieves the ip address of the connected server.
     * @return the ip address as a String.
     */
    String getIp();

    /**
     * Retrieves the port of the connected server.
     * @return the port number.
     */
    int getPort();

    /**
     * High level interface to trigger the procedure of closing the connection.
     */
    void closeConnection();
}
