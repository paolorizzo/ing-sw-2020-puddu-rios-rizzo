package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.SetupAction;
;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CliUtils
{
    static void printBlueOnWhite(String s)
    {
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_BLACK + s);
        System.out.println(AnsiColors.ANSI_RESET);
    }

    static void printRedOnWhite(String s)
    {
        System.out.print(AnsiColors.ANSI_RED + AnsiColors.ANSI_BG_BLACK + s);
        System.out.println(AnsiColors.ANSI_RESET);
    }

    static void printBlueOnWhiteSameLine(String s)
    {
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_BLACK + s);
        System.out.print(AnsiColors.ANSI_RESET);
        System.out.print(" ");
    }

    private static void showCard(String desc, String title, int userSelection)
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI();
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
        selection.setPalette(userSelection == 1? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_BG_BLACK);

        //create arrows
        RectangleCLI rightArrow = new RectangleCLI(27,14,8,9);
        rightArrow.setMask("./src/main/resources/arrow.txt");
        rightArrow.setPalette(AnsiColors.ANSI_BG_BLACK, userSelection == 2? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_BRIGHT_BG_BLACK);

        RectangleCLI leftArrow = new RectangleCLI(1,14,8,9);
        leftArrow.setMask("./src/main/resources/arrow2.txt");
        leftArrow.setPalette(AnsiColors.ANSI_BG_BLACK, userSelection == 0? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_BRIGHT_BG_BLACK);

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

    public static String readString()
    {
        Scanner stdin = new Scanner(System.in);
        String word = stdin.nextLine();
        System.out.println();
        return word;

    }

    private static void showControls()
    {
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_BLACK+ "Use WASD to control the selection: W to go UP, S to go DOWN, A to go RIGHT, D to go LEFT");
        System.out.println(AnsiColors.ANSI_RESET);
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

    static int handleWorkerSet(List<Action> possibleActions, int[][] workersMask)
    {
        String input;
        int chosenAction = 0;
        BidimensionalSelectionCLI selection = new BidimensionalSelectionCLI(0,4,true,0,4,true);

        boolean spin = true;
        while(spin)
        {
            showControls();
            System.out.println();

            showBoard(generateEmptyBoard(), selection, possibleActions, workersMask);
            input = readString();

            if(input.equals(""))
            {
                chosenAction = getActionNumber(selection.getX(), selection.getY(), possibleActions);

                if( chosenAction >= 0)
                {
                    spin = false;
                }
            }
            else
            {
                selection.handleWASD(input);
            }
        }
        return chosenAction;
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

    static int handleNumPlayerSelection()
    {
        int numPlayers = 2;
        String input;
        boolean spin = true;

        while(spin)
        {
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

    public static void showNumPlayersDialog(int num)
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI();

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

    static void showUsernameDialog()
    {
        //create Canvas
        CanvasCLI canvas = new CanvasCLI(0,0,36,5);
        canvas.setPalette(AnsiColors.ANSI_BG_BLACK);

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

    static int readInt()
    {
        Scanner stdin = new Scanner(System.in);
        int num = stdin.nextInt();
        System.out.println();
        return num;
    }

    public void showCustomError(String s)
    {
        CliUtils.printRedOnWhite(s);
        System.out.println();
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

    private static void showBoard(int [][] intBoard, BidimensionalSelectionCLI selection, List<Action> possibleActions, int[][] workersMask)
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

        //create highlights
        for(Action a: possibleActions)
        {
            RectangleCLI highlightedCell = new RectangleCLI(a.getTargetX()*7, a.getTargetY()*7, 8,8);
            highlightedCell.setPalette(AnsiColors.ANSI_BG_CYAN);
            canvas.addOverlappingFigure(highlightedCell);
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
                if(workersMask[row][col] >= 0)
                {
                    RectangleCLI worker = new RectangleCLI(row*7+3, col*7+3, 2,2);
                    worker.setPalette(AnsiColors.ANSI_BG_BLACK);
                    worker.addText("  /\\    /\\");
                    canvas.addOverlappingFigure(worker);
                }
            }
        }

        //print the figure
        canvas.printFigure();
    }

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

    private static int[][] generateEmptyBoard()
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

    static int[][] generateEmptyWorkersMask()
    {
        int[][] board = new int[5][5];

        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                board[row][col] = -1;
            }
        }

        return board;
    }

    static void updateWorkersMask(int[][] mask, SetupAction action)
    {

        mask[action.getTargetX()][action.getTargetY()] = action.getWorkerID().charAt(1)-'0';
    }
}
