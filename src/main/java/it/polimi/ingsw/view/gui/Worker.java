package it.polimi.ingsw.view.gui;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;


public class Worker extends MeshView {
    private String workerId;
    private String defaultColor;
    public Worker(String id, String color){
        this.workerId = id;
        this.defaultColor = color;
        this.setVisible(false);
        this.setMesh(GraphicsLoader.instance().getMesh("WORKER_"+workerId.charAt(workerId.length()-1)));

        setToDefaultView();
    }
    public void setPosition(Point3D position){
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.setTranslateZ(position.getZ());
        this.setVisible(true);
    }
    public void setToDefaultView() {
        this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_"+workerId.charAt(workerId.length()-1)+"_"+defaultColor));
    }
    public void setToEnableView(){
        this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_"+workerId.charAt(workerId.length()-1)+"_ENABLED"));
    }
    public void setToPreview(){
        this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_"+workerId.charAt(workerId.length()-1)+"_PREVIEW"));
    }
    public void setToDisableView(){
        this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_"+workerId.charAt(workerId.length()-1)+"_DISABLED"));
    }

    public String getWorkerId(){
        return workerId;
    }
    public String getColor(){
        return defaultColor;
    }
}
