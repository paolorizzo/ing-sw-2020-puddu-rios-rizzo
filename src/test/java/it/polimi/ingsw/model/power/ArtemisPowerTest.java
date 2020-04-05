package it.polimi.ingsw.model.power;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.power.ApolloPower;
import it.polimi.ingsw.model.power.PowerStrategy;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ArtemisPowerTest {

    @Test
    public void gameRandomTestTwoPlayer(){
        int numberOfTest = 10;
        for(int seed=0;seed<numberOfTest;seed++)
            gameRandomTestTwoPlayer(seed);
    }
    public void gameRandomTestTwoPlayer(int seed) {
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

        PowerStrategy powerStrategy = new ArtemisPower();

        Random rand = new Random(seed);

        int numTurn = 0;
        ActionTree result;
        while(true){
            if(numTurn%2==0){
                result = powerStrategy.generateActionTree(board, p1);
            }else{
                result = powerStrategy.generateActionTree(board, p2);
            }
            ActionTree curr = result;
            while(!curr.isEndOfTurn()){
                int size = curr.getChildren().size();
                if(size == 0)
                    assert(false);
                int numChild = rand.nextInt(size);
                curr = curr.getChildren().get(numChild);
                board.executeAction(curr.getAction());
            }
            if(curr.isWin() || curr.isLose())
                break;
            numTurn++;
        }
        assert(true);
    }
    @Test
    public void gameRandomTestThreePlayer(){
        int numberOfTest = 10;
        for(int seed=0;seed<numberOfTest;seed++)
            gameRandomTestThreePlayer(seed);
    }
    public void gameRandomTestThreePlayer(int seed) {
        Vector<Player> p = new Vector<Player>();

        Player p1 = new Player("Francesco", Color.BLUE, 1);
        Player p2 = new Player("Paolo", Color.WHITE, 2);
        Player p3 = new Player("Federico", Color.GREY, 3);
        p.add(p1);
        p.add(p2);
        p.add(p3);

        Board board = new Board(Arrays.asList(p1, p2, p3));
        Action a1 = new SetupAction(p1.getWorker(Sex.MALE).toString(), 1, 1);
        Action a2 = new SetupAction(p1.getWorker(Sex.FEMALE).toString(), 2, 2);
        Action a3 = new SetupAction(p2.getWorker(Sex.MALE).toString(), 1, 3);
        Action a4 = new SetupAction(p2.getWorker(Sex.FEMALE).toString(), 3, 1);
        Action a5 = new SetupAction(p3.getWorker(Sex.MALE).toString(), 4, 4);
        Action a6 = new SetupAction(p3.getWorker(Sex.FEMALE).toString(), 0, 0);
        board.executeAction(a1);
        board.executeAction(a2);
        board.executeAction(a3);
        board.executeAction(a4);
        board.executeAction(a5);
        board.executeAction(a6);

        PowerStrategy powerStrategy = new ArtemisPower();

        Random rand = new Random(seed);

        int numTurn = 0, playerTurn = 0;
        ActionTree result;
        System.out.println("gameRandomTestThreePlayer: Game #"+seed);
        while(true){
            Player currPlayer = p.get(playerTurn%p.size());
            System.out.println("gameRandomTestThreePlayer: Num Turn "+numTurn+" player: "+currPlayer.getNickname());
            result = powerStrategy.generateActionTree(board, currPlayer);
            ActionTree curr = result;
            while(!curr.isEndOfTurn()){
                int size = curr.getChildren().size();
                if(size == 0)
                    assert(false);
                int numChild = rand.nextInt(size);
                curr = curr.getChildren().get(numChild);
                System.out.println("\t"+curr.getAction());
                board.executeAction(curr.getAction());
            }
            if(curr.isWin()){
                System.out.println(currPlayer.getNickname()+" won");
                assert(true);
                return;
            }
            if(curr.isLose()){
                System.out.println("\t"+currPlayer.getNickname()+" lose");
                p.remove(currPlayer);
                playerTurn--;
                if(p.size()==1){
                    System.out.println(p.get(0).getNickname()+" won");
                    assert(true);
                    return;
                }
            }
            numTurn++;
            playerTurn = (playerTurn+1) % p.size();
        }
    }

}

