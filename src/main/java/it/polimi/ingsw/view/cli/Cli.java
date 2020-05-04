package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observation.UserInterfaceObservable;
import it.polimi.ingsw.view.UserInterface;

import java.util.HashMap;
import java.util.List;

//TODO check not null where indicated
public class Cli extends UserInterfaceObservable implements UserInterface
{
    private HashMap<Integer, Player> players;
    private int numPlayers;

    private String[][] workersMask;
    private int[][] board;

    public Cli()
    {
        players = new HashMap<>();
        workersMask = CliUtils.generateEmptyWorkersMask();
        board = CliUtils.generateEmptyBoard();
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

        notifyReadName(name);
    }

    @Override
    public void askCard(Deck deck)
    {

        notifyReadNumCard(deck.getCards().get(CliUtils.handleCardSelection(deck.getCards())).getNum());
    }

    @Override
    public void askGod(List<Card> cards)
    {

        notifyReadGod(cards.get(CliUtils.handleCardSelection(cards)).getNum());
    }

    @Override
    public void askSetupWorker(List<Action> possibleActions)
    {

        notifyReadAction(possibleActions.get(CliUtils.handleSetupWorker(board, possibleActions, workersMask)));
    }

    @Override
    public void askAction(List<Action> possibleActions, boolean canEndOfTurn)
    {
        int actionSelected = CliUtils.handleAction(possibleActions, canEndOfTurn, board , workersMask);

        if(actionSelected >=0 && actionSelected < possibleActions.size())
            notifyReadAction(possibleActions.get(actionSelected));
        else
            notifyReadVoluntaryEndOfTurn();
    }

    @Override
    public void removeWorkersOfPlayer(int id)
    {

        CliUtils.removeWorkersOfPlayer(workersMask, id);
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
    public synchronized void executeAction(Action action)
    {
        if(action instanceof SetupAction)
            this.executeAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.executeAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.executeAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.executeAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't execute a normal action!");
    }

    public void executeAction(SetupAction action)
    {
        CliUtils.updateMaskOnMove(workersMask, action.getTargetX(), action.getTargetY(), action.getWorkerID());
        CliUtils.showUpdatedBoard(board, workersMask);
    }

    public void executeAction(MoveAction action)
    {
        CliUtils.updateMaskOnMove(workersMask, action.getTargetX(), action.getTargetY(), action.getWorkerID());
        CliUtils.showUpdatedBoard(board, workersMask);
    }

    public void executeAction(MoveAndForceAction action)
    {
        CliUtils.updateMaskOnMove(workersMask, action.getTargetX(), action.getTargetY(), action.getWorkerID());
        CliUtils.updateMaskOnMove(workersMask, action.getForcedTargetX(), action.getForcedTargetY(), action.getForcedWorkerId());
        CliUtils.showUpdatedBoard(board, workersMask);
    }

    public void executeAction(BuildAction action)
    {
        CliUtils.updateBoardOnBuild(board, action);
        CliUtils.showUpdatedBoard(board, workersMask);
    }

    @Override
    public int getNumPlayersRegister()
    {

        return players.size();
    }
}
