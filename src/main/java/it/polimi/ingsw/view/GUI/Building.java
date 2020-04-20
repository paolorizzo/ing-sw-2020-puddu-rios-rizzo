package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.Piece;
import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;

public class Building extends MeshView {
    Piece piece;
    public Building(Piece piece){
        this.piece = piece;
        this.setVisible(false);
        this.setMesh(GraphicsLoader.instance().getMesh(piece.toString()));
        setToDefaultView();
    }
    public void setPosition(Point3D position){
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.setTranslateZ(position.getZ());
        this.setVisible(true);
    }
    public void setToDefaultView(){
        this.setMaterial(GraphicsLoader.instance().getTexture(piece.toString()+"_default"));
    }
    public void setToEnableView(){
        this.setMaterial(GraphicsLoader.instance().getTexture(piece.toString()+"_enabled"));
    }
    public void setToPreview(){
        this.setMaterial(GraphicsLoader.instance().getTexture(piece.toString()+"_preview"));
    }
    public void setToDisableView(){
        this.setMaterial(GraphicsLoader.instance().getTexture(piece.toString()+"_disabled"));
    }

}
