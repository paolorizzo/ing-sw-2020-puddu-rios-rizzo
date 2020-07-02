package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Represents the actual game board of the game.
 */
public class Board {
    private Space[][] spaces;
    private PieceBag pieceBag;
    private HashMap<String, Worker> workers;

    /**
     * Constructs an empty board
     */
    public Board(){
        spaces = generateSpaces();
        workers = new HashMap<>();
        pieceBag = new PieceBag();
    }

    /**
     * creates the workers for a given player
     * @param p the player for whom to create the workers
     */
    public void createPlayerWorkers(Player p){
        workers.put(p.getWorker(Sex.MALE).toString(), p.getWorker(Sex.MALE));
        workers.put(p.getWorker(Sex.FEMALE).toString(), p.getWorker(Sex.FEMALE));
    }

    /**
     * populates the field spaces with a correctly build collection
     * of Space object, representing an empty board
     * @return a collection of Space objects which represents an empty board
     */
    private Space[][] generateSpaces(){
        Space[][] spaces = new Space[5][5];
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                spaces[i][j] = new Space(i, j);
            }
        }

        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                if(i<4 && j<4){
                    spaces[i][j].addAdjacentSpace(spaces[i+1][j+1]);
                    spaces[i+1][j+1].addAdjacentSpace(spaces[i][j]);
                }
                if(j<4){
                    spaces[i][j].addAdjacentSpace(spaces[i][j+1]);
                    spaces[i][j+1].addAdjacentSpace(spaces[i][j]);
                }
                if(i<4){
                    spaces[i][j].addAdjacentSpace(spaces[i+1][j]);
                    spaces[i+1][j].addAdjacentSpace(spaces[i][j]);
                }
                if(i<4 && j>0){
                    spaces[i][j].addAdjacentSpace(spaces[i+1][j-1]);
                    spaces[i+1][j-1].addAdjacentSpace(spaces[i][j]);
                }
            }
        }
        return spaces;
    }

    /**
     * returns the collection of Space objects that make up the board
     * @return the collection of Space objects that make up the board
     */
    public Space[][] getSpaces(){
        return spaces;
    }

    /**
     * returns the piece bag, which contains info about how many pieces remain
     * for each type
     * @return the piece bag
     */
    public PieceBag getPieceBag() { return pieceBag;}

    /**
     * reads an action and calculates the next state of the board
     * based on it
     * @param action the given action taken by some player
     */
    public void executeAction(Action action){
        if(action instanceof SetupAction)
            this.executeAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.executeAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.executeAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.executeAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't execute a normal action!");
    }

    /**
     * reads a setup action and calculates the next state of the board
     * based on it
     * @param action the given setup action taken by some player
     */
    public void executeAction(SetupAction action){
        Worker worker = workers.get(action.getWorkerID());
        //aggiungo worker
        spaces[action.getTargetX()][action.getTargetY()].setWorkerOnIt(worker);
        //setto space
        worker.setSpace(spaces[action.getTargetX()][action.getTargetY()]);
    }

    /**
     * reads a move action and calculates the next state of the board
     * based on it
     * @param action the given move action taken by some player
     */
    public void executeAction(MoveAction action){
        //salvo worker
        Worker worker = spaces[((MoveAction) action).getStartX()][((MoveAction) action).getStartY()].getWorkerOnIt();
        //tolgo worker
        spaces[((MoveAction) action).getStartX()][((MoveAction) action).getStartY()].removeWorkerOnIt();
        //aggiungo worker
        spaces[action.getTargetX()][action.getTargetY()].setWorkerOnIt(worker);
        //setto space
        worker.setSpace(spaces[action.getTargetX()][action.getTargetY()]);
    }

    /**
     * reads a move and force action and calculates the next state of the board
     * based on it
     * @param action the given move and force action taken by some player
     */
    public void executeAction(MoveAndForceAction action){
        //salvo workers
        Worker worker = spaces[((MoveAction)action).getStartX()][((MoveAction)action).getStartY()].getWorkerOnIt();
        Worker forcedWorker = spaces[((MoveAndForceAction) action).getForcedStartX()][((MoveAndForceAction) action).getForcedStartY()].getWorkerOnIt();
        //tolgo workers
        spaces[((MoveAction)action).getStartX()][((MoveAction)action).getStartY()].removeWorkerOnIt();
        spaces[((MoveAndForceAction) action).getForcedStartX()][((MoveAndForceAction) action).getForcedStartY()].removeWorkerOnIt();
        //aggiungo workers
        spaces[action.getTargetX()][action.getTargetY()].setWorkerOnIt(worker);
        spaces[((MoveAndForceAction) action).getForcedTargetX()][((MoveAndForceAction) action).getForcedTargetY()].setWorkerOnIt(forcedWorker);
        //setto spaces
        worker.setSpace(spaces[action.getTargetX()][action.getTargetY()]);
        forcedWorker.setSpace(spaces[((MoveAndForceAction) action).getForcedTargetX()][((MoveAndForceAction) action).getForcedTargetY()]);
    }

    /**
     * reads a build action and calculates the next state of the board
     * based on it
     * @param action the given build action taken by some player
     */
    public void executeAction(BuildAction action){
        pieceBag.pickPiece(((BuildAction) action).getPiece());
        spaces[action.getTargetX()][action.getTargetY()].addPiece(((BuildAction) action).getPiece());
    }

    /**
     * reads an action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given action
     */
    public void undoExecuteAction(Action action){
        if(action instanceof SetupAction)
            this.undoExecuteAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.undoExecuteAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.undoExecuteAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.undoExecuteAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't undo a normal action!");
    }

    /**
     * reads a setup action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given setup action
     */
    public void undoExecuteAction(SetupAction action){
        Worker worker = workers.get(action.getWorkerID());
        //aggiungo worker
        spaces[action.getTargetX()][action.getTargetY()].setWorkerOnIt(null);
        //setto space
        worker.setSpace(null);
    }

    /**
     * reads a move action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given move action
     */
    public void undoExecuteAction(MoveAction action){
        //salvo worker
        Worker worker = spaces[action.getTargetX()][action.getTargetY()].getWorkerOnIt();
        //tolgo worker
        spaces[action.getTargetX()][action.getTargetY()].removeWorkerOnIt();
        //aggiungo worker
        spaces[((MoveAction)action).getStartX()][((MoveAction)action).getStartY()].setWorkerOnIt(worker);
        //setto space
        worker.setSpace(spaces[((MoveAction)action).getStartX()][((MoveAction)action).getStartY()]);
    }

    /**
     * reads a move and force action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given move and force action
     */
    public void undoExecuteAction(MoveAndForceAction action){
        //salvo workers
        Worker worker = spaces[((MoveAction)action).getTargetX()][((MoveAction)action).getTargetY()].getWorkerOnIt();
        Worker forcedWorker = spaces[((MoveAndForceAction) action).getForcedTargetX()][((MoveAndForceAction) action).getForcedTargetY()].getWorkerOnIt();
        //tolgo workers
        spaces[((MoveAction)action).getTargetX()][((MoveAction)action).getTargetY()].removeWorkerOnIt();
        spaces[((MoveAndForceAction) action).getForcedTargetX()][((MoveAndForceAction) action).getForcedTargetY()].removeWorkerOnIt();
        //aggiungo workers
        spaces[action.getStartX()][action.getStartY()].setWorkerOnIt(worker);
        spaces[((MoveAndForceAction) action).getForcedStartX()][((MoveAndForceAction) action).getForcedStartY()].setWorkerOnIt(forcedWorker);
        //setto spaces
        worker.setSpace(spaces[action.getStartX()][action.getStartY()]);
        forcedWorker.setSpace(spaces[((MoveAndForceAction) action).getForcedStartX()][((MoveAndForceAction) action).getForcedStartY()]);
    }

    /**
     * reads a build action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given build action
     */
    public void undoExecuteAction(BuildAction action){
        pieceBag.undoPickPiece(((BuildAction) action).getPiece());
        spaces[action.getTargetX()][action.getTargetY()].removeLastPiece();
    }

    /**
     * removes the workers of a given player from the board
     * @param p the given player
     */
    public void removeWorkersPlayer(Player p){
        Worker m = p.getWorker(Sex.MALE);
        Worker f = p.getWorker(Sex.FEMALE);

        m.getSpace().removeWorkerOnIt();
        m.setSpace(null);

        f.getSpace().removeWorkerOnIt();
        f.setSpace(null);
    }

    /**
     * returns a string representing the positions of the workers
     * @return a string representing the positions of the workers
     */
    public String workersRep(){
        String s = "";
        int side = 5;
        int cell = 4;
        int width = side*cell + side + 1;
        int height = side*cell + side + 1;
        for(Space[] row:spaces){
            for(int i=0;i<width;i++)
                s += "*";
            s += "\n";
            for(Space space:row){
                s += "*";
                s += space.toString();
            }
            s += "*\n";
        }
        for(int i=0;i<width;i++)
            s += "*";
        s += "\n";
        return s;
    }

    /**
     * returns a string representing the height of the buildings for every space
     * @return a string representing the height of the buildings for every space
     */
    public String buildingsRep(){
        String s = "";
        int side = 5;
        int cell = 4;
        int width = side*cell + side + 1;
        int height = side*cell + side + 1;
        for(Space[] row:spaces){
            for(int i=0;i<width;i++)
                s += "*";
            s += "\n";
            for(Space space:row){
                s += "*";
                s += space.getLastPiece().ordinal();
                s += "   ";
            }
            s += "*\n";
        }
        for(int i=0;i<width;i++)
            s += "*";
        s += "\n";
        return s;
    }

    /**
     * compares this board with another one, by propagating
     * the comparison through the collections that make up Board
     * @param that the other board
     * @return true if the two boards and their internal objects
     * are identical
     */
    public boolean fullEquals(Board that){
        boolean equality = true;
        equality &= this.pieceBag.equals(that.pieceBag);
        for(int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                equality &= this.spaces[i][j].equals(that.spaces[i][j]);
            }
        }
        equality &= this.workers.equals(that.workers);
        return equality;
    }
}
