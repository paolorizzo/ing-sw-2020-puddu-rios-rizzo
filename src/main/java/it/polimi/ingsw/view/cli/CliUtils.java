package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A set of utils available to the CLI user interface to update the internal model representation and visualize the game.
 */
public class CliUtils
{
    /**
     * Handles the user input for the IP address and the port.
     * @return the IP and the port as concatenated strings.
     */
    static String handleIpAndPortSelection()
    {
        String ip = "";
        String port = "";
        String ipv4_regex = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";


        while(!ip.matches(ipv4_regex))
        {
            showIpDialog();
            ip = readString();
        }

        while(port.length() == 0 || !port.chars().allMatch( Character::isDigit ) || Integer.parseInt(port)<1024 || Integer.parseInt(port) > 65535)
        {
            showPortDialog();
            port = readString();
        }

        return ip+" "+port;
    }

    /**
     * Handles errors that must be shown to the player.
     * @param model the model instance.
     * @param error the error message.
     */
    static void handleShowError(ModelCLI model, String error)
    {
        if(error.equals("Length must be between 1 and 8 characters!"))
        {
            CliUtils.handleNameError();
        }

        if(error.equals("You were excluded by the game."))
        {
            CliUtils.handleExcluded(model);
        }

        if(error.contains("Waiting for the server"))
        {
            CliUtils.handleWaitingForServer(model);
        }
        else if(error.contains("Waiting for the other players"))
        {
            CliUtils.handleWaitingPlayers(model);
        }
    }

    /**
     * Handles the waiting for the server connection.
     * @param model the model instance.
     */
    static void handleWaitingForServer(ModelCLI model)
    {
        if(model.isWaiting())
        {
            showWaitingForTheServer();
            model.notWaiting();
        }
    }

    /**
     * Handles the waiting for other players to join the game.
     * @param model the model instance.
     */
    static void handleWaitingPlayers(ModelCLI model)
    {
        if(model.isWaiting())
        {
            showWaitingForOtherPlayers();
            model.notWaiting();
        }
    }

    /**
     * Handles the notification of disconnection of another player.
     * @param model the model instance.
     */
    static void handlePlayerDisconnection(ModelCLI model)
    {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        showPlayerDisconnected();
        model.notWaiting();
    }

    /**
     * Handles the server disconnection notification.
     * @param model the model instance.
     */
    static void handleServerDisconnection(ModelCLI model)
    {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        showServerDisconnected();
        model.notWaiting();
    }

    /**
     * Handles the proposal of restore of the game.
     * @return the boolean flag being true if the player wants to restore.
     */
    static boolean handleRestore()
    {
        showRestore();
        String choice = " ";
        while(!(choice.toLowerCase().equals("y") || choice.toLowerCase().equals("n")))
        {
            choice = readString();
        }
        return choice.toLowerCase().equals("y");
    }

    /**
     * Handles the request of username input to the player.
     * @return the proposed username as String.
     */
    static String handleUsername()
    {
        showUsernameDialog();
        System.out.println();
        return readString();
    }

    /**
     * Handles the notification of incorrect username input.
     */
    static void handleNameError()
    {

        showNameErrorDialog();
    }

    /**
     * Handles the notification of exlcusion from the upcoming game.
     * @param model the model instance.
     */
    static void handleExcluded(ModelCLI model)
    {
        showExcludedDialog();
        model.notWaiting();
    }

    /**
     * Handles the selection of a card from a set.
     * @param cards the list of cards.
     * @return the identification number of the chosen card.
     */
    static int handleCardSelection(List<Card> cards)
    {
        String input;
        SelectionCLI actionSelection = new SelectionCLI(1, 2, true);
        SelectionCLI cardSelection = new SelectionCLI(0, cards.size()-1, true);

        boolean spin = true;
        while(spin)
        {
            showControls();
            System.out.println();
            showCard(cards.get(cardSelection.getX()).getDescription(), cards.get(cardSelection.getX()).getName().toUpperCase(), actionSelection.getX());
            input = readString();
            input = input.toLowerCase();

            if(input.equals(""))
            {
                if(actionSelection.getX() == 1)
                    spin = false;

                if(actionSelection.getX() == 0)
                    cardSelection.subX();

                if(actionSelection.getX() == 2)
                    cardSelection.addX();

                actionSelection.setX(1);
            }
            else
            {
                actionSelection.handleWASD(input);
            }
        }

        return cardSelection.getX();
    }

