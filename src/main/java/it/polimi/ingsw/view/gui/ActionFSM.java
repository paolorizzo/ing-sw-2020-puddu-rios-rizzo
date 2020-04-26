package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.*;
import javafx.application.Platform;

import java.util.List;

public class ActionFSM{
    private enum ActionState {
        WAIT_INITIALIZE {
            public ActionState execute(Object input){
                System.out.println("STATE: WAIT_INIZIALIZE");
                if(board != null){
                    board.setAllToDefaultView();
                    board.hideSelectTypeActionMenu();
                    board.hideSelectPieceMenu();
                }
                resetPreview();
                worker_id = null;
                piece = null;
                return WAIT_INITIALIZE;
            }
        },
        WAIT_TARGET_SETUP_WORKER {
            public ActionState execute(Object input) {
                System.out.println("STATE: WAIT_SELECT_WORKER");


                board.setAllToDefaultView();
                board.hideSelectTypeActionMenu();
                board.hideSelectPieceMenu();
                resetPreview();
                worker_id = null;
                piece = null;

                if(input instanceof String){
                    switch(((String) input).charAt(0)){
                        case 'T':
                            if(previewOn){
                                resetPreview();
                            }
                            int x = ((String) input).charAt(1)-'0';
                            int y = ((String) input).charAt(2)-'0';
                            for (final Action action : possibleActions) {
                                if (action instanceof SetupAction && ((SetupAction)action).matches(x, y)) {
                                    board.clientView.updateReadAction(action);
                                    return WAIT_INITIALIZE.execute("");
                                }
                            }
                            break;
                        case 'P':
                            //preview
                            int previewTargetX = ((String) input).charAt(1) - '0';
                            int previewTargetY = ((String) input).charAt(2) - '0';

                            for (Action action : possibleActions) {
                                if (action instanceof SetupAction && ((SetupAction)action).matches(previewTargetX, previewTargetY)) {
                                    setPreview(action);
                                    System.out.println("PREVIEW "+action);
                                    break;
                                }
                            }
                            break;
                        case 'E':
                            resetPreview();
                            break;
                    }

                }

                return WAIT_TARGET_SETUP_WORKER;
            }
        },
        WAIT_SELECT_WORKER {
            public ActionState execute(Object input) {
                System.out.println("STATE: WAIT_SELECT_WORKER");


                board.setAllToDefaultView();
                board.hideSelectTypeActionMenu();
                board.hideSelectPieceMenu();
                resetPreview();
                worker_id = null;
                piece = null;

                if(input instanceof String){
                    if(((String) input).charAt(0) == 'T'){
                        int x = ((String) input).charAt(1)-'0';
                        int y = ((String) input).charAt(2)-'0';
                        if(board.getTower(x, y).hasWorker()){
                            worker_id = board.getTower(x, y).getWorker().getWorkerId();
                            boolean foundPossibleAction = false;
                            for(Action action: possibleActions){
                                if(action.matches(worker_id)){
                                    foundPossibleAction = true;
                                    break;
                                }
                            }
                            if(foundPossibleAction){
                                System.out.println("Selezionato "+worker_id);
                                board.showSelectTypeActionMenu();
                                return WAIT_SELECT_TYPE_ACTION;
                            }else{
                                System.out.println("Nessun azione disponibile per "+worker_id);
                                return WAIT_SELECT_WORKER;
                            }
                        }
                    }
                }

                return WAIT_SELECT_WORKER;
            }
        },
        WAIT_SELECT_TYPE_ACTION {
            public ActionState execute(Object input) {

                board.setAllToDefaultView();
                board.showSelectTypeActionMenu();
                board.hideSelectPieceMenu();
                resetPreview();
                piece = null;

                System.out.println("STATE: WAIT_SELECT_TYPE_ACTION worker selected: "+worker_id);
                if(input instanceof String){
                    switch((String)input) {
                        case "move":
                            System.out.println("move: rendo verdi le possibili mosse");
                            for(Action action: possibleActions){
                                if(action.matches(worker_id) && action instanceof MoveAction){
                                    board.getTower(action.getTargetX(), action.getTargetY()).setToEnableView();
                                }
                            }
                            return WAIT_SELECT_TARGET_MOVE;
                        case "build":
                            System.out.println("build: rendo visibile il menu");
                            board.showSelectPieceMenu();
                            return WAIT_SELECT_PIECE;
                        case "unselect":
                            return WAIT_SELECT_WORKER;

                    }
                }

                return WAIT_SELECT_TYPE_ACTION;
            }
        },
        WAIT_SELECT_TARGET_MOVE {
            public ActionState execute(Object input) {

                board.showSelectTypeActionMenu();
                board.hideSelectPieceMenu();
                piece = null;

                System.out.println("STATE: WAIT_SELECT_TARGET_MOVE worker selected: "+worker_id);

                board.showSelectTypeActionMenu();
                board.hideSelectPieceMenu();
                piece = null;

                if(input instanceof String){
                    switch((String)input) {
                        case "build":
                            return WAIT_SELECT_TYPE_ACTION.execute("build");
                        case "unselect":
                            return WAIT_SELECT_WORKER.execute("unselect");
                        default:
                            switch (((String) input).charAt(0)) {
                                case 'T':
                                    int targetX = ((String) input).charAt(1) - '0';
                                    int targetY = ((String) input).charAt(2) - '0';
                                    System.out.println("move to :" + targetX + " " + targetY);
                                    //boolean moveAndForce = board.getTower(targetX, targetY).hasWorker();
                                    for (Action action : possibleActions) {
                                        if (action.matches(worker_id, targetX, targetY) && action instanceof MoveAction) {
                                            System.out.println("Ecco la mia azione " + action);
                                            board.clientView.updateReadAction(action);
                                            return WAIT_INITIALIZE.execute("");
                                        }
                                    }

                                    break;
                                case 'P':
                                    //preview
                                    int previewTargetX = ((String) input).charAt(1) - '0';
                                    int previewTargetY = ((String) input).charAt(2) - '0';

                                    for (Action action : possibleActions) {
                                        if (action.matches(worker_id, previewTargetX, previewTargetY) && action instanceof MoveAction) {
                                            setPreview(action);
                                            System.out.println("PREVIEW "+action);
                                            break;
                                        }
                                    }
                                    break;
                                case 'E':
                                    resetPreview();
                                    break;
                                    //remove preview
                            }
                    }
                }

                return WAIT_SELECT_TARGET_MOVE;
            }
        },
        WAIT_SELECT_PIECE {
            public ActionState execute(Object input) {

                board.setAllToDefaultView();
                board.showSelectTypeActionMenu();
                board.showSelectPieceMenu();
                resetPreview();
                piece = null;

                System.out.println("STATE: WAIT_SELECT_PIECE worker selected: "+worker_id);
                if(input instanceof Piece){
                    piece = (Piece)input;
                    System.out.println("pezzo selezionato: "+piece);
                    for(Action action: possibleActions){
                        if(action.matches(worker_id, piece)){
                            board.getTower(action.getTargetX(), action.getTargetY()).setToEnableView();
                        }
                    }
                    return WAIT_SELECT_TARGET_BUILD;
                }else if(input instanceof String){
                    switch((String)input) {
                        case "move":
                            return WAIT_SELECT_TYPE_ACTION.execute("move");
                        case "unselect":
                            return WAIT_SELECT_WORKER.execute("unselect");
                    }
                }
                return WAIT_SELECT_PIECE;

            }
        },
        WAIT_SELECT_TARGET_BUILD {
            public ActionState execute(Object input) {
                System.out.println("STATE: WAIT_SELECT_TARGET_BUILD worker selected: "+worker_id+" piece selected:+"+piece);

                board.showSelectTypeActionMenu();
                board.showSelectPieceMenu();

                if(input instanceof String){
                    switch((String)input) {
                        case "move":
                            return WAIT_SELECT_TYPE_ACTION.execute("move");
                        case "unselect":
                            return WAIT_SELECT_WORKER.execute("unselect");
                        default:
                            switch (((String) input).charAt(0)){
                                case 'T':
                                    int targetX = ((String) input).charAt(1) - '0';
                                    int targetY = ((String) input).charAt(2) - '0';
                                    //boolean moveAndForce = board.getTower(targetX, targetY).hasWorker();
                                    for (Action action : possibleActions) {
                                        //TODO: in action multiple functions to match the action
                                        if (action.matches(worker_id, targetX, targetY, piece)) {
                                            System.out.println("Ecco la mia azione " + action);
                                            board.clientView.updateReadAction(action);
                                            worker_id = null;
                                            board.setAllToDefaultView();
                                            board.hideSelectTypeActionMenu();
                                            board.hideSelectPieceMenu();
                                            return WAIT_INITIALIZE.execute("");
                                        }
                                    }
                                    break;
                                case 'P':
                                    //preview
                                    int previewTargetX = ((String) input).charAt(1) - '0';
                                    int previewTargetY = ((String) input).charAt(2) - '0';
                                    for (Action action : possibleActions) {
                                        //TODO: in action multiple functions to match the action
                                        if (action.matches(worker_id, previewTargetX, previewTargetY, piece)) {
                                            setPreview(action);
                                            break;
                                        }
                                    }
                                    break;
                                case 'E':
                                    resetPreview();
                            }
                    }
                }else if(input instanceof Piece){
                    piece = null;
                    board.setAllToDefaultView();
                    return WAIT_SELECT_PIECE.execute((Piece)input);
                }
                return WAIT_SELECT_TARGET_BUILD;

            }
        };

        static String worker_id;
        static boolean previewOn;
        static Action previewAction;
        static Piece piece;
        static List<Action> possibleActions;
        static Board board;
        public ActionState execute(Object input) {
            return this.execute(input);
        }
        public ActionState init(Board b, List<Action> actions){
            possibleActions = actions;
            board = b;
            if(actions.size()>0 && actions.get(0) instanceof SetupAction)
                return ActionState.WAIT_TARGET_SETUP_WORKER;
            else
                return ActionState.WAIT_SELECT_WORKER;
        }
        public void setPreview(Action action){
            if(previewOn)
                resetPreview();
            previewAction = action;
            board.previewAction(previewAction);
            previewOn =  true;
        }
        public void resetPreview(){
            if(!previewOn)
                return;

            board.undoPreviewAction(previewAction);
            previewOn = false;
            previewAction = null;
        }
    }

    ActionState state;

    public ActionFSM(){
        state = ActionState.WAIT_INITIALIZE;
    }

    public void execute(Object input){
        state = state.execute(input);
    }

    public void setPossibleActions(Board board, List<Action> actions){
        state = state.init(board, actions);
    }
}
