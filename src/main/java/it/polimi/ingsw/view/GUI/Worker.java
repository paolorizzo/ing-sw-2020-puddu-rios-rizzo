package it.polimi.ingsw.view.GUI;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;


public class Worker extends MeshView {
    private String workerId;
    private Color defaultColor;
    public Worker(String id, Color color){
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
        PhongMaterial defaultMat = new PhongMaterial();
        defaultMat.setDiffuseColor(defaultColor);
        this.setMaterial(defaultMat);
    }
    public void setToEnableView(){
        PhongMaterial enabled = new PhongMaterial();
        enabled.setDiffuseColor(Color.GREEN);
        this.setMaterial(enabled);
    }
    public void setToPreview(){
        PhongMaterial preview = new PhongMaterial();
        preview.setDiffuseColor(new Color(0.4862, 0.9882, 0f, 0.5));
        this.setMaterial(preview);
    }
    public void setToDisableView(){
        PhongMaterial disabled = new PhongMaterial();
        disabled.setDiffuseColor(Color.RED);
        this.setMaterial(disabled);
    }

    public String getWorkerId(){
        return workerId;
    }
}
