package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

import java.util.HashMap;
import java.util.List;

public class Board extends Group {
    private Tower [][] towers = new Tower[5][5];
    private HashMap<Integer, Player> players;
    ActionFSM actionFSM;
    SelectTypeActionMenu selectTypeActionMenu;
    SelectPieceMenu selectPieceMenu;
    Group group2d, group3d;
    PieceBag pieceBag;
    public Board() {
        players = new HashMap<>();

        group2d = new Group();
        group3d = new Group3D();

        this.getChildren().addAll(group2d, group3d);

        prepareIsland(group3d);

        actionFSM = new ActionFSM();
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                towers[i][j] = new Tower(i, j, new Point3D(i*100-200, 0, j*100-200));
                group3d.getChildren().add(towers[i][j]);
                towers[i][j].setOnMouseClicked(actionFSM);
                towers[i][j].setOnMouseEntered(actionFSM);
                towers[i][j].setOnMouseExited(actionFSM);
            }
        }

        selectTypeActionMenu = new SelectTypeActionMenu(actionFSM);
        group2d.getChildren().add(selectTypeActionMenu);

        selectPieceMenu = new SelectPieceMenu(actionFSM);
        group2d.getChildren().add(selectPieceMenu);

        pieceBag = new PieceBag();
    }

    public void addPlayer(Player p){
        players.put(p.getId(), p);
    }

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
        Color color = players.get(action.getWorkerID().charAt(1)-'0').getColor();
        Worker worker = new Worker(action.getWorkerID(), color);
        //aggiungo worker
        towers[action.getTargetX()][action.getTargetY()].setWorker(worker);
    }
    public void executeAction(MoveAction action){
        //salvo worker
        Worker worker = towers[action.getStartX()][action.getStartY()].getWorker();
        //tolgo worker
        towers[action.getStartX()][action.getStartY()].removeWorker();
        //aggiungo worker
        towers[action.getTargetX()][action.getTargetY()].setWorker(worker);
    }
    public void executeAction(MoveAndForceAction action){
        //salvo workers
        Worker worker = towers[action.getStartX()][action.getStartY()].getWorker();
        Worker forcedWorker = towers[action.getForcedStartX()][action.getForcedStartY()].getWorker();
        //tolgo workers
        towers[action.getStartX()][action.getStartY()].removeWorker();
        towers[action.getForcedStartX()][((MoveAndForceAction) action).getForcedStartY()].removeWorker();
        //aggiungo workers
        towers[action.getTargetX()][action.getTargetY()].setWorker(worker);
        towers[action.getForcedTargetX()][action.getForcedTargetY()].setWorker(forcedWorker);
    }
    public void executeAction(BuildAction action){
        pieceBag.pickPiece(action.getPiece());
        towers[action.getTargetX()][action.getTargetY()].addBuilding(new Building(action.getPiece()));
    }

    public void previewAction(Action action){
        if(action instanceof SetupAction)
            this.previewAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.previewAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.previewAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.previewAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't execute a normal action!");
    }

    public void previewAction(SetupAction action){
        executeAction(action);
        towers[action.getTargetX()][action.getTargetY()].getWorker().setToPreview();
    }
    public void previewAction(MoveAction action){
        executeAction(action);
        towers[action.getTargetX()][action.getTargetY()].getWorker().setToPreview();
    }
    public void previewAction(MoveAndForceAction action){
        executeAction(action);
        towers[action.getTargetX()][action.getTargetY()].getWorker().setToPreview();
        towers[action.getForcedTargetX()][action.getForcedTargetY()].getWorker().setToDefaultView();
    }
    public void previewAction(BuildAction action){
        executeAction(action);
        towers[action.getTargetX()][action.getTargetY()].getLastBuilding().setToPreview();
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
        towers[action.getTargetX()][action.getTargetY()].removeWorker();
    }
    public void undoExecuteAction(MoveAction action){
        //salvo worker
        Worker w = towers[action.getTargetX()][action.getTargetY()].getWorker();
        //tolgo worker
        towers[action.getTargetX()][action.getTargetY()].removeWorker();
        //aggiungo worker
        towers[action.getStartX()][action.getStartY()].setWorker(w);
    }
    public void undoExecuteAction(MoveAndForceAction action){
        //salvo workers
        Worker worker = towers[action.getTargetX()][action.getTargetY()].getWorker();
        Worker forcedWorker = towers[action.getForcedTargetX()][action.getForcedTargetY()].getWorker();
        //tolgo workers
        towers[action.getTargetX()][action.getTargetY()].removeWorker();
        towers[action.getForcedTargetX()][action.getForcedTargetY()].removeWorker();
        //aggiungo workers
        towers[action.getStartX()][action.getStartY()].setWorker(worker);
        towers[action.getForcedStartX()][action.getForcedStartY()].setWorker(forcedWorker);
    }
    public void undoExecuteAction(BuildAction action){
        pieceBag.undoPickPiece(action.getPiece());
        towers[action.getTargetX()][action.getTargetY()].removeLastBuilding();
    }

    public void undoPreviewAction(Action action){
        if(action instanceof SetupAction)
            this.undoPreviewAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.undoPreviewAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.undoPreviewAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.undoPreviewAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't undo a normal action!");
    }
    public void undoPreviewAction(SetupAction action){
        undoExecuteAction(action);
    }
    public void undoPreviewAction(MoveAction action){
        undoExecuteAction(action);
        towers[action.getStartX()][action.getStartY()].setToDefaultView();
    }
    public void undoPreviewAction(MoveAndForceAction action){
        undoExecuteAction(action);
        towers[action.getStartX()][action.getStartY()].setToDefaultView();
        towers[action.getForcedStartX()][action.getForcedStartY()].setToEnableView();
    }
    public void undoPreviewAction(BuildAction action){
        undoExecuteAction(action);
    }


    public void showSelectTypeActionMenu() {
        selectTypeActionMenu.show();
    }
    public void hideSelectTypeActionMenu() {
        selectTypeActionMenu.hide();
    }
    public void showSelectPieceMenu() {
        selectPieceMenu.show();
    }
    public void hideSelectPieceMenu() {
        selectPieceMenu.hide();
    }

    public Tower getTower(int targetX, int targetY) {
        return towers[targetX][targetY];
    }

    public void setAllToDefaultView(){
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                towers[i][j].setToDefaultView();
            }
        }
    }
    public void setPossibleActions(List<Action> actions){
        actionFSM.setPossibleActions(this, actions);
    }

    private void prepareIsland(Group group3D) {
        MeshView island = new MeshView(GraphicsLoader.instance().getMesh("ISLAND"));
        PhongMaterial islandTexture = GraphicsLoader.instance().getTexture("ISLAND");
        island.setMaterial(islandTexture);
        group3d.getChildren().add(island);
    }
}
