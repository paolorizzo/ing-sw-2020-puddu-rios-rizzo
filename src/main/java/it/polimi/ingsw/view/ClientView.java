package it.polimi.ingsw.view;

import java.util.Scanner;

public class ClientView extends View
{
    @Override
    public void startNameView()
    {
        Scanner stdin = new Scanner(System.in);
        System.out.println("Insert username: ");
        String name = stdin.nextLine();
        notify(name);
    }

    @Override
    public void startNumberOfPlayersView()
    {
        Scanner stdin = new Scanner(System.in);
        System.out.println("Insert the desired number of players: ");
        String num = stdin.nextLine();
        notify(num+"Players");
    }

    @Override
    public void update(Object o)
    {

    }
}