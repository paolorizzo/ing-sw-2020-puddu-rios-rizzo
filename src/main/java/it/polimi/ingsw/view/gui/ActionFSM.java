package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observation.UserInterfaceObserver;
import javafx.application.Platform;

import java.util.List;

/**
 * The FSM for the interact of user to select the action on board
 */
public class ActionFSM{
    private enum ActionState{
        WAIT_INITIALIZE {
            /**
             * It initializes all menus to hide and reset the variables to null
             * @param input a object
             * @return the new state of fsm
             */
            public ActionState execute(Object input){
                if(board != null){
                    board.setAllToDefaultView();
                    selectTypeActionMenu.hide();
                    selectPieceMenu.hide();
                    endOfTurnMenu.hide();
                }

                resetPreview();
                worker_id = null;
                piece = null;
                return WAIT_INITIALIZE;
            }
        },
        WAIT_TARGET_SETUP_WORKER {
            /**
             * It handles the different views of board when there is a worker's setup, showing the preview of move and
             * notifying the select.
             * @param input a object, if it is String T<x><y> for select, P<x><y> for preview, E<x><y> for end the preview
             * @return the new state of fsm
             */
            public ActionState execute(Object input) {
                board.setAllToDefaultView();
                selectTypeActionMenu.hide();
                selectPieceMenu.hide();
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
                                    resetPreview();
                                    board.notifyReadAction(action);
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
            /**
             * It handles the select of worker
             * @param input a object, if it is String T<x><y> is a select of a space where can be a worker of player
             * @return the new state of fsm
             */
            public ActionState execute(Object input) {
                board.setAllToDefaultView();
                selectTypeActionMenu.hide();
                selectPieceMenu.hide();
                resetPreview();
                worker_id = null;
                piece = null;

                if(canEndOfTurn){
                    endOfTurnMenu.show();
                }else{
                    endOfTurnMenu.hide();
                }

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
                                selectTypeActionMenu.show(possibleActions, worker_id);
                                return WAIT_SELECT_TYPE_ACTION;
                            }else{
                                return WAIT_SELECT_WORKER;
                            }
                        }
                    }
                }

