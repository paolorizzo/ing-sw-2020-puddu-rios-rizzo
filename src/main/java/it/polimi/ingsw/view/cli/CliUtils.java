package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CliUtils
{
    //handler methods
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

    static int handleSetupWorker(int[][] board, List<Action> possibleActions, String[][] workersMask)
    {
        Space chosenSpace;
        int chosenAction = -1;

        while(chosenAction < 0)
        {
            chosenSpace = selectionOnBoard(workersMask, board, "Now! Choose where to put your worker");
            chosenAction = getActionNumber(chosenSpace.getPosX(),chosenSpace.getPosY(), possibleActions);
        }

        return chosenAction;
    }

    static int handleMoveAction(int[][] board, List<Action> possibleActions, String[][] workersMask, String workerId)
    {
        Space chosenSpace = null;
        int chosenAction = -1;

        while(chosenAction < 0)
        {
            chosenSpace = selectionOnBoard(workersMask, board, "Now! Make your move:");
            chosenAction = getMoveActionNumber(chosenSpace.getPosX(),chosenSpace.getPosY(), possibleActions, workerId);
        }

        //System.err.println("Sending move" + chosenAction+": X:"+ chosenSpace.getPosX() + " Y:"+chosenSpace.getPosY()+"  "+workerId);

        return chosenAction;
    }

    static int handleBuildAction(int[][] board, List<Action> possibleActions, String[][] workersMask, String workerId)
    {
        SpaceCLI chosenSpace;
        int chosenAction = -1;

        while(chosenAction < 0)
        {
            chosenSpace = selectAndBuildOnBoard(workersMask, board);
            chosenAction = getBuildActionNumber(chosenSpace.getX(),chosenSpace.getY(), possibleActions, workerId, chosenSpace.getLevel());
        }

        return chosenAction;
    }

    static int handleAction(List<Action> possibleActions, boolean canEndOfTurn, int[][] board, String[][] workersMask)
    {
        if(canEndOfTurn && handleWouldYouLikeToEndYourTurn())
            return possibleActions.size();

        String selectedWorker;
        int selectedAction = -1;
        if(isWorkerSelected(possibleActions))
            selectedWorker = possibleActions.get(0).getWorkerID();
        else
            selectedWorker = handleWorkerSelection(board, workersMask,possibleActions.get(0).getWorkerID().charAt(1)-'0');

        while(selectedAction < 0)
        {
            if(isMoveAction(possibleActions))
            {
                //System.err.println("This is a move action");
                selectedAction = handleMoveAction(board, possibleActions, workersMask, selectedWorker);
            }
            else if(isBuildAction(possibleActions))
            {
                //System.err.println("This is a build action");
                selectedAction = handleBuildAction(board, possibleActions, workersMask, selectedWorker);
            }
            else
            {
                if(handleChooseBetweenMoveAndBuild()==0)
                    selectedAction = handleMoveAction(board, possibleActions, workersMask, selectedWorker);
                else
                    selectedAction = handleBuildAction(board, possibleActions, workersMask, selectedWorker);
            }
        }
        return selectedAction;
    }

    static String handleWorkerSelection(int[][] board, String[][] workersMask, int idPlayer)
    {
        String input;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BLACK);
        List<Space> workersPositions = getWorkersPositions(workersMask, idPlayer);
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
            showBoard(board, boardSelection, workersMask);

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

        return workersMask[boardSelection.getX()][boardSelection.getY()];
    }

    static int handleNumPlayerSelection()
    {
        int numPlayers = 2;
        String input;
        boolean spin = true;

        while(spin)
        {
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

    //0 to move, 1 to build
    static int handleChooseBetweenMoveAndBuild()
    {
        String input;
        int choice = 0;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_CYAN);


        boolean spin = true;
        while(spin)
        {
            printer.print("INSERT M TO MOVE OR B TO BUILD: ");
            input = readString();
            printer.resetAndBreak();

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
            input = readString();
            printer.resetAndBreak();

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
        printer.resetAndBreak();
        return choice;
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

    static int getActionNumber(int x, int y, List<Action> actions)
    {
        for(int i = 0; i<actions.size(); i++)
        {
            if(actions.get(i).getTargetX() == x && actions.get(i).getTargetY() == y)
                return i;
        }

        return -1;
    }

    //TODO change to Space and Piece instead of int...
    static int getMoveActionNumber(int x, int y, List<Action> possibleActions, String workerId)
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

    //TODO maybe use generic the unificate the similar methods
    static int getBuildActionNumber(int x, int y, List<Action> possibleActions, String workerId, int level)
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

    static void slideDown(int rows, int rateInMilliseconds)
    {
        for(int i = 0; i<rows; i++)
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(rateInMilliseconds);
                //System.out.println(".");
            }
            catch(InterruptedException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }

    static private Space selectionOnBoard(String[][] workersMask, int[][] board, String message)
    {
        String input;
        Space chosenSpace = null;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BLACK);
        BidimensionalSelectionCLI selection = new BidimensionalSelectionCLI(0,4,true,0,4,true);

        boolean spin = true;
        while(spin)
        {
            printer.lineBreak();
            showControls();
            printer.print(message);
            printer.resetAndBreak();
            printer.lineBreak();

            showBoard(board, selection, workersMask);
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

    static private SpaceCLI selectAndBuildOnBoard(String[][] workersMask, int[][] board)
    {
        String input;
        SpaceCLI chosenSpace = null;
        PrintCLI printer = new PrintCLI(AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BLACK);
        BidimensionalSelectionCLI selection = new BidimensionalSelectionCLI(0,4,true,0,4,true);

        boolean spin = true;
        while(spin)
        {
            printer.lineBreak();
            showControls();
            printer.print("Now! Make your build:");
            printer.resetAndBreak();
            printer.lineBreak();

            showBoard(board, selection, workersMask);
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
    static int readInt()
    {
        Scanner stdin = new Scanner(System.in);
        int num = stdin.nextInt();
        System.out.println();
        return num;
    }

    public static String readString()
    {
        Scanner stdin = new Scanner(System.in);
        String word = stdin.nextLine();
        System.out.println();
        return word;

    }


    //show methods
    static void showUpdatedBoard(int[][] intBoard, String[][] workersMask)
    {
        System.out.println();
        System.out.println();
        //create Canvas
        CanvasCLI canvas = new CanvasCLI();
        canvas.setPalette(AnsiColors.ANSI_BG_GREEN);

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
        Iterator<Integer> boardIterator = getBoardIterator(intBoard);
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
                    worker.setMask("./src/main/resources/worker.txt");

                    switch(workersMask[row][col].charAt(1))
                    {
                        case '0':
                            worker.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BG_CYAN);
                            break;
                        case '1':
                            worker.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BG_PURPLE);
                            break;
                        case '2':
                            worker.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BRIGHT_BG_RED);
                            break;
                        default:
                            break;
                    }
                    canvas.addOverlappingFigure(worker);
                }
            }
        }

        //print the figure
        canvas.printFigure();
    }

    static private void showBoard(int [][] intBoard, BidimensionalSelectionCLI selection, String[][] workersMask)
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI();
        canvas.setPalette(AnsiColors.ANSI_BG_GREEN);

        //create selection
        RectangleCLI selectionFigure = new RectangleCLI(selection.getX()*7, selection.getY()*7, 8,8);
        selectionFigure.setPalette(AnsiColors.ANSI_BG_RED);

        //create spaces
        List<RectangleCLI> spaces = new ArrayList<>();
        for(int row = 1; row<36; row+=7)
        {
            for(int col = 1; col<36; col+=7)
            {
                spaces.add(new RectangleCLI(row, col, 6,6 ));
            }
        }

        //add the selection to the figure
        canvas.addOverlappingFigure(selectionFigure);

        //decorate spaces and add them to the figure
        Iterator<Integer> boardIterator = getBoardIterator(intBoard);
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
                    worker.setMask("./src/main/resources/worker.txt");

                    switch(workersMask[row][col].charAt(1))
                    {
                        case '0':
                            worker.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BG_CYAN);
                            break;
                        case '1':
                            worker.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BG_PURPLE);
                            break;
                        case '2':
                            worker.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BRIGHT_BG_RED);
                            break;
                        default:
                            break;
                    }

                    canvas.addOverlappingFigure(worker);
                }
            }
        }

        //print the figure
        canvas.printFigure();
    }

    static void showUsernameDialog()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_RESET);

        /*
        //create arrow
        RectangleCLI lowArrow = new RectangleCLI(13,1,9,6);
        lowArrow.setMask("./src/main/resources/arrow3.txt");
        lowArrow.setPalette(Color.ANSI_BG_BLACK, Color.ANSI_BRIGHT_BG_BLUE);
         */

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

    private static void showCard(String desc, String title, int userSelection)
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI();
        canvas.setPalette(AnsiColors.ANSI_RESET);
        canvas.setTextColor(AnsiColors.ANSI_BLACK);
        //canvas.setPalette(Color.ANSI_BG_BLACK);

        //create card
        RectangleCLI card = new RectangleCLI(10,4,16,27);
        card.setMask("./src/main/resources/card.txt");
        card.setPalette(AnsiColors.ANSI_BG_YELLOW, AnsiColors.ANSI_BRIGHT_BG_YELLOW);

        //create text box
        RectangleCLI textBox = card.createInRelativeFrame(2,18,12,7);
        textBox.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLACK);
        textBox.addText(desc);

        //create picture box
        RectangleCLI pic = card.createInRelativeFrame(2,2,12,13);
        pic.setPalette(AnsiColors.ANSI_BG_BLUE);

        RectangleCLI name = textBox.createInRelativeFrame(0,-2,12,1);
        name.setPalette(AnsiColors.ANSI_BG_WHITE);
        name.addText(title);

        //create selection box
        RectangleCLI selection = card.createInRelativeFrame(-1,-1,18,29);
        selection.setPalette(userSelection == 1? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_RESET);

        //create arrows
        RectangleCLI rightArrow = new RectangleCLI(27,14,8,9);
        rightArrow.setMask("./src/main/resources/arrow.txt");
        rightArrow.setPalette(AnsiColors.ANSI_RESET, userSelection == 2? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_BG_WHITE);

        RectangleCLI leftArrow = new RectangleCLI(1,14,8,9);
        leftArrow.setMask("./src/main/resources/arrow2.txt");
        leftArrow.setPalette(AnsiColors.ANSI_RESET, userSelection == 0? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_BG_WHITE);

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

    public static void showNumPlayersDialog(int num)
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI();
        canvas.setPalette(AnsiColors.ANSI_RESET);

        //create number 2
        RectangleCLI number2 = new RectangleCLI(5,11,12,12);
        number2.setMask("./src/main/resources/two.txt");
        number2.setPalette(num == 2? AnsiColors.ANSI_BG_BLUE : AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BRIGHT_BG_BLACK);

        //create number 3
        RectangleCLI number3 = new RectangleCLI(17,11,12,12);
        number3.setMask("./src/main/resources/three.txt");
        number3.setPalette(num == 3? AnsiColors.ANSI_BG_BLUE : AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BRIGHT_BG_BLACK);

        //create text box
        RectangleCLI textBox = new RectangleCLI(12,5,10,1);
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


    //matrix methods
    private static Iterator<Integer> getBoardIterator(int[][] board)
    {
        ArrayList<Integer> numbers = new ArrayList<>();

        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                numbers.add(board[row][col]);
            }
        }

        return numbers.iterator();
    }

    private static int[][] generateRandomBoard()
    {
        int board[][] = new int[5][5];
        Random rand =  new Random();

        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                board[row][col] = rand.nextInt(5);
            }
        }

        return board;
    }

    static int[][] generateEmptyBoard()
    {
        int[][] board = new int[5][5];

        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                board[row][col] = 0;
            }
        }

        return board;
    }

    static String[][] generateEmptyWorkersMask()
    {
        String[][] board = new String[5][5];

        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                board[row][col] = "";
            }
        }

        return board;
    }


    //updates
    static void updateMaskOnMove(String[][] mask, int targetX, int targetY, String workerId)
    {
        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                if(mask[row][col].equals(workerId))
                    mask[row][col] = "";
            }
        }

        mask[targetX][targetY] = workerId;
    }

    static void updateBoardOnBuild(int[][] board, BuildAction action)
    {
        board[action.getTargetX()][action.getTargetY()] = action.getPiece().getLevel();
    }
}
