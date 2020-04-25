package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Piece;
import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import org.junit.Test;

import java.io.File;


public class TowerTest{

    @Test
    public void TowerConstructorTest() {
        Tower t = new Tower(2, 2, new Point3D(0, 0, 0));
        assert (!t.hasWorker());
        assert (t.getWorker() == null);


        assert (t.getLastBuilding().getPiece() == Piece.LEVEL0);
        assert (t.getRow() == 2);
        assert (t.getCol() == 2);
        assert (t.getTop().equals(new Point3D(0, 0 + t.getLastBuilding().getLayoutBounds().getMinY(), 0)));
        assert (t.getBase().equals(new Point3D(0, 0, 0)));
    }
    @Test
    public void TowerBuildingAndWorkerMethodTest() {
        Tower t = new Tower(2, 2, new Point3D(0, 0, 0));
        assert (!t.hasWorker());
        assert (t.getWorker() == null);

        Worker w = new Worker("P1-M", "ORANGE");
        t.setWorker(w);
        assert (t.hasWorker());
        assert (t.getWorker() == w);

        Building [] b = new Building[4];

        
        b[0] = new Building(Piece.LEVEL1);
        t.addBuilding(b[0]);
        assert (t.getLastBuilding() == b[0]);

        b[1] = new Building(Piece.LEVEL2);
        t.addBuilding(b[1]);
        assert (t.getLastBuilding() == b[1]);

        b[2] = new Building(Piece.LEVEL3);
        t.addBuilding(b[2]);
        assert (t.getLastBuilding() == b[2]);

        b[3] = new Building(Piece.DOME);
        t.addBuilding(b[3]);
        assert (t.getLastBuilding() == b[3]);

        t.setToEnableView();

        for(Building building: b){
            assert(building.getMaterial() == GraphicsLoader.instance().getTexture(building.getPiece().toString()+"_enabled"));
        }
        assert(w.getMaterial() == GraphicsLoader.instance().getTexture("WORKER_"+w.getWorkerId().charAt(w.getWorkerId().length()-1)+"_ENABLED"));

        t.setToPreview();
        for(Building building: b){
            assert(building.getMaterial() == GraphicsLoader.instance().getTexture(building.getPiece().toString()+"_preview"));
        }
        assert(w.getMaterial() == GraphicsLoader.instance().getTexture("WORKER_"+w.getWorkerId().charAt(w.getWorkerId().length()-1)+"_PREVIEW"));

        t.setToDisableView();

        for(Building building: b){
            assert(building.getMaterial() == GraphicsLoader.instance().getTexture(building.getPiece().toString()+"_disabled"));
        }
        assert(w.getMaterial() == GraphicsLoader.instance().getTexture("WORKER_"+w.getWorkerId().charAt(w.getWorkerId().length()-1)+"_DISABLED"));

        t.setToDefaultView();

        for(Building building: b){
            assert(building.getMaterial() == GraphicsLoader.instance().getTexture(building.getPiece().toString()+"_default"));
        }
        assert(w.getMaterial() == GraphicsLoader.instance().getTexture("WORKER_"+w.getWorkerId().charAt(w.getWorkerId().length()-1)+"_"+w.getColor()));

        t.removeLastBuilding();
        assert (t.getLastBuilding() == b[2]);

        t.removeWorker();
        assert (!t.hasWorker());
        assert (t.getWorker() == null);
    }
}
