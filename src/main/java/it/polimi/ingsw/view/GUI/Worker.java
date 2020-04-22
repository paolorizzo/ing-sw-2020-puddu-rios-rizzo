package it.polimi.ingsw.view.GUI;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
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

        if(workerId.charAt(workerId.length()-1) == 'M'){
            this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_MALE_"+defaultColor));
        }else{
            this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_FEMALE_"+defaultColor));
        }

    }
    public void setToEnableView(){

        if(workerId.charAt(workerId.length()-1) == 'M'){
            this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_MALE_ENABLED"));
        }else{
            this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_FEMALE_ENABLED"));
        }
    }
    public void setToPreview(){

        if(workerId.charAt(workerId.length()-1) == 'M'){
            this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_MALE_PREVIEW"));
        }else{
            this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_FEMALE_PREVIEW"));
        }

    }
    public void setToDisableView(){
        if(workerId.charAt(workerId.length()-1) == 'M'){
            this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_MALE_DISABLED"));
        }else{
            this.setMaterial(GraphicsLoader.instance().getTexture("WORKER_FEMALE_DISABLED"));
        }
    }

    public String getWorkerId(){
        return workerId;
    }
}