                return WAIT_SELECT_WORKER;
            }
        },
        WAIT_SELECT_TYPE_ACTION {
            /**
             * It handles the different action that can be the player after the select of worker
             * @param input a object, if it is String can be "move", "build", "unselected"
             * @return the new state of fsm
             */
            public ActionState execute(Object input) {

                board.setAllToDefaultView();
                selectTypeActionMenu.show(possibleActions, worker_id);
                selectPieceMenu.hide();
                resetPreview();
                piece = null;

                if(input instanceof String){
                    switch((String)input) {
                        case "move":
                            for(Action action: possibleActions){
                                if(action.matches(worker_id) && action instanceof MoveAction){
                                    board.getTower(action.getTargetX(), action.getTargetY()).setToEnableView();
                                }
                            }
                            return WAIT_SELECT_TARGET_MOVE;
                        case "build":
                            selectPieceMenu.show(possibleActions, worker_id);
                        return WAIT_SELECT_PIECE;
                        case "unselect":
                            return WAIT_SELECT_WORKER;

                    }
                }

                return WAIT_SELECT_TYPE_ACTION;
            }
        },
        WAIT_SELECT_TARGET_MOVE {
            /**
             * It handles the target of a move with the preview, and notifying the choose
             * @param input a object, if it is String T<x><y> for select, P<x><y> for preview, E<x><y> for end the preview
             * @return the new state of fsm
             */
            public ActionState execute(Object input) {

                selectTypeActionMenu.show(possibleActions, worker_id);
                selectPieceMenu.hide();
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
                                            resetPreview();
                                            board.notifyReadAction(action);
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
            /**
             * It handles the select of piece to build
             * @param input a object, if it is a Piece the piece selected
             * @return the new state of fsm
             */
            public ActionState execute(Object input) {

                board.setAllToDefaultView();
                selectTypeActionMenu.show(possibleActions, worker_id);
                selectPieceMenu.show(possibleActions, worker_id);
                piece = null;

                if(input instanceof Piece){
                    piece = (Piece)input;
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
            /**
             * It handles the target of a build with the preview, and notifying the choose
             * @param input a object, if it is String T<x><y> for select, P<x><y> for preview, E<x><y> for end the preview
             * @return the new state of fsm
             */
            public ActionState execute(Object input) {

                selectTypeActionMenu.show(possibleActions, worker_id);
                selectPieceMenu.show(possibleActions, worker_id);

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

                                    for (Action action : possibleActions) {
                                        if (action.matches(worker_id, targetX, targetY, piece)) {
                                            System.out.println("Ecco la mia azione " + action);
                                            resetPreview();
                                            board.notifyReadAction(action);
                                            return WAIT_INITIALIZE.execute("");
                                        }
                                    }
                                    break;
                                case 'P':
                                    //preview
                                    int previewTargetX = ((String) input).charAt(1) - '0';
                                    int previewTargetY = ((String) input).charAt(2) - '0';
                                    for (Action action : possibleActions) {
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

        static List<Action> possibleActions;
        static Board board;
        static SelectTypeActionMenu selectTypeActionMenu;
        static SelectPieceMenu selectPieceMenu;
        static EndOfTurnMenu endOfTurnMenu;
        static boolean canEndOfTurn;

        static String worker_id;
        static boolean previewOn;
        static Action previewAction;
        static Piece piece;

        /**
         * Default implementation of the method for the execution of a state
         * @param input the input for the execution of the state
         * @return the new state of fsm
         */
        public ActionState execute(Object input) {
            return this.execute(input);
        }

        /**
         * It sets the variables and the initial state of fsm
         * @param b actually board of the game
         * @param actions the new possible actions
         * @param flagEndOfTurn true if is possible end the turn early
         * @return
         */
        public ActionState init(Board b, List<Action> actions, boolean flagEndOfTurn){
            possibleActions = actions;
            canEndOfTurn = flagEndOfTurn;
            board = b;
            if(actions.size()>0 && actions.get(0) instanceof SetupAction)
                return ActionState.WAIT_TARGET_SETUP_WORKER;
            else
                return ActionState.WAIT_SELECT_WORKER;
        }

        /**
         * It sets the preview on board of the action and save the action to future restore of default board.
         * @param action the action you want to preview
         */
        public void setPreview(Action action){
            if(previewOn)
                resetPreview();
            previewAction = action;
            board.previewAction(previewAction);
            previewOn =  true;
        }

        /**
         * It resets the board to normal state undoing the preview action saved before
         */
        public void resetPreview(){
            if(!previewOn)
                return;

            board.undoPreviewAction(previewAction);
            previewOn = false;
            previewAction = null;
        }

        /**
         * It notifies the voluntary end of turn
         * @return the new state of fsm
         */
        public ActionState voluntaryEndOfTurn(){
            board.notifyReadVoluntaryEndOfTurn();
            return ActionState.WAIT_INITIALIZE.execute(null);
        }
        public void setMenus(SelectTypeActionMenu sta, SelectPieceMenu spm, EndOfTurnMenu eot){
            selectTypeActionMenu = sta;
            selectPieceMenu = spm;
            endOfTurnMenu = eot;
        }

    }

    ActionState state;

    /**
     * It initializes the state to WAIT_INITIALIZE
     */
    public ActionFSM(){
        state = ActionState.WAIT_INITIALIZE;
    }

    /**
     * It sets the menus used in fsm to interact to user
     * @param sta the menu to select the type of action
     * @param spm the menu to select the piece for build
     * @param eot the menu to select a voluntary end of turn
     */
    public void setMenus(SelectTypeActionMenu sta, SelectPieceMenu spm, EndOfTurnMenu eot){
        state.setMenus(sta, spm, eot);
    }

    /**
     * Interface of the method for the execution of a state
     * @param input the input for the execution of the state
     */
    public void execute(Object input){
        state = state.execute(input);
    }

    /**
     * Interface of the method init for the ActionState
     * @param board actually board of the game
     * @param actions the new possible actions
     * @param canEndOfTurn true if is possible end the turn early
     */
    public void setPossibleActions(Board board, List<Action> actions, boolean canEndOfTurn){
        state = state.init(board, actions, canEndOfTurn);
    }
    /**
     * Interface of the method voluntaryEndOfTurn for ActionState
     */
    public void voluntaryEndOfTurn(){
        state = state.voluntaryEndOfTurn();
    }
}