    /**
     * Handles the setup of a worker on the board.
     * @param model the model instance.
     * @param possibleActions the list of possible setup actions.
     * @return the index of the chosen setup action.
     */
    static int handleSetupWorker(ModelCLI model, List<Action> possibleActions)
    {
        Space chosenSpace;
        int chosenAction = -1;


        while(chosenAction < 0)
        {
            chosenSpace = selectionOnBoard(model, "Now! Choose where to put your worker. Use A and D to switch!", 0,0);
            chosenAction = getActionNumber(chosenSpace.getPosX(),chosenSpace.getPosY(), possibleActions);
        }

        return chosenAction;
    }

    /**
     * Handles the selection of the number of players for the game.
     * @return the number of player (2 or 3 are the only accepted values).
     */
    static int handleNumPlayerSelection()
    {
        int numPlayers = 2;
        String input;
        boolean spin = true;

        while(spin)
        {
            slideDown(36,1);
            System.out.println();
            showControls();
            System.out.println();
            showNumPlayersDialog(numPlayers);

            input = readString();

            switch(input.toLowerCase())
            {
                case("2"):
                    spin = false;
                    numPlayers =  2;
                    break;
                case("3"):
                    spin = false;
                    numPlayers =  3;
                    break;
                case("d"):
                case("a"):
                    numPlayers = numPlayers == 3? 2:3;
                    break;
                case(""):
                    spin = false;
                    break;
                default:
                    break;
            }

        }
        return numPlayers;
    }

    /**
     * Handles a move action.
     * @param model the model instance.
     * @param possibleActions the list of possible move actions.
     * @param workerId the worker to perform the action.
     * @return the index of the chosen action.
     */
    static int handleMoveAction(ModelCLI model, List<Action> possibleActions, String workerId)
    {
        Space chosenSpace = null;
        int chosenAction = -1;
        Space workerInitialPosition = getWorkersPositionFromId(model.getWorkers(), workerId);

        while(chosenAction < 0)
        {
            chosenSpace = selectionOnBoard(model, "Now! Make your move:", workerInitialPosition.getPosX(), workerInitialPosition.getPosY());
            chosenAction = getActionNumber(chosenSpace.getPosX(),chosenSpace.getPosY(), possibleActions, workerId);
        }

        return chosenAction;
    }

    /**
     * Handles a build action.
     * @param model the model instance.
     * @param possibleActions the list of possible build actions.
     * @param workerId the worker to perform the action.
     * @return the index of the chosen action.
     */
    static int handleBuildAction(ModelCLI model, List<Action> possibleActions, String workerId)
    {
        SpaceCLI chosenSpace;
        int chosenAction = -1;
        Space workerInitialPosition = getWorkersPositionFromId(model.getWorkers(), workerId);

        while(chosenAction < 0)
        {
            chosenSpace = selectAndBuildOnBoard(model, workerInitialPosition.getPosX(), workerInitialPosition.getPosY());
            chosenAction = getActionNumber(chosenSpace.getX(),chosenSpace.getY(), possibleActions, workerId, chosenSpace.getLevel());
        }

        return chosenAction;
    }

