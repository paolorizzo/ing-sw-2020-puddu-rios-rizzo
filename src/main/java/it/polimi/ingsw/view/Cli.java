package it.polimi.ingsw.view;

import java.util.Scanner;

public class Cli implements UserInterface {
    private static Cli instance;

    private Cli(){

    }

    static Cli instance(){
        if(instance==null)
            instance = new Cli();
        return instance;
    }

    public int getNumPlayers(){
        System.out.println("Sei il primo player! Inserisci il numero di giocatori");
        Scanner in = new Scanner(System.in);
        int numPlayers = in.nextInt();
        return numPlayers;
    }
}