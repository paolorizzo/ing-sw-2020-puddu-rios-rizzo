package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Piece;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class Tower extends Group{
    List<Building> buildings;
    Worker worker;
    Point3D base;
    Point3D top;
    int row, col;

    public Tower(int row, int col, Point3D base){
        this.row = row;
        this.col = col;
        this.base = base;
        this.top = new Point3D(base.getX(), base.getY(), base.getZ());

        worker = null;
        buildings = new ArrayList<>();
        addBuilding(new Building(Piece.LEVEL0));
    }

    public Point3D getBase() {
        return base;
    }

    public Point3D getTop() {
        return top;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setToDefaultView(){
        for(Building building: buildings){
           building.setToDefaultView();
        }
        if(worker != null)
            worker.setToDefaultView();
    }
    public void setToEnableView(){
        for(Building building: buildings){
            building.setToEnableView();
        }
        if(worker != null)
            worker.setToEnableView();
    }
    public void setToPreview(){
        for(Building building: buildings){
            building.setToPreview();
        }
        if(worker != null)
            worker.setToPreview();
    }
    public void setToDisableView(){
        for(Building building: buildings){
            building.setToDisableView();
        }
        if(worker != null)
            worker.setToDisableView();
    }
    public void addBuilding(Building b){
        buildings.add(b);
        b.setPosition(top);
        //y up is negative, so pick the minY
        top = new Point3D(top.getX(), top.getY()+b.getLayoutBounds().getMinY(), top.getZ());
        if(this.hasWorker()){
            worker.setPosition(top);
        }
        this.getChildren().add(b);
    }

    public Building getLastBuilding() {
        return buildings.get(buildings.size()-1);
    }

    public void removeLastBuilding(){
        Building b = buildings.get(buildings.size()-1);
        top = new Point3D(top.getX(), top.getY()-b.getLayoutBounds().getMinY(), top.getZ());
        if(this.hasWorker()){
            worker.setPosition(top);
        }
        buildings.remove(b);
        this.getChildren().remove(b);
    }
    public void setWorker(Worker w){
        worker = w;
        this.getChildren().add(w);
        worker.setPosition(top);
        System.out.println("set on "+row+" "+col);
    }
    public boolean hasWorker() {
        if(worker == null)
            return false;
        return true;
    }
    public Worker getWorker(){
        return worker;
    }
    public void removeWorker(){
        this.getChildren().remove(worker);
        worker = null;
    }
    public void setOnMouseClicked(final ActionFSM actionFSM){
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("T"+row+""+col);
            }
        });
    }
    public void setOnMouseEntered(final ActionFSM actionFSM){
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("P"+row+""+col);
            }
        });
    }
    public void setOnMouseExited(final ActionFSM actionFSM){
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("E"+row+""+col);
            }
        });
    }

}