    /**
     * Handles a generic action, delegating the actual operation to the specific method.
     * @param model the model instance.
     * @param possibleActions the index of the chosen action.
     * @param canEndOfTurn the flag indicating the possibility to end the turn within this action.
     * @return the index of the chosen action.
     */
    static int handleAction(ModelCLI model, List<Action> possibleActions, boolean canEndOfTurn)
    {
        if(canEndOfTurn && handleWouldYouLikeToEndYourTurn())
            return possibleActions.size();

        String selectedWorker;
        int selectedAction = -1;
        if(isWorkerSelected(possibleActions))
            selectedWorker = possibleActions.get(0).getWorkerID();
        else
            selectedWorker = handleWorkerSelection(model,possibleActions.get(0).getWorkerID().charAt(1)-'0');

        while(selectedAction < 0)
        {
            if(isMoveAction(possibleActions))
            {
                //System.err.println("This is a move action");
                selectedAction = handleMoveAction(model, possibleActions, selectedWorker);
            }
            else if(isBuildAction(possibleActions))
            {
                //System.err.println("This is a build action");
                selectedAction = handleBuildAction(model, possibleActions, selectedWorker);
            }
            else
            {
                BidimensionalSelectionCLI selection = new BidimensionalSelectionCLI(getWorkersPositionFromId(model.getWorkers(), selectedWorker).getPosX(),4,true,getWorkersPositionFromId(model.getWorkers(), selectedWorker).getPosY(), 4, true);
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
                showBoard(model, selection);
                if(handleChooseBetweenMoveAndBuild()==0)
                    selectedAction = handleMoveAction(model, possibleActions, selectedWorker);
                else
                    selectedAction = handleBuildAction(model, possibleActions, selectedWorker);
            }
        }
        return selectedAction;
    }

    /**
     * Handles the selection of the active worker for the turn.
     * @param model the model instance.
     * @param idPlayer the player's id.
     * @return the id of the selected worker.
     */
    static String handleWorkerSelection(ModelCLI model, int idPlayer)
    {
        String input;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BLACK);
        List<Space> workersPositions = getWorkersPositions(model.getWorkers(), idPlayer);
        SelectionCLI workerSelection = new SelectionCLI(0,workersPositions.size()-1,true);
        BidimensionalSelectionCLI boardSelection = new BidimensionalSelectionCLI(0,4,true,0,4,true);

        boolean spin = true;
        while(spin)
        {
            printer.lineBreak();
            showControls();
            printer.print("Now! Select your worker for this turn. Press A/D to switch between them.");
            printer.resetAndBreak();
            printer.lineBreak();

            boardSelection.setX(workersPositions.get(workerSelection.getX()).getPosX());
            boardSelection.setY(workersPositions.get(workerSelection.getX()).getPosY());
            showBoard(model, boardSelection);

            input = readString();
            input = input.toLowerCase();

            if(input.equals(""))
            {
                spin = false;
            }
            else
            {
                workerSelection.handleWASD(input);
            }
        }

