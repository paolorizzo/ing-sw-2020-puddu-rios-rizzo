package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observation.UserInterfaceObservable;
import it.polimi.ingsw.view.UserInterface;

import java.util.HashMap;
import java.util.List;

public class Cli extends UserInterfaceObservable implements UserInterface
{
    private HashMap<Integer, Player> players;
    private int numPlayers;

    public Cli(){
        players = new HashMap<>();
    }

    //UI methods
    @Override
    public void showLogo()
    {


    }

    @Override
    public void askNumPlayers()
    {

        notifyReadNumPlayers(CliUtils.handleNumPlayerSelection());
    }

    @Override
    public void askUsername()
    {
        CliUtils.showUsernameDialog();
        System.out.println();

        String name = CliUtils.readString();
        CliUtils.slideDown(36,1);

        notifyReadName(name);
    }

    //TODO check not null
    @Override
    public void askCard(Deck deck)
    {
        List<Card> cards = deck.getCards();
        notifyReadNumCard(cards.get(CliUtils.handleCardSelection(cards)).getNum());
    }

    @Override
    public void askGod(List<Card> cards)
    {

        notifyReadGod(cards.get(CliUtils.handleCardSelection(cards)).getNum());
    }

    @Override
    public void askSetupWorker(List<Action> possibleActions)
    {
        int cont = 0;
        for(Action a: possibleActions){
            CliUtils.printBlueOnWhiteSameLine(cont+" "+a.toString());
            cont++;
            System.out.println("\n");
        }
        int num = CliUtils.readInt();
        notifyReadAction(possibleActions.get(num));
    }

    @Override
    public void askAction(List<Action> possibleActions, boolean canEndOfTurn)
    {
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
            int num = CliUtils.readInt();
            if(num>=0 && num<cont)
                notifyReadAction(possibleActions.get(num));
            else if(num == cont)
                notifyReadVoluntaryEndOfTurn();
            else
                repeat = true;
        }while(repeat);
    }

    @Override
    public void removeWorkersOfPlayer(int id)
    {

    }

    //gets and sets
    @Override
    public void setNumPlayers(int numPlayers)
    {

        this.numPlayers = numPlayers;
    }

    @Override
    public int getNumPlayers()
    {

        return numPlayers;
    }

    @Override
    public void registerPlayer(int id, String name)
    {
        players.put(id, new Player(id, name));
    }

    @Override
    public void registerGod(int id, Card card)
    {

        players.get(id).setCard(card);
    }

    @Override
    public void executeAction(Action action)
    {

        //TODO execute action
    }

    @Override
    public int getNumPlayersRegister()
    {

        return players.size();
    }
}
