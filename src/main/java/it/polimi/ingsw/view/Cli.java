package it.polimi.ingsw.view;

import java.util.Scanner;

public class Cli implements UserInterface {

    public Cli(){

    }

    public int getNumPlayers(){
        System.out.println("Sei il primo player! Inserisci il numero di giocatori");
        Scanner in = new Scanner(System.in);
        int numPlayers = in.nextInt();
        return numPlayers;
    }
}
