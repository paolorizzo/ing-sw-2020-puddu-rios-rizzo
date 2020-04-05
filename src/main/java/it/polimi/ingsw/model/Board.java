package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Board {
    private Space[][] spaces;
    private PieceBag pieceBag;
    private Deck deck;
    private HashMap<String, Worker> workers;
    public Board(List<Player> players){
        spaces = generateSpaces();

        workers = new HashMap<>();
        for(Player p: players) {
            workers.put(p.getWorker(Sex.MALE).toString(), p.getWorker(Sex.MALE));
            workers.put(p.getWorker(Sex.FEMALE).toString(), p.getWorker(Sex.FEMALE));
        }
        deck = new Deck();
        pieceBag = new PieceBag();
    }
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

    public Space[][] getSpaces(){
        return spaces;
    }

    public Deck getDeck() { return deck;}
    public PieceBag getPieceBag() { return pieceBag;}

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

    public void executeAction(SetupAction action){
        Worker worker = workers.get(action.getWorkerID());
        //aggiungo worker
        spaces[action.getTargetX()][action.getTargetY()].setWorkerOnIt(worker);
        //setto space
        worker.setSpace(spaces[action.getTargetX()][action.getTargetY()]);
    }
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
    public void executeAction(BuildAction action){
        pieceBag.pickPiece(((BuildAction) action).getPiece());
        spaces[action.getTargetX()][action.getTargetY()].addPiece(((BuildAction) action).getPiece());
    }
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
    public void undoExecuteAction(SetupAction action){
        Worker worker = workers.get(action.getWorkerID());
        //aggiungo worker
        spaces[action.getTargetX()][action.getTargetY()].setWorkerOnIt(null);
        //setto space
        worker.setSpace(null);
    }
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
    public void undoExecuteAction(MoveAndForceAction action){
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
    public void undoExecuteAction(BuildAction action){
        pieceBag.undoPickPiece(((BuildAction) action).getPiece());
        spaces[action.getTargetX()][action.getTargetY()].removeLastPiece();
    }


}
