package it.polimi.ingsw.view;


public interface UserInterface
{
    void askNumPlayers();
    void askUsername();
    void showLogo();
    void registerPlayer(int id, String name);
    int getNumPlayersRegister();
}
