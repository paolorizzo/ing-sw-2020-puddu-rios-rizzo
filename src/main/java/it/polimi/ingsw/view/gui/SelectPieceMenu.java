package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class SelectPieceMenu extends Menu {
    Rectangle rect;
    Label LEVEL1Button;
    Label LEVEL2Button;
    Label LEVEL3Button;
    Label DOMEButton;
    PieceBag pieceBag;
    Text lv1Count;
    Text lv2Count;
    Text lv3Count;
    Text domeCount;
    public SelectPieceMenu(int widthResolution, int heightResolution, final ActionFSM actionFSM, PieceBag pieceBag) {
        super(widthResolution, heightResolution);
        this.pieceBag = pieceBag;
        //BOX
        rect = new Rectangle(200, 200);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution-rect.getWidth()-50);
        rect.setTranslateY(200);
        //LEVEL1
        Image lv1Image = GraphicsLoader.instance().getImage("lv1_button");
        ImageView lv1ImageView = new ImageView(lv1Image);
        lv1ImageView.setFitHeight(70);
        lv1ImageView.setFitWidth(70);
        LEVEL1Button = new Label("");
        LEVEL1Button.setTranslateX(rect.getTranslateX() + 20);
        LEVEL1Button.setTranslateY(rect.getTranslateY() + 20);
        LEVEL1Button.setGraphic(lv1ImageView);
        LEVEL1Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL1);
            }
        });

        StackPane lv1Pane = new StackPane();
        lv1Pane.setTranslateX(rect.getTranslateX() + 20);
        lv1Pane.setTranslateY(rect.getTranslateY() + 20 + 40);

        Rectangle lv1Rect = new Rectangle(70, 30);
        lv1Rect.setVisible(false);
        lv1Pane.getChildren().add(lv1Rect);

        lv1Count = new Text();
        lv1Count.setText(""+pieceBag.getCount(Piece.LEVEL1));
        lv1Count.setFont(new Font("Forte", 30));
        lv1Count.setFill(Color.ROYALBLUE);
        lv1Count.setStroke(Color.BLACK);
        lv1Count.setStrokeWidth(1);
        lv1Count.setTextAlignment(TextAlignment.CENTER);
        lv1Count.setTranslateY(0);
        lv1Pane.getChildren().add(lv1Count);
        lv1Pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL1);
            }
        });
        //LEVEL2
        Image lv2Image = GraphicsLoader.instance().getImage("lv2_button");
        ImageView lv2ImageView = new ImageView(lv2Image);
        lv2ImageView.setFitHeight(70);
        lv2ImageView.setFitWidth(70);
        LEVEL2Button = new Label("");
        LEVEL2Button.setTranslateX(rect.getTranslateX()+20+70+20);
        LEVEL2Button.setTranslateY(rect.getTranslateY()+20);
        LEVEL2Button.setGraphic(lv2ImageView);
        LEVEL2Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL2);
            }
        });

        StackPane lv2Pane = new StackPane();
        lv2Pane.setTranslateX(rect.getTranslateX() + 20+70+20);
        lv2Pane.setTranslateY(rect.getTranslateY() + 20 + 40);

        Rectangle lv2Rect = new Rectangle(70, 30);
        lv2Rect.setVisible(false);
        lv2Pane.getChildren().add(lv2Rect);

        lv2Count = new Text();
        lv2Count.setText(""+pieceBag.getCount(Piece.LEVEL2));
        lv2Count.setFont(new Font("Forte", 30));
        lv2Count.setFill(Color.ROYALBLUE);
        lv2Count.setStroke(Color.BLACK);
        lv2Count.setStrokeWidth(1);
        lv2Count.setTextAlignment(TextAlignment.CENTER);
        lv2Count.setTranslateY(0);
        lv2Pane.getChildren().add(lv2Count);
        lv2Pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL2);
            }
        });
        //LEVEL3
        Image lv3Image = GraphicsLoader.instance().getImage("lv3_button");
        ImageView lv3ImageView = new ImageView(lv3Image);
        lv3ImageView.setFitHeight(70);
        lv3ImageView.setFitWidth(70);
        LEVEL3Button = new Label("");
        LEVEL3Button.setTranslateX(rect.getTranslateX()+20);
        LEVEL3Button.setTranslateY(rect.getTranslateY()+20+70+20);
        LEVEL3Button.setGraphic(lv3ImageView);
        LEVEL3Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL3);
            }
        });

        StackPane lv3Pane = new StackPane();
        lv3Pane.setTranslateX(rect.getTranslateX() + 20);
        lv3Pane.setTranslateY(rect.getTranslateY()+20+70+20+40);

        Rectangle lv3Rect = new Rectangle(70, 30);
        lv3Rect.setVisible(false);
        lv3Pane.getChildren().add(lv3Rect);

        lv3Count = new Text();
        lv3Count.setText(""+pieceBag.getCount(Piece.LEVEL3));
        lv3Count.setFont(new Font("Forte", 30));
        lv3Count.setFill(Color.ROYALBLUE);
        lv3Count.setStroke(Color.BLACK);
        lv3Count.setStrokeWidth(1);
        lv3Count.setTextAlignment(TextAlignment.CENTER);
        lv3Count.setTranslateY(0);
        lv3Pane.getChildren().add(lv3Count);
        lv3Pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.LEVEL3);
            }
        });
        //DOME
        Image domeImage = GraphicsLoader.instance().getImage("dome_button");
        ImageView domeImageView = new ImageView(domeImage);
        domeImageView.setFitHeight(70);
        domeImageView.setFitWidth(70);
        DOMEButton = new Label("");
        DOMEButton.setTranslateX(rect.getTranslateX()+20+70+20);
        DOMEButton.setTranslateY(rect.getTranslateY()+20+70+20);
        DOMEButton.setGraphic(domeImageView);
        DOMEButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.DOME);
            }
        });

        StackPane domePane = new StackPane();
        domePane.setTranslateX(rect.getTranslateX()+20+70+20);
        domePane.setTranslateY(rect.getTranslateY()+20+70+20+40);

        Rectangle domeRect = new Rectangle(70, 30);
        domeRect.setVisible(false);
        domePane.getChildren().add(domeRect);

        domeCount = new Text();
        domeCount.setText(""+pieceBag.getCount(Piece.DOME));
        domeCount.setFont(new Font("Forte", 30));
        domeCount.setFill(Color.ROYALBLUE);
        domeCount.setStroke(Color.BLACK);
        domeCount.setStrokeWidth(1);
        domeCount.setTextAlignment(TextAlignment.CENTER);
        domeCount.setTranslateY(0);
        domePane.getChildren().add(domeCount);
        domePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute(Piece.DOME);
            }
        });

        group.getChildren().add(rect);
        group.getChildren().add(LEVEL1Button);
        group.getChildren().add(LEVEL2Button);
        group.getChildren().add(LEVEL3Button);
        group.getChildren().add(DOMEButton);
        group.getChildren().add(lv1Pane);
        group.getChildren().add(lv2Pane);
        group.getChildren().add(lv3Pane);
        group.getChildren().add(domePane);
    }

    public void show(List<Action> possibleActions, String worker_id){
        boolean lv1 = false, lv2 = false, lv3 = false, dome = false;
        for(Action action: possibleActions){
            if(action.matches(worker_id, Piece.LEVEL1))
                lv1 = true;
            if(action.matches(worker_id, Piece.LEVEL2))
                lv2 = true;
            if(action.matches(worker_id, Piece.LEVEL3))
                lv3 = true;
            if(action.matches(worker_id, Piece.DOME))
                dome = true;
        }

        Image lv1Image;
        lv1Count.setText(""+pieceBag.getCount(Piece.LEVEL1));
        if(lv1) {
            lv1Count.setFill(Color.ROYALBLUE);
            lv1Image = GraphicsLoader.instance().getImage("lv1_button");
        }else {
            lv1Count.setFill(Color.LIGHTGRAY);
            lv1Image = GraphicsLoader.instance().getImage("lv1_button_disabled");
        }
        ImageView lv1ImageView = new ImageView(lv1Image);
        lv1ImageView.setFitHeight(70);
        lv1ImageView.setFitWidth(70);
        LEVEL1Button.setGraphic(lv1ImageView);

        Image lv2Image;
        lv2Count.setText(""+pieceBag.getCount(Piece.LEVEL2));
        if(lv2) {
            lv2Count.setFill(Color.ROYALBLUE);
            lv2Image = GraphicsLoader.instance().getImage("lv2_button");
        }else {
            lv2Count.setFill(Color.LIGHTGRAY);
            lv2Image = GraphicsLoader.instance().getImage("lv2_button_disabled");
        }
        ImageView lv2ImageView = new ImageView(lv2Image);
        lv2ImageView.setFitHeight(70);
        lv2ImageView.setFitWidth(70);
        LEVEL2Button.setGraphic(lv2ImageView);

        Image lv3Image;
        lv3Count.setText(""+pieceBag.getCount(Piece.LEVEL3));
        if(lv3) {
            lv3Count.setFill(Color.ROYALBLUE);
            lv3Image = GraphicsLoader.instance().getImage("lv3_button");
        }else {
            lv3Count.setFill(Color.LIGHTGRAY);
            lv3Image = GraphicsLoader.instance().getImage("lv3_button_disabled");
        }
        ImageView lv3ImageView = new ImageView(lv3Image);
        lv3ImageView.setFitHeight(70);
        lv3ImageView.setFitWidth(70);
        LEVEL3Button.setGraphic(lv3ImageView);

        Image domeImage;
        domeCount.setText(""+pieceBag.getCount(Piece.DOME));
        if(dome) {
            domeCount.setFill(Color.ROYALBLUE);
            domeImage = GraphicsLoader.instance().getImage("dome_button");
        }else {
            domeCount.setFill(Color.LIGHTGRAY);
            domeImage = GraphicsLoader.instance().getImage("dome_button_disabled");
        }
        ImageView domeImageView = new ImageView(domeImage);
        domeImageView.setFitHeight(70);
        domeImageView.setFitWidth(70);
        DOMEButton.setGraphic(domeImageView);

        super.show();
    }
}
