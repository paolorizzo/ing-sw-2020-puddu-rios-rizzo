package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Piece;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * It is the collection of building in the 3D
 */
public class Tower extends Group{
    private List<Building> buildings;
    private Worker worker;
    private Point3D base;
    private Point3D top;
    private int row, col;

    /**
     * It initializes a level 0, sets no worker on it and saves row, col and base point
     * @param row row on the board
     * @param col col on the board
     * @param base the base position in the 3D plane
     */
    public Tower(int row, int col, Point3D base){
        this.row = row;
        this.col = col;
        this.base = base;
        this.top = new Point3D(base.getX(), base.getY(), base.getZ());

        worker = null;
        buildings = new ArrayList<>();
        addBuilding(new Building(Piece.LEVEL0));
    }

    /**
     *
     * @return the base point of tower
     */
    public Point3D getBase() {
        return base;
    }

    /**
     *
     * @return the top point of tower
     */
    public Point3D getTop() {
        return top;
    }

    /**
     *
     * @return the column
     */
    public int getCol() {
        return col;
    }

    /**
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * It sets the default texture in all buildings and in an hypothetical worker
     */
    public void setToDefaultView(){
        for(Building building: buildings){
           building.setToDefaultView();
        }
        if(worker != null)
            worker.setToDefaultView();
    }
    /**
     * It sets the enable texture in all buildings and in an hypothetical worker
     */
    public void setToEnableView(){
        for(Building building: buildings){
            building.setToEnableView();
        }
        if(worker != null)
            worker.setToEnableView();
    }
    /**
     * It sets the preview texture in all buildings and in an hypothetical worker
     */
    public void setToPreview(){
        for(Building building: buildings){
            building.setToPreview();
        }
        if(worker != null)
            worker.setToPreview();
    }
    /**
     * It sets the disabled texture in all buildings and in an hypothetical worker
     */
    public void setToDisableView(){
        for(Building building: buildings){
            building.setToDisableView();
        }
        if(worker != null)
            worker.setToDisableView();
    }

    /**
     * It adds a building to the tower on the previews top point and next it recalculate the new top point
     * @param b the new building to add to the tower
     */
    public void addBuilding(final Building b){
        buildings.add(b);
        b.setPosition(top);
        //y up is negative, so pick the minY
        top = new Point3D(top.getX(), top.getY()+b.getLayoutBounds().getMinY(), top.getZ());
        if(this.hasWorker()){
            worker.setPosition(top);
        }
        Platform.runLater(
            new Runnable() {
                @Override
                public void run() {
                    getChildren().add(b);
                }
            }
        );
    }

    /**
     * It returns the top piece on the tower
     * @return the last piece added to the tower
     */
    public Building getLastBuilding() {
        return buildings.get(buildings.size()-1);
    }

    /**
     * It erases the top building in the tower and recalculates the new top point
     */
    public void removeLastBuilding(){
        final Building b = buildings.get(buildings.size()-1);
        top = new Point3D(top.getX(), top.getY()-b.getLayoutBounds().getMinY(), top.getZ());
        if(this.hasWorker()){
            worker.setPosition(top);
        }
        buildings.remove(b);
        Platform.runLater(
            new Runnable() {
                @Override
                public void run() {
                    getChildren().remove(b);
                }
            }
        );
    }

    /**
     * It sets a new worker on the top of the tower
     * @param w the worker to place on it
     */
    public void setWorker(final Worker w){

        worker = w;

        Platform.runLater(
            new Runnable() {
                @Override
                public void run() {
                    getChildren().add(w);
                    w.setVisible(true);
                    w.setPosition(top);
                }
            }
        );

    }

    /**
     * It returns true is the attribute worker is not null
     * @return true is the attribute worker is not null
     */
    public boolean hasWorker() {
        if(worker == null)
            return false;
        return true;
    }

    /**
     * It returns the worker on the tower. The value can be null.
     * @return the worker on it
     */
    public Worker getWorker(){
        return worker;
    }

    /**
     * It erases the worker on the tower
     */
    public void removeWorker(){
        worker.setVisible(false);
        final Worker w = worker;
        Platform.runLater(
            new Runnable() {
                @Override
                public void run() {
                    getChildren().remove(w);
                }
            }
        );
        worker = null;

    }

    /**
     * It sets a mouse event on the towers. It invokes the execute of action FSM with the string of Tower T<row><col>
     * @param actionFSM
     */
    public void setOnMouseClicked(final ActionFSM actionFSM){
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("T"+row+""+col);
            }
        });
    }
    /**
     * It sets a mouse event on the towers. It invokes the execute of action FSM with the string of Tower P<row><col>
     * @param actionFSM
     */
    public void setOnMouseEntered(final ActionFSM actionFSM){
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("P"+row+""+col);
            }
        });
    }
    /**
     * It sets a mouse event on the towers. It invokes the execute of action FSM with the string of Tower E<row><col>
     * @param actionFSM
     */
    public void setOnMouseExited(final ActionFSM actionFSM){
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("E"+row+""+col);
            }
        });
    }

}
