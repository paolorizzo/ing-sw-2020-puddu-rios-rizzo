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
    public Cli(){
        players = new HashMap<>();
    }

    @Override
    public void showLogo()
    {
        /*
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
         */
    }

    @Override
    public void askNumPlayers(){
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
                    notifyReadNumPlayers(2);
                    spin = false;
                    break;
                case("3"):
                    notifyReadNumPlayers(3);
                    spin = false;
                    break;
                case("d"):
                case("a"):
                    numPlayers = numPlayers == 3? 2:3;
                    break;
                case(""):
                    notifyReadNumPlayers(numPlayers);
                    spin = false;
                    break;
                default:
                    break;
            }
        }
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

    @Override
    public void askUsername() {
        showUsernameDialog();
        System.out.println();
        String name = readString();
        slideDown(36,1);
        notifyReadName(name);
    }

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
