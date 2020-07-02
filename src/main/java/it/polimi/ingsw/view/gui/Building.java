package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Piece;
import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;

/**
 * A single 3D piece of GUI
 */
public class Building extends MeshView {
    Piece piece;

    /**
     * It loads the right mesh of the given piece.
     * @param piece the piece that the new Building must be
     */
    public Building(Piece piece){
        this.piece = piece;
        this.setVisible(false);
        this.setMesh(GraphicsLoader.instance().getMesh(piece.toString()));
        setToDefaultView();
    }

    /**
     * It sets the position of the building in the 3D plane
     * @param position the new position on the piece
     */
    public void setPosition(Point3D position){
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.setTranslateZ(position.getZ());
        this.setVisible(true);
    }

    /**
     * It sets the building with the default texture
     */
    public void setToDefaultView(){
        this.setMaterial(GraphicsLoader.instance().getTexture(piece.toString()+"_default"));
    }

    /**
     * It sets the building with the enabled texture
     */
    public void setToEnableView(){
        this.setMaterial(GraphicsLoader.instance().getTexture(piece.toString()+"_enabled"));
    }
    /**
     * It sets the building with the preview texture
     */
    public void setToPreview(){
        this.setMaterial(GraphicsLoader.instance().getTexture(piece.toString()+"_preview"));
    }
    /**
     * It sets the building with the disabled texture
     */
    public void setToDisableView(){
        this.setMaterial(GraphicsLoader.instance().getTexture(piece.toString()+"_disabled"));
    }

    /**
     * It returns what kind of piece is the building
     * @return the type of building
     */
    public Piece getPiece(){
        return piece;
    }
}
