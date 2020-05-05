package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.BuildAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ModelCLI
{
    int[][] board;
    String [][] workers;

    public ModelCLI()
    {
        board = generateEmptyBoard();
        workers = generateEmptyWorkersMask();
    }

    public int[][] getBoard()
    {

        return board;
    }

    public String[][] getWorkers()
    {

        return workers;
    }

    Iterator<Integer> getBoardIterator()
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

    static int[][] generateRandomBoard()
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

    void updateMaskOnMove(Action action)
    {
        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                if(workers[row][col].equals(action.getWorkerID()))
                    workers[row][col] = "";
            }
        }

        workers[action.getTargetX()][action.getTargetY()] = action.getWorkerID();
    }

    void updateBoardOnBuild(BuildAction action)
    {

        board[action.getTargetX()][action.getTargetY()] = action.getPiece().getLevel();
    }

    void removeWorkersOfPlayer(int id)
    {
        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                if(workers[row][col].charAt(1)-'0' == id)
                    workers[row][col] = "";
            }
        }
    }
}
