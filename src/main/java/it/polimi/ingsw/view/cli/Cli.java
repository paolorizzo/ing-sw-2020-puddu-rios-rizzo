package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observation.UserInterfaceObservable;
import it.polimi.ingsw.view.UserInterface;

import java.util.HashMap;
import java.util.List;

/**
 * The user interface class for command line.
 */
public class Cli extends UserInterfaceObservable implements UserInterface
{
    private HashMap<Integer, Player> players;
    private int numPlayers;
    private final ModelCLI model;

    /**
     * Creates an empty CLI object.
     */
    public Cli()
    {
        players = new HashMap<>();
        model = new ModelCLI();
    }

    /**
     * Asks for the IP address and port number of the desired game server.
     */
    @Override
    public void askIpAndPort()
    {
        String result = CliUtils.handleIpAndPortSelection();
        notifyIp(result.split(" ")[0]);
        notifyPort(result.split(" ")[1]);
    }

    /**
     * Legacy method supposed to trigger the display of the game's logo.
     */
    @Override
    public void showLogo()
    {


    }

    /**
     * Informs the user interface about the currently active player.
     * @param id the player's id.
     */
    @Override
    public void setCurrentPlayer(int id)
    {

        CliUtils.handleCurrentPlayer(id, model);

    }

    /**
     * Informs the user about an occurred disconnection.
     * @param message a String containing details about the cause of the disconnection.
     */
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

    /**
     * Displays an error message.
     * @param e the error text.
     */
    @Override
    public void showError(String e)
    {

        CliUtils.handleShowError(model,e);
    }

    /**
     * Asks for the desired number of players.
     */
    @Override
    public void askNumPlayers()
    {

        notifyReadNumPlayers(CliUtils.handleNumPlayerSelection());
    }

    /**
     * Asks for the desired username.
     */
    @Override
    public void askUsername()
    {

        notifyReadName(CliUtils.handleUsername());
    }

    /**
     * Asks if the user wants to restore the stored game.
     */
    @Override
    public void askRestore()
    {

        notifyReadRestore(CliUtils.handleRestore());
    }

    /**
     * Asks the user to select a card from the deck.
     * @param deck the collection of cards.
     */
    @Override
    public void askCard(Deck deck)
    {

        notifyReadNumCard(deck.getCards().get(CliUtils.handleCardSelection(deck.getCards())).getNum());
    }

    /**
     * Asks the user to select a god for the game.
     * @param cards the set of available cards.
     */
    @Override
    public void askGod(List<Card> cards)
    {
        notifyReadGod(cards.get(CliUtils.handleCardSelection(cards)).getNum());
        model.setGameMode();
    }

    /**
     * Asks the user to set up a worker on the board.
     * @param possibleActions the possible setup choices list.
     */
    @Override
    public void askSetupWorker(List<Action> possibleActions)
    {

        notifyReadAction(possibleActions.get(CliUtils.handleSetupWorker(model, possibleActions)));
    }

    /**
     * Asks the user to perform an action.
     * @param possibleActions the possible actions list.
     * @param canEndOfTurn a boolean flag indicating the possibility to end the current turn within the current action.
     */
    @Override
    public void askAction(List<Action> possibleActions, boolean canEndOfTurn)
    {
        int actionSelected = CliUtils.handleAction(model, possibleActions, canEndOfTurn);

        if(actionSelected >=0 && actionSelected < possibleActions.size())
            notifyReadAction(possibleActions.get(actionSelected));
        else
            notifyReadVoluntaryEndOfTurn();
    }

    /**
     * Displays the win dialog.
     * @param id the winner's id.
     */
    @Override
    public void winAnnounce(int id)
    {

        CliUtils.handleWin(model);
    }

    /**
     * Displays the lose dialog.
     * @param id the loser's id.
     */
    @Override
    public void loseAnnounce(int id)
    {

        CliUtils.handleLose(model);
    }

    /**
     * Removes all the active workers of a player from the board.
     * @param id the player's id.
     */
    @Override
    public void removeWorkersOfPlayer(int id)
    {

        model.removeWorkersOfPlayer(id);
    }

    /**
     * Sets the number of players in the user interface model representation.
     * @param numPlayers the number of players.
     */
    @Override
    public void setNumPlayers(int numPlayers)
    {

        this.numPlayers = numPlayers;
    }

    /**
     * Retrieves the current number of active players in the game.
     * @return the number of active players.
     */
    @Override
    public int getNumPlayers()
    {

        return numPlayers;
    }

    /**
     * Adds a new player to the game.
     * @param id the players's id.
     * @param name the player's username.
     */
    @Override
    public void registerPlayer(int id, String name)
    {

        players.put(id, new Player(id, name));
    }

    /**
     * Registers the card chosen by another player.
     * @param id the player's id.
     * @param card the chosen power card.
     */
    @Override
    public void registerGod(int id, Card card)
    {

        players.get(id).setCard(card);
        model.addPlayer(players.get(id));
    }

    /**
     * Updates the user interface following an action.
     * @param action the action object.
     */
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

    /**
     * Specific handler for instances of SetupAction.
     * @param action the action object.
     */
    public void executeAction(SetupAction action)
    {
        model.updateMaskOnMove(action);
        CliUtils.showBoard(model);
    }

    /**
     * Specific handler for instances of MoveAction.
     * @param action the action object.
     */
    public void executeAction(MoveAction action)
    {
        model.updateMaskOnMove(action);
        CliUtils.showBoard(model);
    }

    /**
     * Specific handler for instances of MoveAndForceAction.
     * @param action the action object.
     */
    public void executeAction(MoveAndForceAction action)
    {
        MoveAction forcedAction = new MoveAction(action.getForcedWorkerId(), action.getForcedTargetX(), action.getForcedTargetY(), action.getDirection(), action.getForcedStartX(), action.getForcedStartY());
        model.updateMaskOnMove(action);
        model.updateMaskOnMove(forcedAction);
        CliUtils.showBoard(model);
    }

    /**
     * Specific handler for instances of BuildAction.
     * @param action the action object.
     */
    public void executeAction(BuildAction action)
    {
        model.updateBoardOnBuild(action);
        CliUtils.showBoard(model);
    }

    /**
     * Retrieves the current number of active players in the game.
     * @return the number of active players.
     */
    @Override
    public int getNumPlayersRegister()
    {

        return players.size();

    }
}
