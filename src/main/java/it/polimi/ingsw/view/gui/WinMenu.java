package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.view.cli.RectangleCLI;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class WinMenu extends Menu {
    ImageView godView;

    public WinMenu(){
        super();

        Rectangle rect = new Rectangle(500, 300);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(1400/2-rect.getWidth()/2);
        rect.setTranslateY(800/2-rect.getHeight()/2);
        rect.setTranslateZ(0);

        group.getChildren().add(rect);

        StackPane textPane = new StackPane();

        textPane.setMinWidth(rect.getWidth());
        textPane.setTranslateX(rect.getTranslateX());
        textPane.setTranslateY(rect.getTranslateY()+20);

        Text text = new Text();
        text.setText("YOU WIN");
        text.setFont(new Font("Forte", 30));
        text.setFill(Color.GOLD);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(1);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTranslateY(0);
        textPane.getChildren().add(text);

        group.getChildren().add(textPane);


        Image parthenon = GraphicsLoader.instance().getImage("parthenon");
        ImageView parthenonView1 = new ImageView(parthenon);
        parthenonView1.setFitHeight(200);
        parthenonView1.setFitWidth(200);
        parthenonView1.setTranslateX(rect.getTranslateX()+rect.getWidth()/2);
        parthenonView1.setTranslateY(rect.getTranslateY()+40);
        group.getChildren().add(parthenonView1);

        ImageView parthenonView2 = new ImageView(parthenon);
        parthenonView2.setFitHeight(200);
        parthenonView2.setFitWidth(200);
        parthenonView2.setTranslateX(rect.getTranslateX()+rect.getWidth()/2-parthenonView2.getFitWidth());
        parthenonView2.setTranslateY(rect.getTranslateY()+40);
        group.getChildren().add(parthenonView2);

        Image podium = GraphicsLoader.instance().getImage("podium");
        ImageView podiumView = new ImageView(podium);
        podiumView.setFitHeight(120);
        podiumView.setFitWidth(150);
        podiumView.setTranslateX(rect.getTranslateX()+rect.getWidth()/2-podiumView.getFitWidth()/2);
        podiumView.setTranslateY(rect.getTranslateY()+150);
        group.getChildren().add(podiumView);

        Image god = GraphicsLoader.instance().getImage("Zeus_podium");
        godView = new ImageView(god);
        godView.setFitHeight(150);
        godView.setFitWidth(150);
        godView.setTranslateX(rect.getTranslateX()+rect.getWidth()/2-godView.getFitWidth()/2);
        godView.setTranslateY(rect.getTranslateY()+30);
        group.getChildren().add(godView);
    }

    public void setGodView(Card card){
        Image god = GraphicsLoader.instance().getImage(card.getName()+"_podium");
        godView.setImage(god);
    }
}