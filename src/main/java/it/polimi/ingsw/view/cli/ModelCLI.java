package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;

import java.util.*;

/**
 * Stores the data required for CLI representations.
 */
public class ModelCLI
{
    int[][] board;
    private int currentPlayerId;
    private boolean gameMode;
    private boolean spectator;
    private boolean myTurn;
    private boolean waiting;
    String [][] workers;
    private HashMap<Integer, Player> players;
    private HashMap<Integer, Integer> pieceBag;

    /**
     * Constructs the ModelCLI object.
     */
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
        waiting = true;
        pieceBag.put(1, 22);
        pieceBag.put(2, 18);
        pieceBag.put(3, 14);
        pieceBag.put(4, 18);
    }

    /**
     * Sets this player as spectator of the game.
     */
    void setAsSpectator()
    {
        if(players.keySet().size() == 3)
        {
            this.spectator = true;
        }
    }

    /**
     * Checks if the player is a spectator of the game or an active player.
     * @return the boolean flag being true if the player is a spectator.
     */
    boolean getSpectator()
    {

        return this.spectator;
    }

    /**
     * Checks if the game has started.
     * @return the boolean flag being true if the game has started.
     */
    boolean isGameOn()
    {

        return gameMode;
    }

    /**
     * Specifies that the game has started.
     */
    void setGameMode()
    {

        this.gameMode = true;
    }

    /**
     * Sets the specified player as currently active.
     * @param id the player's id.
     */
    void setCurrentPlayerId(int id)
    {
        if(players.keySet().contains(id))
        {
            this.currentPlayerId = id;
        }
    }

    /**
     * Checks if the model is expecting inputs.
     * @return the boolean flag being true if the model is not expecting input.
     */
    boolean isWaiting()
    {

        return waiting;
    }

    /**
     * Specifies that the model is not expecting inputs.
     */
    void notWaiting()
    {

        waiting = false;
    }

    /**
     * Gets the currently active player's id.
     * @return the id number.
     */
    int getCurrentPlayerId()
    {

        return currentPlayerId;
    }

    /**
     * Gets the number of pieces remaining for a specific building level.
     * @param level the building level.
     * @return the number of pieces left.
     */
    int getPiecesLeft(int level)
    {

        return pieceBag.get(level);
    }

    /**
     * Adds a player to the current model representation.
     * @param player the player object.
     */
    void addPlayer(Player player)
    {

        players.put(player.getId(), player);
    }

    /**
     * Gets the data about a specific player.
     * @param id the player's id.
     * @return the Player object.
     */
    Player getPlayer(int id)
    {

        return players.get(id);
    }

    /**
     * Gets the number of active players.
     * @return the number of active players.
     */
    int getNumPlayers()
    {

        return players.size();
    }

    /**
     * Gets the current board state in the form of a matrix.
     * @return the integer matrix, with every cell storing the building level.
     */
    public int[][] getBoard()
    {

        return board;
    }

    /**
     * Gets the current workers' positions in the form of a mask on the board.
     * @return a String matrix, with every cell storing the worker's id if present, nothing elsewhere.
     */
    public String[][] getWorkers()
    {

        return workers;
    }

    /**
     * Creates an iterator of the board matrix.
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
     * Creates an iterator for the collection of players.
     * @return the iterator.
     */
    Iterator<Player> getPlayersIterator()
    {
        Collection<Player> players_list = players.values();
        return players_list.iterator();
    }

    /**
     * Creates and initializes the representation of an empty 5x5 game board.
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
     * Creates and initializes the representation of an empty 5x5 workers mask.
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
     * Removes the workers of a chosen player from the workers' mask.
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
