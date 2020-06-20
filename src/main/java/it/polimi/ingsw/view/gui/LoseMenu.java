package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LoseMenu extends Menu {
    ImageView godView;

    public LoseMenu(int widthResolution, int heightResolution) {
        super(widthResolution, heightResolution);
        Rectangle rect = new Rectangle(500, 300);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution/2-rect.getWidth()/2);
        rect.setTranslateY(heightResolution/2-rect.getHeight()/2);
        rect.setTranslateZ(0);

        group.getChildren().add(rect);

        StackPane textPane = new StackPane();

        textPane.setMinWidth(rect.getWidth());
        textPane.setTranslateX(rect.getTranslateX());
        textPane.setTranslateY(rect.getTranslateY()+20);

        Text text = new Text();
        text.setText("YOU LOSE");
        text.setFont(new Font("Forte", 30));
        text.setFill(Color.RED);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(1);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTranslateY(0);
        textPane.getChildren().add(text);

        group.getChildren().add(textPane);


        Image volcano = GraphicsLoader.instance().getImage("volcano");
        ImageView volcanoView1 = new ImageView(volcano);
        volcanoView1.setFitHeight(200);
        volcanoView1.setFitWidth(200);
        volcanoView1.setTranslateX(rect.getTranslateX()+rect.getWidth()/2);
        volcanoView1.setTranslateY(rect.getTranslateY()+40);
        group.getChildren().add(volcanoView1);

        ImageView volcanoView2 = new ImageView(volcano);
        volcanoView2.setFitHeight(200);
        volcanoView2.setFitWidth(200);
        volcanoView2.setTranslateX(rect.getTranslateX()+rect.getWidth()/2-volcanoView2.getFitWidth());
        volcanoView2.setTranslateY(rect.getTranslateY()+40);
        group.getChildren().add(volcanoView2);


        Image god = GraphicsLoader.instance().getImage("Zeus_podium");
        godView = new ImageView(god);
        godView.setFitHeight(150);
        godView.setFitWidth(150);
        godView.setTranslateX(rect.getTranslateX()+rect.getWidth()/2-godView.getFitWidth()/2);
        godView.setTranslateY(rect.getTranslateY()+80);
        group.getChildren().add(godView);

        Image cage = GraphicsLoader.instance().getImage("cage");
        ImageView cageView = new ImageView(cage);
        cageView.setFitHeight(150);
        cageView.setFitWidth(150);
        cageView.setTranslateX(rect.getTranslateX()+rect.getWidth()/2-cageView.getFitWidth()/2);
        cageView.setTranslateY(rect.getTranslateY()+120);
        group.getChildren().add(cageView);

    }

    public void setGodView(Card card){
        Image god = GraphicsLoader.instance().getImage(card.getName()+"_podium");
        godView.setImage(god);
    }
}
