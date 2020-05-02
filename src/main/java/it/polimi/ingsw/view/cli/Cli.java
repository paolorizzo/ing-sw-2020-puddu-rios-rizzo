package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observation.UserInterfaceObservable;
import it.polimi.ingsw.view.UserInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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

        notifyReadNumPlayers(handleNumPlayerSelection());
    }

    @Override
    public void askUsername()
    {
        showUsernameDialog();
        System.out.println();

        String name = readString();
        slideDown(36,1);

        notifyReadName(name);
    }

    //TODO check not null
    @Override
    public void askCard(Deck deck)
    {
        List<Card> cards = deck.getCards();
        notifyReadNumCard(cards.get(handleCardSelection(cards)).getNum());
    }

    @Override
    public void askGod(List<Card> cards)
    {

        notifyReadGod(cards.get(handleCardSelection(cards)).getNum());
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
        int num = readInt();
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
    public void removeWorkersOfPlayer(int id)
    {

    }

    
    //selection handlers
    private int handleCardSelection(List<Card> cards)
    {
        int numCard = 0;
        int selection = 1;
        String input;

        boolean spin = true;

        while(spin)
        {
            //showCardDialog
            showControls();
            System.out.println();
            showCard(cards.get(numCard).getDescription(), cards.get(numCard).getName().toUpperCase(), selection);
            input = readString();

            switch(input)
            {
                case "d":
                    if(selection == 2)
                        selection = 0;
                    else
                        selection++;
                    break;
                case "a":
                    if(selection != 0)
                        selection--;
                    break;
                case "":
                    if(selection == 1)
                        spin = false;

                    if(selection == 0)
                        numCard = Math.max(numCard - 1, 0);

                    if(selection == 2)
                        numCard  = Math.min(numCard + 1, cards.size()-1);
                    selection = 1;
                    break;
                default:
                    break;
            }
        }

        return numCard;
    }

    private int handleNumPlayerSelection()
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


    //scenes
    public void showUsernameDialog()
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

    private static void showControls()
    {
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_BLACK+ "Use WASD to control the selection: W to go UP, S to go DOWN, A to go RIGHT, D to go LEFT");
        System.out.println(AnsiColors.ANSI_RESET);
    }

    public void showNumPlayersDialog(int num)
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


    //utils
    private static void slideDown(int rows, int rateInMilliseconds)
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

    //wrong input is handled by FSM
    private int readInt()
    {
        Scanner stdin = new Scanner(System.in);
        int num = stdin.nextInt();
        System.out.println();
        return num;
    }

    //utility function used to get a string from user input
    public String readString()
    {
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
