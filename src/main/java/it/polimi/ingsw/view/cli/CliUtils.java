package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CliUtils
{
    //handler methods
    //CONNECTION

    static String handleIpAndPortSelection()
    {
        showIpDialog();
        String ip = readString();
        showPortDialog();
        String port = readString();
        return ip+" "+port;
    }

    static void handleWaitingForServer(ModelCLI model)
    {
        if(!model.getAlreadyShownWaitingServer())
        {
            showWaitingForTheServer();
            model.setAlreadyShownWaitingServer();
        }
    }

    static void handleWaitingPlayers(ModelCLI model)
    {
        if(!model.getAlreadyShownWaitingPlayers())
        {
            showWaitingForOtherPlayers();
            model.setAlreadyShownWaitingPlayers();
        }
    }

    //SETUP
    static String handleUsername()
    {
        showUsernameDialog();
        System.out.println();
        return readString();
    }

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

    static int handleSetupWorker(ModelCLI model, List<Action> possibleActions)
    {
        Space chosenSpace;
        int chosenAction = -1;


        while(chosenAction < 0)
        {
            chosenSpace = selectionOnBoard(model, "Now! Choose where to put your worker", 0,0);
            chosenAction = getActionNumber(chosenSpace.getPosX(),chosenSpace.getPosY(), possibleActions);
        }

        return chosenAction;
    }

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

            switch(input)
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

    //GAME
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

        //System.err.println("Sending move" + chosenAction+": X:"+ chosenSpace.getPosX() + " Y:"+chosenSpace.getPosY()+"  "+workerId);

        return chosenAction;
    }

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
                System.out.println("\n\n\n");
                showBoard(model, selection);
                if(handleChooseBetweenMoveAndBuild()==0)
                    selectedAction = handleMoveAction(model, possibleActions, selectedWorker);
                else
                    selectedAction = handleBuildAction(model, possibleActions, selectedWorker);
            }
        }
        return selectedAction;
    }

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
            printer.print("Now! Select your worker for this turn:");
            printer.resetAndBreak();
            printer.lineBreak();

            boardSelection.setX(workersPositions.get(workerSelection.getX()).getPosX());
            boardSelection.setY(workersPositions.get(workerSelection.getX()).getPosY());
            showBoard(model, boardSelection);

            input = readString();

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

    static int handleChooseBetweenMoveAndBuild()
    {
        String input;
        int choice = 0;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_CYAN);


        boolean spin = true;
        while(spin)
        {
            printer.print("INSERT M TO MOVE OR B TO BUILD: ");
            System.out.print(AnsiColors.ANSI_RESET);
            input = readString();

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

    static boolean handleWouldYouLikeToEndYourTurn()
    {
        String input;
        boolean choice = false;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_CYAN);


        boolean spin = true;
        while(spin)
        {
            printer.print("WOULD YOU LIKE TO END YOUR TURN HERE? Y/N ");
            System.out.print(AnsiColors.ANSI_RESET);
            input = readString();

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

    static void handleCurrentPlayer(int id, ModelCLI model)
    {
        model.setCurrentPlayerId(id);

        if(model.isGameOn())
            showBoard(model);
    }

    static void handleLose(ModelCLI model)
    {
        model.setAsSpectator();

        CanvasCLI canvas = new CanvasCLI(0,0,53,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        RectangleCLI loseAnnounce = new RectangleCLI(20,3,11,1);
        canvas.setTextColor(AnsiColors.ANSI_BLACK);
        loseAnnounce.setPalette(AnsiColors.ANSI_BG_RED);
        loseAnnounce.addText("       YOU LOST! I'M SORRY.");
        canvas.addOverlappingFigure(loseAnnounce);
        canvas.printFigure();
        showBoard(model, null);
    }

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

    //utils for handlers
    static private boolean isMoveAction(List<Action> possibleActions)
    {
        for(Action a:possibleActions)
        {
            if(!(a instanceof MoveAction))
                return false;
        }
        return true;
    }

    static private boolean isBuildAction(List<Action> possibleActions)
    {
        for(Action a:possibleActions)
        {
            if(!(a instanceof BuildAction))
                return false;
        }
        return true;
    }

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
            printer.print("Now! Make your build:");
            printer.resetAndBreak();
            printer.lineBreak();

            showBoard(model, selection);
            input = readString();

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


    //input-output methods
    public static String readString()
    {
        Scanner stdin = new Scanner(System.in);
        String word = stdin.nextLine();
        System.out.println();
        return word;

    }


    //show methods
    static void showBoard(ModelCLI model)
    {
        System.out.println("\n\n\n");
        showBoard(model, null);
    }

    //TODO: clean this
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
        innerLable.setPalette(AnsiColors.ANSI_BG_BLACK);
        back_canvas.addOverlappingFigure(innerLable);

        //create the (empty) piecebag label
        RectangleCLI buildLabel  = new RectangleCLI(37,25,16,11);
        buildLabel.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);
        back_canvas.addOverlappingFigure(buildLabel);
        RectangleCLI innerBuildLabel  = buildLabel.createInRelativeFrame(1,1,14,9);
        innerBuildLabel.setPalette(AnsiColors.ANSI_BG_BLACK);
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
            godDescription.setPalette(AnsiColors.ANSI_BG_WHITE);
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

    static void showWaitingForOtherPlayers()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_RED);

        //create text box
        RectangleCLI textBox = new RectangleCLI(13,2,9,1);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText(" WAITING OTHER PLAYERS TO JOIN ");

        //create frame
        RectangleCLI frame = textBox.createInRelativeFrame(-1,-1, 11,3);
        frame.setPalette(AnsiColors.ANSI_BRIGHT_BG_RED);

        //overlap figures in the correct order
        canvas.addOverlappingFigure(frame);
        canvas.addOverlappingFigure(textBox);

        //print the image
        canvas.printFigure();
    }


    private static void showCard(String desc, String title, int userSelection)
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI();
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_BLACK);

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
        name.setPalette(AnsiColors.ANSI_BG_WHITE);
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

    private static void showControls()
    {
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_BLACK+ "Use WASD to control the selection: W to go UP, S to go DOWN, A to go RIGHT, D to go LEFT");
        System.out.println(AnsiColors.ANSI_RESET);
    }

    private static void showNotYourTurn()
    {
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_BLACK+ "Please wait while the other players play");
        System.out.println(AnsiColors.ANSI_RESET);
    }

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


    //graphics
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
