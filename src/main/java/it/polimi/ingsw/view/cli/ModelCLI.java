package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;

import java.util.*;

/**
 * Stores the data required for CLI representations.
 */
public class ModelCLI
{
    /**
     * An integer matrix storing the highest building in every space of the board.
     */
    int[][] board;

    private int currentPlayerId;

    private boolean gameMode;

    private boolean spectator;

    private boolean myTurn;

    /**
     * A mask is a String matrix that stores workers' IDs in the cells corresponding to their position on the board.
     */
    String [][] workers;

    private HashMap<Integer, Player> players;
    private HashMap<Integer, Integer> pieceBag;

    public ModelCLI()
    {
        board = generateEmptyBoard();
        workers = generateEmptyWorkersMask();
        players = new HashMap<>();
        pieceBag = new HashMap<>();
        currentPlayerId = 0;
        gameMode = false;
        spectator = false;
        myTurn = false;
        pieceBag.put(1, 22);
        pieceBag.put(2, 18);
        pieceBag.put(3, 14);
        pieceBag.put(4, 18);
    }

    void setAsSpectator()
    {
        if(players.keySet().size() == 3)
        {
            this.spectator = true;
        }
    }

    boolean getSpectator()
    {
        return this.spectator;
    }

    boolean isGameOn()
    {
        return gameMode;
    }

    void setGameMode()
    {
        this.gameMode = true;
    }

    void setCurrentPlayerId(int id)
    {
        if(players.keySet().contains(id))
        {
            this.currentPlayerId = id;
        }
    }

    int getCurrentPlayerId()
    {
        return currentPlayerId;
    }

    int getPiecesLeft(int level)
    {
        return pieceBag.get(level);
    }

    void addPlayer(int id, Player player)
    {
        players.put(id, player);
    }

    Player getPlayer(int id)
    {
        return players.get(id);
    }

    int getNumPlayers()
    {
        return players.size();
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

    Iterator<Player> getPlayersIterator()
    {
        Collection<Player> players_list = players.values();
        return players_list.iterator();
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
        pieceBag.put(action.getPiece().getLevel(), pieceBag.get(action.getPiece().getLevel()) - 1);
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
                if(!workers[row][col].equals("") && workers[row][col].charAt(1)-'0' == id)
                    workers[row][col] = "";
            }
        }
        players.remove(id);
    }
}