        return model.getWorkers()[boardSelection.getX()][boardSelection.getY()];
    }

    /**
     * Handles the choice between moving and building, that can happen with specific powers.
     * @return 1 if the input chooses to build, 0 if he chooses to move.
     */
    static int handleChooseBetweenMoveAndBuild()
    {
        String input;
        int choice = 0;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BG_WHITE, AnsiColors.ANSI_CYAN);


        boolean spin = true;
        while(spin)
        {
            printer.print("INSERT M TO MOVE OR B TO BUILD: ");
            System.out.print(AnsiColors.ANSI_RESET);
            input = readString();
            input = input.toLowerCase();

            switch(input)
            {
                case "m":
                    spin = false;
                    choice = 0;
                    break;
                case "b":
                    spin = false;
                    choice = 1;
                    break;
                default:
                    break;
            }
        }
        return choice;
    }

    /**
     * Handles the proposal to end the turn with this action.
     * @return the boolean flag being true if the player wants to end the turn.
     */
    static boolean handleWouldYouLikeToEndYourTurn()
    {
        String input;
        boolean choice = false;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BG_WHITE, AnsiColors.ANSI_CYAN);


        boolean spin = true;
        while(spin)
        {
            printer.print("WOULD YOU LIKE TO END YOUR TURN HERE? Y/N ");
            System.out.print(AnsiColors.ANSI_RESET);
            input = readString();
            input = input.toLowerCase();

            switch(input)
            {
                case "y":
                    spin = false;
                    choice = true;
                    break;
                case "n":
                    spin = false;
                    break;
                default:
                    break;
            }
        }
        return choice;
    }

    /**
     * Handles the update about the currently active player.
     * @param id the active player's id.
     * @param model the model instance.
     */
    static void handleCurrentPlayer(int id, ModelCLI model)
    {
        model.setCurrentPlayerId(id);

        if(model.isGameOn())
            showBoard(model);
    }

    /**
     * Handles the update informing the player that he just lost the game.
     * @param model the model instance.
     */
    static void handleLose(ModelCLI model)
    {
        CanvasCLI canvas = new CanvasCLI(0,0,53,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        RectangleCLI loseAnnounce = new RectangleCLI(20,3,11,1);
        canvas.setTextColor(AnsiColors.ANSI_BLACK);
        loseAnnounce.setPalette(AnsiColors.ANSI_BG_RED);
        loseAnnounce.addText("       YOU LOST! I'M SORRY.");
        canvas.addOverlappingFigure(loseAnnounce);
        canvas.printFigure();
        showBoard(model, null);
        model.setAsSpectator();
    }

    /**
     * Handles the update informing the player that he just won the game.
     * @param model the model instance.
     */
    static void handleWin(ModelCLI model)
    {
        CanvasCLI canvas = new CanvasCLI(0,0,53,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        RectangleCLI loseAnnounce = new RectangleCLI(20,3,11,1);
        canvas.setTextColor(AnsiColors.ANSI_BLACK);
        loseAnnounce.setPalette(AnsiColors.ANSI_BG_GREEN);
        loseAnnounce.addText("        YOU WON! CONGRATS");
        canvas.addOverlappingFigure(loseAnnounce);
        canvas.printFigure();
        showBoard(model, null);
    }

    /**
     * Checks if the upcoming action is a move action.
     * @param possibleActions the list of possible actions.
     * @return the boolean flag being true if the upcoming action is a move action.
     */
    static private boolean isMoveAction(List<Action> possibleActions)
    {
        for(Action a:possibleActions)
        {
            if(!(a instanceof MoveAction))
                return false;
        }
        return true;
    }

    /**
     * Checks if the upcoming action is a build action.
     * @param possibleActions the list of possible actions.
     * @return the boolean flag being true if the upcoming action is a build action.
     */
    static private boolean isBuildAction(List<Action> possibleActions)
    {
        for(Action a:possibleActions)
        {
            if(!(a instanceof BuildAction))
                return false;
        }
        return true;
    }

    /**
     * Checks if the worker has been already selected for the turn.
     * @param possibleActions the list of possible actions.
     * @return the boolean flag being true if the worker has been selected.
     */
    static private boolean isWorkerSelected(List<Action> possibleActions)
    {
        String firstId = possibleActions.get(0).getWorkerID();
        for(Action a: possibleActions)
        {
            if(!a.getWorkerID().equals(firstId))
                return false;
        }
        return true;
    }

    /**
     * Gets the workers' positions for a specific player.
     * @param workersMask the representation of the workers' positions as a matrix storing the ids.
     * @param idPlayer the player's id.
     * @return the list of spaces containing the workers of the player.
     */
    static private List<Space> getWorkersPositions(String[][] workersMask, int idPlayer)
    {
        List<Space> spaces = new ArrayList<Space>();

        for(int row = 0; row<5; row++)
        {
            for(int col = 0; col<5; col++)
            {
                if(workersMask[row][col] != "" && workersMask[row][col].charAt(1)-'0' == idPlayer)
                    spaces.add(new Space(row, col));
            }
        }

        return spaces;
    }

    /**
     * Gets a specific worker's position based on its id.
     * @param workersMask the representation of the workers' positions as a matrix storing the ids.
     * @param id the worker's id.
     * @return the space containing the worker.
     */
    static private Space getWorkersPositionFromId(String[][] workersMask, String id)
    {
        Space position = new Space(0,0);
        for(int row = 0; row<5; row++)
        {
            for(int col = 0; col<5; col++)
            {
                if(workersMask[row][col].equals(id))
                    position = new Space(row,col);
            }
        }
        return position;
    }

    /**
     * Finds a specific action in the list of possible ones, based only on the coordinates.
     * @param x the x coordinate of the action.
     * @param y the y coordinate of the action.
     * @param actions the list of possible actions.
     * @return the index of the requested action in the list.
     */
    static int getActionNumber(int x, int y, List<Action> actions)
    {
        for(int i = 0; i<actions.size(); i++)
        {
            if(     actions.get(i).getTargetX() == x &&
                    actions.get(i).getTargetY() == y)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Finds a specific action in the list of possible ones, based only on the coordinates and the worker's id.
     * @param x the x coordinate of the action.
     * @param y the y coordinate of the action.
     * @param possibleActions the list of possible actions.
     * @param workerId the worker's id.
     * @return the index of the requested action in the list.
     */
    static int getActionNumber(int x, int y, List<Action> possibleActions, String workerId)
    {
        for(int i = 0; i<possibleActions.size(); i++)
        {
            if(     possibleActions.get(i) instanceof MoveAction &&
                    possibleActions.get(i).getTargetX() == x &&
                    possibleActions.get(i).getTargetY() == y &&
                    possibleActions.get(i).getWorkerID().equals(workerId))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds a specific action in the list of possible ones, based only on the coordinates, the worker's id and the building level.
     * @param x the x coordinate of the action.
     * @param y the y coordinate of the action.
     * @param possibleActions the list of possible actions.
     * @param workerId the worker's id.
     * @param level the building level.
     * @return the index of the requested action in the list.
     */
    static int getActionNumber(int x, int y, List<Action> possibleActions, String workerId, int level)
    {
        for(int i = 0; i<possibleActions.size(); i++)
        {
            if(     possibleActions.get(i) instanceof BuildAction &&
                    possibleActions.get(i).getTargetX() == x &&
                    possibleActions.get(i).getTargetY() == y &&
                    possibleActions.get(i).getWorkerID().equals(workerId) &&
                    ((BuildAction) possibleActions.get(i)).getPiece().getLevel() == level)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles a WASD selection on the game board, with an header message.
     * @param model the model instance.
     * @param message the header message to display above the board.
     * @param startX the starting horizontal index.
     * @param startY the starting vertical index.
     * @return the selected space.
     */
    static private Space selectionOnBoard(ModelCLI model, String message, int startX, int startY)
    {
        String input;
        Space chosenSpace = null;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BLACK);
        BidimensionalSelectionCLI selection = new BidimensionalSelectionCLI(startX,4,true,startY,4,true);

        boolean spin = true;
        while(spin)
        {
            printer.lineBreak();
            showControls();
            printer.print(message);
            printer.resetAndBreak();
            printer.lineBreak();

            showBoard(model, selection);
            input = readString();
            input = input.toLowerCase();

            if(input.equals(""))
            {
                spin = false;
                chosenSpace = new Space(selection.getX(), selection.getY());
            }
            else
            {
                selection.handleWASD(input);
            }
        }
        return chosenSpace;
    }

    /**
     * Handles a WASD selection of a space to build on.
     * @param model the model instance.
     * @param startX the starting horizontal index.
     * @param startY the starting vertical index.
     * @return the selected space.
     */
    static private SpaceCLI selectAndBuildOnBoard(ModelCLI model, int startX, int startY)
    {
        String input;
        SpaceCLI chosenSpace = null;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BLACK);
        BidimensionalSelectionCLI selection = new BidimensionalSelectionCLI(startX,4,true,startY,4,true);

        boolean spin = true;
        while(spin)
        {
            printer.lineBreak();
            showControls();
            printer.print("Now! Make your build. Press the number of the level requested and then Enter!");
            printer.resetAndBreak();
            printer.lineBreak();

            showBoard(model, selection);
            input = readString();
            input = input.toLowerCase();

            switch(input)
            {
                case "1":
                case "2":
                case "3":
                case "4":
                    spin = false;
                    chosenSpace = new SpaceCLI(selection.getX(), selection.getY(), Integer.parseInt(input));
                    break;
                case "w":
                case "a":
                case "s":
                case "d":
                    selection.handleWASD(input);
                    break;
                default:
                    break;
            }
        }
        return chosenSpace;
    }

    /**
     * Reads a String from the user input.
     * @return the input String.
     */
    public static String readString()
    {
        Scanner stdin = new Scanner(System.in);
        String word = stdin.nextLine();
        System.out.println();
        return word;

    }

    /**
     * Shows the game board based on the data stored in the model.
     * @param model the model instance.
     */
    static void showBoard(ModelCLI model)
    {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        showBoard(model, null);
    }

    /**
     * Shows the game board with a selected space.
     * @param model the model instance.
     * @param selection the SelectionCLI object handling the selection.
     */
    static private void showBoard(ModelCLI model, BidimensionalSelectionCLI selection)
    {
        String[][] workersMask = model.getWorkers();

        //create Canvas
        CanvasCLI back_canvas = new CanvasCLI(0,0,65,36);
        back_canvas.setPalette(AnsiColors.ANSI_RESET);
        back_canvas.setTextColor(AnsiColors.ANSI_BLACK);

        //create the (empty) players label
        RectangleCLI playerLable  = new RectangleCLI(37,0,16,24);
        playerLable.setPalette(AnsiColors.ANSI_BG_CYAN);
        back_canvas.addOverlappingFigure(playerLable);
        RectangleCLI innerLable  = playerLable.createInRelativeFrame(1,1,14,22);
        innerLable.setPalette(AnsiColors.ANSI_BG_WHITE);
        back_canvas.addOverlappingFigure(innerLable);

        //create the (empty) piecebag label
        RectangleCLI buildLabel  = new RectangleCLI(37,25,16,11);
        buildLabel.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);
        back_canvas.addOverlappingFigure(buildLabel);
        RectangleCLI innerBuildLabel  = buildLabel.createInRelativeFrame(1,1,14,9);
        innerBuildLabel.setPalette(AnsiColors.ANSI_BG_WHITE);
        back_canvas.addOverlappingFigure(innerBuildLabel);

        //populate the players label
        Iterator<Player> playersIterator = model.getPlayersIterator();
        for(int i=0; playersIterator.hasNext(); i++)
        {
            Player currentPlayer = playersIterator.next();
            RectangleCLI label = innerLable.createInRelativeFrame(3, (1+7*i), 5, 1);
            label.addText(" "+currentPlayer.getNickname().toUpperCase()+" ");
            RectangleCLI god = innerLable.createInRelativeFrame(9,(1+7*i), 4,1);
            god.addText(" "+currentPlayer.getCard().getName().toUpperCase());

            switch(currentPlayer.getId())
            {
                case 0:
                    god.setPalette(AnsiColors.ANSI_BG_CYAN);
                    break;
                case 1:
                    god.setPalette(AnsiColors.ANSI_BRIGHT_BG_PURPLE);
                    break;
                case 2:
                    god.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);
                    break;
                default:
                    break;
            }


            RectangleCLI activePlayer = innerLable.createInRelativeFrame(1,(1+7*i), 1,1);

            activePlayer.setPalette( model.getCurrentPlayerId() == currentPlayer.getId()? AnsiColors.ANSI_BRIGHT_BG_GREEN : AnsiColors.ANSI_BG_RED);

            RectangleCLI godDescription = activePlayer.createInRelativeFrame(0,1,12, 5);
            godDescription.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
            godDescription.addText(currentPlayer.getCard().getDescription());

            label.setPalette(AnsiColors.ANSI_BRIGHT_BG_CYAN);
            back_canvas.addOverlappingFigure(label);
            back_canvas.addOverlappingFigure(activePlayer);
            back_canvas.addOverlappingFigure(god);
            back_canvas.addOverlappingFigure(godDescription);
        }

        //populate the piecebag label
        for(int i=0; i<4; i++)
        {
            RectangleCLI label = innerBuildLabel.createInRelativeFrame(1, (1+2*i), 3, 1);
            label.setPalette(AnsiColors.ANSI_BG_RED);
            label.addText(" LEVEL "+ Integer.toString(i + 1) +" ");

            RectangleCLI num = label.createInRelativeFrame(4, 0, 8, 1);
            num.setPalette(AnsiColors.ANSI_BG_RED);
            GraphicsElementsCLI.drawBuildCounter(num, model.getPiecesLeft(i+1));
            back_canvas.addOverlappingFigure(num);
            back_canvas.addOverlappingFigure(label);

        }

        //create the board
        RectangleCLI canvas = new RectangleCLI(0,0,36,36);
        back_canvas.addOverlappingFigure(canvas);
        canvas.setPalette(AnsiColors.ANSI_BG_YELLOW);

        //create selection
        if(selection != null)
        {
            //create selection
            RectangleCLI selectionFigure = new RectangleCLI(selection.getX()*7, selection.getY()*7, 8,8);
            selectionFigure.setPalette(AnsiColors.ANSI_BG_RED);

            //add the selection to the figure
            canvas.addOverlappingFigure(selectionFigure);
        }

        //create spaces
        List<RectangleCLI> spaces = new ArrayList<>();
        for(int row = 1; row<36; row+=7)
        {
            for(int col = 1; col<36; col+=7)
            {
                spaces.add(new RectangleCLI(row, col, 6,6 ));
            }
        }

        //decorate spaces and add them to the figure
        Iterator<Integer> boardIterator = model.getBoardIterator();
        for(RectangleCLI s : spaces)
        {
            SpaceCLI.buildLevel(s, boardIterator.next());
            canvas.addOverlappingFigure(s);
        }

        //create workers
        for(int row = 0; row<5; row++)
        {
            for(int col = 0; col<5; col++)
            {
                if(workersMask[row][col] != "")
                {
                    RectangleCLI worker = new RectangleCLI(row*7+3, col*7+3, 2,2);
                    GraphicsElementsCLI.drawWorker(worker, workersMask[row][col].charAt(1));
                    canvas.addOverlappingFigure(worker);
                }
            }
        }

        //print the figure
        back_canvas.printFigure();

        if(model.getSpectator())
        {
            System.out.println();
            CanvasCLI spectatorMode = new CanvasCLI(0,0,53,1);
            spectatorMode.setTextColor(AnsiColors.ANSI_BLACK);
            spectatorMode.setPalette(AnsiColors.ANSI_BG_RED);
            spectatorMode.addText("Since you've lost, you're now in spectator mode. Just relax and see how your ex opponents are doing!");
            spectatorMode.printFigure();
        }
    }

    /**
     * Shows the asking for username dialog.
     */
    static void showUsernameDialog()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);

        //create text box
        RectangleCLI textBox = new RectangleCLI(13,2,9,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("      INSERT USERNAME ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 11,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLUE);

        //overlap figures in the correct order
        //canvas.addOverlappingFigure(lowArrow);
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the asking for IP address dialog.
     */
    static void showIpDialog()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);

        //create text box
        RectangleCLI textBox = new RectangleCLI(13,2,9,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("    INSERT AN IP ADDRESS  ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 11,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLUE);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the asking for port number dialog.
     */
    static void showPortDialog()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);

        //create text box
        RectangleCLI textBox = new RectangleCLI(13,2,9,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("      INSERT THE PORT  ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 11,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLUE);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the waiting for the server dialog.
     */
    static void showWaitingForTheServer()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_RED);

        //create text box
        RectangleCLI textBox = new RectangleCLI(13,2,9,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("   WAITING FOR THE SERVER  ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 11,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Show the waiting for other players dialog.
     */
    static void showWaitingForOtherPlayers()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_RED);

        //create text box
        RectangleCLI textBox = new RectangleCLI(12,2,11,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("  WAITING OTHER PLAYERS TO JOIN  ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 13,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the incorrect username error dialog.
     */
    static void showNameErrorDialog()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_RED);

        //create text box
        RectangleCLI textBox = new RectangleCLI(10,2,15,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("  LENGHT MUST BE BETWEEN 1 AND 8 CHARACTERS  ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 17,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the dialog informing the player that he has been excluded from the upcoming game.
     */
    static void showExcludedDialog()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_RED);

        //create text box
        RectangleCLI textBox = new RectangleCLI(11,2,13,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("  SORRY! YOU WERE EXCLUDED BY THE GAME  ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 15,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the dialog informing the player that the server is no more reachable.
     */
    static void showServerDisconnected()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,36);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_RED);

        //create text box
        RectangleCLI textBox = new RectangleCLI(12,15,11,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("  THE SERVER IS NOT REACHABLE  ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 13,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the dialog informing the player that another player has disconnected.
     */
    static void showPlayerDisconnected()
    {
        System.out.println("\n\n\n\n\n");
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,36);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_RED);

        //create text box
        RectangleCLI textBox = new RectangleCLI(12,15,13,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("  SORRY! ANOTHER PLAYER QUIT THE GAME  ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 15,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the dialog asking the player if he wants tor restore the game.
     */
    static void showRestore()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,10);
        canvas.setPalette(AnsiColors.ANSI_RESET);

        //create text box
        RectangleCLI textBox = new RectangleCLI(13,2,16,2);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText("   THERE IS A SAVED GAME WITH THESE USERNAMES         DO YOU WANT TO CONTINUE THAT? Y / N ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 18,4);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLUE);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows a card in the context of the card selection phase.
     * @param desc the description of the card.
     * @param title the name of the god represented on the card.
     * @param userSelection the Selection object handling the WASD selection.
     */
    private static void showCard(String desc, String title, int userSelection)
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI();
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_WHITE);

        //create card
        RectangleCLI card = new RectangleCLI(10,4,16,27);
        GraphicsElementsCLI.drawCard(card);

        //create text box
        RectangleCLI textBox = card.createInRelativeFrame(2,18,12,7);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText(desc);

        //create picture box
        RectangleCLI pic = card.createInRelativeFrame(2,2,12,13);
        GraphicsElementsCLI.drawGod(pic, title);

        //create name box
        RectangleCLI name = textBox.createInRelativeFrame(0,-2,12,1);
        name.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        name.addText(title);

        //create selection box
        RectangleCLI selection = card.createInRelativeFrame(-1,-1,18,29);
        selection.setPalette(userSelection == 1? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_RESET);

        //create arrows
        RectangleCLI rightArrow = new RectangleCLI(27,14,8,9);
        GraphicsElementsCLI.drawArrow(rightArrow, userSelection, true);

        RectangleCLI leftArrow = new RectangleCLI(1,14,8,9);
        GraphicsElementsCLI.drawArrow(leftArrow, userSelection, false);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(selection);
        canvas.addOverlappingFigure(card);
        canvas.addOverlappingFigure(textBox);
        canvas.addOverlappingFigure(rightArrow);
        canvas.addOverlappingFigure(leftArrow);
        canvas.addOverlappingFigure(pic);
        canvas.addOverlappingFigure(name);

        //print the image
        canvas.printFigure();
    }

    /**
     * Shows the information header about WASD controls.
     */
    private static void showControls()
    {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_WHITE+ "Use WASD to control the selection: W to go UP, S to go DOWN, A to go RIGHT, D to go LEFT");
        System.out.println(AnsiColors.ANSI_RESET);
    }

    /**
     * Shows the menu to select the number of players for the game.
     * @param num the currently selected number of players in the menu.
     */
    public static void showNumPlayersDialog(int num)
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,36);
        canvas.setPalette(AnsiColors.ANSI_RESET);

        //create numbers
        RectangleCLI number2 = new RectangleCLI(5,7,12,12);
        GraphicsElementsCLI.drawNumber(number2, 2, num);

        RectangleCLI number3 = new RectangleCLI(17,7,12,12);
        GraphicsElementsCLI.drawNumber(number3, 3, num);

        //create text box
        RectangleCLI textBox = new RectangleCLI(12,2,10,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText(" SELECT THE NUMBER OF PLAYERS");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 12,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLUE);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(number2);
        canvas.addOverlappingFigure(number3);
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the figure
        canvas.printFigure();
    }

    /**
     * Slides down of the specified number of rows at the specified rate.
     * @param rows the number of rows.
     * @param rateInMilliseconds the rate expressed in milliseconds.
     */
    static void slideDown(int rows, int rateInMilliseconds)
    {
        for(int i = 0; i<rows; i++)
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(rateInMilliseconds);
                System.out.println();
            }
            catch(InterruptedException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }
}
