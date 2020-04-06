package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.*;

public class PowerStrategyTest {

    @Test
    public void gameRandomTest(){
        int numberOfTest = 1000;
        Random rand = new Random(42);
        for(int seed=0;seed<numberOfTest;seed++){
            gameRandomTest(seed, rand.nextInt(2)+2);
        }
    }
    public void gameRandomTest(int seed, int numOfPlayer) {

        System.out.println("Game #"+seed+" numOfPlayer "+numOfPlayer);

        Random rand = new Random(seed);

        rand.nextInt();

        List<Player> players = new ArrayList<>();

        Board board;
        Player p1 = new Player("Francesco", Color.BLUE, 1);

        Player p2 = new Player("Paolo", Color.WHITE, 2);


        players.add(p1);
        players.add(p2);
        if(numOfPlayer == 3) {
            Player p3 = new Player("Federico", Color.GREY, 3);
            players.add(p3);
        }

        TurnArchive turnArchive = new TurnArchive();
        board = new Board(players);

        HashMap<Integer, PowerTest> powersTest = new HashMap<>();
        powersTest.put(1, new ApolloPowerTest());
        powersTest.put(2, new ArtemisPowerTest());
        powersTest.put(3, new AthenaPowerTest());
        powersTest.put(4, new AtlasPowerTest());
        powersTest.put(5, new DemeterPowerTest());
        powersTest.put(6, new HephaestusPowerTest());
        powersTest.put(8, new MinotaurPowerTest());

        for(Player player: players) {

            List<Card> cards = board.getDeck().getCards();

            player.setCard(board.getDeck().pickCard(cards.get(rand.nextInt(cards.size())).getNum()));

            System.out.println(player.getNickname()+" pick the card "+player.getCard().getName());

            for(Sex sex: Sex.values()){
                int x = rand.nextInt(5), y = rand.nextInt(5);
                while(board.getSpaces()[x][y].hasWorkerOnIt()){
                    x = rand.nextInt(5);
                    y = rand.nextInt(5);
                }
                board.executeAction(new SetupAction(player.getWorker(sex).toString(), x, y));
                System.out.println(player.getNickname()+" setup worker "+sex+" on "+x+" "+y);
            }

        }



        int numTurn = 0, playerTurn = 0;
        ActionTree result;
        while(true){
            Player currPlayer = players.get(playerTurn%players.size());
            System.out.println("Num Turn "+numTurn+" player: "+currPlayer.getNickname());

            //generate ActionTree
            result = currPlayer.getCard().getPowerStrategy().generateActionTree(board, currPlayer);
            //pruning ActionTree
            for(Player opponent: players){
                if(!opponent.equals(currPlayer) && opponent.getCard().getPowerStrategy().requirePruning(turnArchive.getLastTurnOf(opponent))){
                        opponent.getCard().getPowerStrategy().pruneActionTree(result);
                }
            }

            //generate random turn
            ActionTree curr = result;
            Turn turn = new Turn(currPlayer);
            while(!curr.isEndOfTurn() || (curr.isEndOfTurn() && curr.getChildren().size()!=0 && rand.nextInt(2) == 1)){
                int size = curr.getChildren().size();
                if(size == 0)
                    assert(false);
                int numChild = rand.nextInt(size);
                curr = curr.getChildren().get(numChild);
                System.out.println("\t"+curr.getAction());
                turn.add(curr.getAction());
            }
            for(Player opponent: players){
                if(!opponent.equals(currPlayer) && opponent.getCard().getPowerStrategy().requirePruning(turnArchive.getLastTurnOf(opponent))){
                    powersTest.get(opponent.getCard().getNum()).assertPruning(turn);
                }
            }
            //assert and execute turn
            powersTest.get(currPlayer.getCard().getNum()).assertPower(board, turn, curr);

            if(curr.isWin()){
                System.out.println(currPlayer.getNickname()+" won");
                assert(true);
                return;
            }
            if(curr.isLose()){
                System.out.println("\t"+currPlayer.getNickname()+" lose");
                players.remove(currPlayer);
                playerTurn--;
                if(players.size()==1){
                    System.out.println(players.get(0).getNickname()+" won");
                    assert(true);
                    return;
                }
            }
            turnArchive.addTurn(turn);
            numTurn++;
            playerTurn = (playerTurn+1) % players.size();
        }
    }
    @Test
    public void gameTest(){
        Player p1 = new Player("Matteo", Color.BLUE, 1);
        Player p2 = new Player("Paolo", Color.WHITE, 2);
        Board board = new Board(Arrays.asList(p1, p2));
        Action a1 = new SetupAction(p1.getWorker(Sex.MALE).toString(), 1, 1);
        Action a2 = new SetupAction(p1.getWorker(Sex.FEMALE).toString(), 2, 2);
        Action a3 = new SetupAction(p2.getWorker(Sex.MALE).toString(), 1, 3);
        Action a4 = new SetupAction(p2.getWorker(Sex.FEMALE).toString(), 3, 1);
        board.executeAction(a1);
        board.executeAction(a2);
        board.executeAction(a3);
        board.executeAction(a4);

        PowerStrategy powerStrategy = new PowerStrategy();

        //turn 1
        ActionTree result = powerStrategy.generateActionTree(board, p1);

        Action m1_1 = new MoveAction("P1-F", 3, 2, Direction.SAME, 2, 2);
        Action b1_1 = new BuildAction("P1-F", 4, 3, Piece.LEVEL1);

        assert(result.isPathPresent(Arrays.asList(m1_1, b1_1)));

        board.executeAction(m1_1);
        board.executeAction(b1_1);

        //turn 2
        result = powerStrategy.generateActionTree(board, p2);

        Action m2_1 = new MoveAction("P2-F", 4, 2, Direction.SAME, 3, 1);
        Action b2_1 = new BuildAction("P2-F", 4, 3, Piece.LEVEL2);

        assert(result.isPathPresent(Arrays.asList(m2_1, b2_1)));

        board.executeAction(m2_1);
        board.executeAction(b2_1);

        //turn 3
        result = powerStrategy.generateActionTree(board, p1);

        Action m1_2 = new MoveAction("P1-M", 0, 0, Direction.SAME, 1, 1);
        Action b1_2 = new BuildAction("P1-M", 1, 0, Piece.LEVEL1);

        assert(result.isPathPresent(Arrays.asList(m1_2, b1_2)));

        board.executeAction(m1_2);
        board.executeAction(b1_2);

        //turn 4
        result = powerStrategy.generateActionTree(board, p2);

        Action m2_2 = new MoveAction("P2-F", 3, 3, Direction.SAME, 4, 2);
        Action b2_2 = new BuildAction("P2-F", 4, 4, Piece.LEVEL1);

        assert(result.isPathPresent(Arrays.asList(m2_2, b2_2)));

        board.executeAction(m2_2);
        board.executeAction(b2_2);

        //turn 5
        result = powerStrategy.generateActionTree(board, p1);

        Action m1_3 = new MoveAction("P1-F", 4, 2, Direction.SAME, 3, 2);
        Action b1_3 = new BuildAction("P1-F", 4, 3, Piece.LEVEL3);

        assert(result.isPathPresent(Arrays.asList(m1_3, b1_3)));

        board.executeAction(m1_3);
        board.executeAction(b1_3);

        //turn 6
        result = powerStrategy.generateActionTree(board, p2);

        Action m2_3 = new MoveAction("P2-M", 2, 3, Direction.SAME, 1, 3);
        Action b2_3 = new BuildAction("P2-M", 3, 4, Piece.LEVEL1);

        assert(result.isPathPresent(Arrays.asList(m2_3, b2_3)));

        board.executeAction(m2_3);
        board.executeAction(b2_3);

        //turn 7
        result = powerStrategy.generateActionTree(board, p1);

        Action m1_4 = new MoveAction("P1-M", 1, 0, Direction.UP, 0, 0);
        Action b1_4 = new BuildAction("P1-M", 2, 0, Piece.LEVEL1);

        assert(result.isPathPresent(Arrays.asList(m1_4, b1_4)));

        board.executeAction(m1_4);
        board.executeAction(b1_4);

        //turn 8

        result = powerStrategy.generateActionTree(board, p2);

        Action m2_4 = new MoveAction("P2-F", 4, 4, Direction.UP, 3, 3);
        Action b2_4 = new BuildAction("P2-F", 3, 4, Piece.LEVEL2);

        assert(result.isPathPresent(Arrays.asList(m2_4, b2_4)));

        board.executeAction(m2_4);
        board.executeAction(b2_4);

        //turn 9

        result = powerStrategy.generateActionTree(board, p1);

        Action m1_5 = new MoveAction("P1-F", 3, 2, Direction.SAME, 4, 2);
        Action b1_5 = new BuildAction("P1-F", 4, 3, Piece.DOME);

        assert(result.isPathPresent(Arrays.asList(m1_5, b1_5)));

        board.executeAction(m1_5);
        board.executeAction(b1_5);

        //turn 10

        result = powerStrategy.generateActionTree(board, p2);

        Action m2_5 = new MoveAction("P2-F", 3, 4, Direction.UP, 4, 4);
        Action b2_5 = new BuildAction("P2-F", 2, 4, Piece.LEVEL1);

        assert(result.isPathPresent(Arrays.asList(m2_5, b2_5)));

        board.executeAction(m2_5);
        board.executeAction(b2_5);

        //turn 11
        result = powerStrategy.generateActionTree(board, p1);

        Action m1_6 = new MoveAction("P1-M", 2, 0, Direction.SAME, 1, 0);
        Action b1_6 = new BuildAction("P1-M", 2, 1, Piece.LEVEL1);

        assert(result.isPathPresent(Arrays.asList(m1_6, b1_6)));

        board.executeAction(m1_6);
        board.executeAction(b1_6);

        //turn 12
        result = powerStrategy.generateActionTree(board, p2);

        Action m2_6 = new MoveAction("P2-M", 3, 3, Direction.SAME, 2, 3);
        Action b2_6 = new BuildAction("P2-M", 4, 4, Piece.LEVEL2);

        assert(result.isPathPresent(Arrays.asList(m2_6, b2_6)));

        board.executeAction(m2_6);
        board.executeAction(b2_6);

        //turn 13
        result = powerStrategy.generateActionTree(board, p1);

        Action m1_7 = new MoveAction("P1-M", 2, 1, Direction.SAME, 2, 0);
        Action b1_7 = new BuildAction("P1-M", 2, 0, Piece.LEVEL2);

        assert(result.isPathPresent(Arrays.asList(m1_7, b1_7)));

        board.executeAction(m1_7);
        board.executeAction(b1_7);

        //turn 14
        result = powerStrategy.generateActionTree(board, p2);

        Action m2_7 = new MoveAction("P2-M", 2, 3, Direction.SAME, 3, 3);
        Action b2_7 = new BuildAction("P2-M", 2, 4, Piece.LEVEL2);

        assert(result.isPathPresent(Arrays.asList(m2_7, b2_7)));

        board.executeAction(m2_7);
        board.executeAction(b2_7);

        //turn 15

        result = powerStrategy.generateActionTree(board, p1);

        Action m1_8 = new MoveAction("P1-M", 2, 0, Direction.UP, 2, 1);
        Action b1_8 = new BuildAction("P1-M", 1, 0, Piece.LEVEL2);

        assert(result.isPathPresent(Arrays.asList(m1_8, b1_8)));

        board.executeAction(m1_8);
        board.executeAction(b1_8);

        //turn 16
        result = powerStrategy.generateActionTree(board, p2);

        Action m2_8 = new MoveAction("P2-M", 1, 4, Direction.SAME, 2, 3);
        Action b2_8 = new BuildAction("P2-M", 2, 4, Piece.LEVEL3);

        assert(result.isPathPresent(Arrays.asList(m2_8, b2_8)));

        board.executeAction(m2_8);
        board.executeAction(b2_8);

        //turn 15

        result = powerStrategy.generateActionTree(board, p1);

        Action m1_9 = new MoveAction("P1-M", 1, 0, Direction.SAME, 2, 0);
        Action b1_9 = new BuildAction("P1-M", 2, 0, Piece.LEVEL3);

        assert(result.isPathPresent(Arrays.asList(m1_9, b1_9)));

        board.executeAction(m1_9);
        board.executeAction(b1_9);

        //turn 16

        result = powerStrategy.generateActionTree(board, p2);

        Action m2_9 = new MoveAction("P2-F", 2, 4, Direction.UP, 3, 4);

        assert(result.isPathPresent(Arrays.asList(m2_9)));
        board.executeAction(m2_9);

    }

    @Test
    public void testPruning(){
        Player p1 = new Player("Paolo", Color.BLUE, 1);
        Player p2 = new Player("Federico", Color.WHITE, 2);
        Board board = new Board(Arrays.asList(p1, p2));
        Action a1 = new SetupAction(p1.getWorker(Sex.MALE).toString(), 1, 3);
        Action a2 = new SetupAction(p1.getWorker(Sex.FEMALE).toString(), 3, 3);
        Action a3 = new SetupAction(p2.getWorker(Sex.MALE).toString(), 0, 4);
        Action a4 = new SetupAction(p2.getWorker(Sex.FEMALE).toString(), 2, 2);
        board.executeAction(a1);
        board.executeAction(a2);
        board.executeAction(a3);
        board.executeAction(a4);

        PowerStrategy powerStrategy = new PowerStrategy();
        assert(!powerStrategy.requirePruning(null));
        ActionTree result = powerStrategy.generateActionTree(board, p1);
        ActionTree result2 = powerStrategy.generateActionTree(board, p1);
        powerStrategy.pruneActionTree(result2);

        assert(result.equals(result2));
    }
}

