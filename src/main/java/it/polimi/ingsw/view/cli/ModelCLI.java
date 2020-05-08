package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.BuildAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Stores the data required for CLI representations.
 */
public class ModelCLI
{
    /**
     * An integer matrix storing the highest building in every space of the board.
     */
    int[][] board;

    /**
     * A mask is a String matrix that stores workers' IDs in the cells corresponding to their position on the board.
     */
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

    /**
     * Create an iterator of the board matrix.
     * @return the iterator.
     */
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

    /**
     * Create and initialize the representation of an empty 5x5 game board.
     * @return the board matrix.
     */
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

    /**
     * Create and initialize the representation of an empty 5x5 workers mask.
     * @return the mask matrix.
     */
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

    //TODO: MoveAction instead of Action
    /**
     * Updates the local workers' mask after a move.
     * @param action the instance of MoveAction just performed.
     */
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

    /**
     * Updates the local workers' mask after a build.
     * @param action the instance of BuildAction just performed.
     */
    void updateBoardOnBuild(BuildAction action)
    {

        board[action.getTargetX()][action.getTargetY()] = action.getPiece().getLevel();
    }

    /**
     * Remove the workers of a chosen player from the workers' mask.
     * @param id the ID of the player whose workers have to be removed from the local representation.
     */
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
