package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observation.UserInterfaceObservable;
import it.polimi.ingsw.view.UserInterface;

import java.util.HashMap;
import java.util.List;

public class Cli extends UserInterfaceObservable implements UserInterface
{
    private HashMap<Integer, Player> players;
    private int numPlayers;
    private final ModelCLI model;

    public Cli()
    {
        players = new HashMap<>();
        model = new ModelCLI();
    }

    //UI methods
    @Override
    public void askIpAndPort()
    {
        String result = CliUtils.handleIpAndPortSelection();
        notifyIp(result.split(" ")[0]);
        notifyPort(result.split(" ")[1]);
    }

    @Override
    public void showLogo()
    {


    }

    @Override
    public void setCurrentPlayer(int id)
    {

        CliUtils.handleCurrentPlayer(id, model);

    }

    @Override
    public void showDisconnection(String message)
    {
        if(message.contains("impossible"))
        {
            CliUtils.handleServerDisconnection(model);
        }
        else if(message.contains("player"))
        {
            CliUtils.handlePlayerDisconnection(model);
        }
    }

    @Override
    public void showError(String e)
    {
        if(e.equals("Length must be between 1 and 8 characters!"))
        {
            CliUtils.handleNameError();
        }

        if(e.equals("You were excluded by the game."))
        {
            CliUtils.handleExcluded(model);
        }

        if(e.contains("Waiting for the server"))
        {
            CliUtils.handleWaitingForServer(model);
        }
        else if(e.contains("Waiting for the other players"))
        {
            CliUtils.handleWaitingPlayers(model);
        }
    }

    @Override
    public void askNumPlayers()
    {

        notifyReadNumPlayers(CliUtils.handleNumPlayerSelection());
    }

    @Override
    public void askUsername()
    {

        notifyReadName(CliUtils.handleUsername());
    }

    @Override
    public void askRestore()
    {

        notifyReadRestore(CliUtils.handleRestore());
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
        model.setGameMode();
    }

    @Override
    public void askSetupWorker(List<Action> possibleActions)
    {

        notifyReadAction(possibleActions.get(CliUtils.handleSetupWorker(model, possibleActions)));
    }

    @Override
    public void askAction(List<Action> possibleActions, boolean canEndOfTurn)
    {
        int actionSelected = CliUtils.handleAction(model, possibleActions, canEndOfTurn);

        if(actionSelected >=0 && actionSelected < possibleActions.size())
            notifyReadAction(possibleActions.get(actionSelected));
        else
            notifyReadVoluntaryEndOfTurn();
    }

    @Override
    public void winAnnounce(int id)
    {

        CliUtils.handleWin(model);
    }

    @Override
    public void loseAnnounce(int id)
    {

        CliUtils.handleLose(model);
    }

    @Override
    public void removeWorkersOfPlayer(int id)
    {

        model.removeWorkersOfPlayer(id);
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
        model.addPlayer(id, players.get(id));
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
        model.updateMaskOnMove(action);
        CliUtils.showBoard(model);
    }

    public void executeAction(MoveAction action)
    {
        model.updateMaskOnMove(action);
        CliUtils.showBoard(model);
    }

    public void executeAction(MoveAndForceAction action)
    {
        MoveAction forcedAction = new MoveAction(action.getForcedWorkerId(), action.getForcedTargetX(), action.getForcedTargetY(), action.getDirection(), action.getForcedStartX(), action.getForcedStartY());
        model.updateMaskOnMove(action);
        model.updateMaskOnMove(forcedAction);
        CliUtils.showBoard(model);
    }

    public void executeAction(BuildAction action)
    {
        model.updateBoardOnBuild(action);
        CliUtils.showBoard(model);
    }

    @Override
    public int getNumPlayersRegister()
    {

        return players.size();

    }
}
