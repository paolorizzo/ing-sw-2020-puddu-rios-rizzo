package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observation.GameObservable;
import it.polimi.ingsw.observation.PlayersObservable;
import it.polimi.ingsw.observation.UserInterfaceObservable;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.middleware.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Cli extends UserInterfaceObservable implements UserInterface
{
    private HashMap<Integer, Player> players;
    public Cli(){
        players = new HashMap<>();
    }
    @Override
    public void showLogo()
    {
        CliUtils.printBlueOnWhite("      ______    ______   __    __  ________  ______   _______   ______  __    __  ______     ");
        CliUtils.printBlueOnWhite("     /      \\  /      \\ /  \\  /  |/        |/      \\ /       \\ /      |/  \\  /  |/      |    ");
        CliUtils.printBlueOnWhite("    /$$$$$$  |/$$$$$$  |$$  \\ $$ |$$$$$$$$//$$$$$$  |$$$$$$$  |$$$$$$/ $$  \\ $$ |$$$$$$/     ");
        CliUtils.printBlueOnWhite("    $$ \\__$$/ $$ |__$$ |$$$  \\$$ |   $$ |  $$ |  $$ |$$ |__$$ |  $$ |  $$$  \\$$ |  $$ |      ");
        CliUtils.printBlueOnWhite("    $$      \\ $$    $$ |$$$$  $$ |   $$ |  $$ |  $$ |$$    $$<   $$ |  $$$$  $$ |  $$ |      ");
        CliUtils.printBlueOnWhite("     $$$$$$  |$$$$$$$$ |$$ $$ $$ |   $$ |  $$ |  $$ |$$$$$$$  |  $$ |  $$ $$ $$ |  $$ |      ");
        CliUtils.printBlueOnWhite("    /  \\__$$ |$$ |  $$ |$$ |$$$$ |   $$ |  $$ \\__$$ |$$ |  $$ | _$$ |_ $$ |$$$$ | _$$ |_     ");
        CliUtils.printBlueOnWhite("    $$    $$/ $$ |  $$ |$$ | $$$ |   $$ |  $$    $$/ $$ |  $$ |/ $$   |$$ | $$$ |/ $$   |    ");
        CliUtils.printBlueOnWhite("     $$$$$$/  $$/   $$/ $$/   $$/    $$/    $$$$$$/  $$/   $$/ $$$$$$/ $$/   $$/ $$$$$$/     ");
        CliUtils.printBlueOnWhite("                                                                                             ");
        System.out.println(AnsiColors.ANSI_RESET);
    }

    @Override
    public void askNumPlayers(){
        CliUtils.printBlueOnWhiteSameLine("Choose the number of players:");
        int numPlayers = readInt();
        notifyReadNumPlayers(numPlayers);
    }



    @Override
    public void askUsername() {
        CliUtils.printBlueOnWhiteSameLine("Choose a username:");
        String name = readString();
        notifyReadName(name);
    }

    @Override
    public void askCard(Deck deck) {
        List<Card> cards = deck.getCards();
        for(Card card: cards){
            CliUtils.printBlueOnWhiteSameLine(card.getNum()+" "+card.getName());
            System.out.println("\n");
        }
        CliUtils.printBlueOnWhiteSameLine("Choose a number card: ");
        int numCard = readInt();
        notifyReadNumCard(numCard);
    }

    @Override
    public void askGod(List<Card> cards) {
        for(Card card: cards){
            CliUtils.printBlueOnWhiteSameLine(card.getNum()+" "+card.getName());
            System.out.println("\n");
        }
        CliUtils.printBlueOnWhiteSameLine("Choose a number card: ");
        int numCard = readInt();
        notifyReadGod(numCard);
    }

    @Override
    public void askSetupWorker(List<Action> possibleActions) {
        int cont = 0;
        for(Action a: possibleActions){
            CliUtils.printBlueOnWhiteSameLine(cont+" "+a.toString());
            cont++;
            System.out.println("\n");
        }
        int num = readInt();
        notifyReadAction(possibleActions.get(num));
    }
    @Override
    public void askAction(List<Action> possibleActions, boolean canEndOfTurn) {
        boolean repeat = false;
        do{

            int cont = 0;
            for(Action a: possibleActions){
                CliUtils.printBlueOnWhiteSameLine(cont+" "+a.toString());
                cont++;
                System.out.println("\n");
            }
            if(canEndOfTurn){
                CliUtils.printBlueOnWhiteSameLine(cont+" End Turn");
            }
            int num = readInt();
            if(num>=0 && num<cont)
                notifyReadAction(possibleActions.get(num));
            else if(num == cont)
                notifyReadVoluntaryEndOfTurn();
            else
                repeat = true;
        }while(repeat);
    }
    @Override
    public void removeWorkersOfPlayer(int id){

    }
    //wrong input is handled by FSM

    private int readInt(){
        Scanner stdin = new Scanner(System.in);
        int num = stdin.nextInt();
        System.out.println();
        return num;
    }

    //utility function used to get a string from user input
    public String readString(){
        Scanner stdin = new Scanner(System.in);
        String word = stdin.nextLine();
        System.out.println();
        return word;

    }

    public void showCustomError(String s)
    {
        CliUtils.printRedOnWhite(s);
        System.out.println();
    }

    public void showUsernameError()
    {
        showCustomError("Error! Username not available.");
    }

    @Override
    public void setNumPlayers(int numPlayers) {
    }
    @Override
    public void registerPlayer(int id, String name){
        players.put(id, new Player(id, name));
    }
    @Override
    public void registerGod(int id, Card card){
        players.get(id).setCard(card);
    }
    @Override
    public void executeAction(Action action){
        //TODO execute action
    }
    @Override
    public int getNumPlayersRegister() {
        return players.size();
    }
}
