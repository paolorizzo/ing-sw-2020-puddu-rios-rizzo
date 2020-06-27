package it.polimi.ingsw.view.middleware;

public interface NetworkInterface
{
    void setIp(String ip);
    void setPort(int port);
    void closeConnection();
}
