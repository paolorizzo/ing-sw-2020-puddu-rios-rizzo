package it.polimi.ingsw.view.gui;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;

/**
 * A single 3D worker of GUI
 */
public class Worker extends MeshView {
    private String workerId;
    private String defaultColor;
    /**
     * It loads the mesh and the default texture of the given color
     * @param id the id of the worker's player
     * @param color the color of the player
     */
    public Worker(String id, String color){
        this.workerId = id;
        this.defaultColor = color;
        this.setVisible(false);
        this.setMesh(GraphicsLoader.instance().getMesh("WORKER_"+workerId.charAt(workerId.length()-1)));

        setToDefaultView();
    }
    /**
     * It sets the position of the worker in the 3D plane
     * @param position the new position on the piece
     */
    public void setPosition(Point3D position){
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.setTranslateZ(position.getZ());
        this.setVisible(true);
    }
    /**
     * It sets the mesh of worker with the default texture of color
     */
    public void setToDefaultView() {
        this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_"+workerId.charAt(workerId.length()-1)+"_"+defaultColor));
    }
    /**
     * It sets the mesh of worker with the enabled texture
     */
    public void setToEnableView(){
        this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_"+workerId.charAt(workerId.length()-1)+"_ENABLED"));
    }
    /**
     * It sets the mesh of worker with the preview texture
     */
    public void setToPreview(){
        this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_"+workerId.charAt(workerId.length()-1)+"_PREVIEW"));
    }
    /**
     * It sets the mesh of worker with the disabled texture
     */
    public void setToDisableView(){
        this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_"+workerId.charAt(workerId.length()-1)+"_DISABLED"));
    }

    /**
     *
     * @return the worker's player id
     */
    public String getWorkerId(){
        return workerId;
    }
    /**
     *
     * @return the worker's color
     */
    public String getColor(){
        return defaultColor;
    }
}
