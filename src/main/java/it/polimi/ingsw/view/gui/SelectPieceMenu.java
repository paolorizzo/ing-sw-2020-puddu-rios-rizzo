package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PieceBag;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.model.Action;
import javafx.event.EventHandler;
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

/**
 * Menu to chose the piece of a build move
 */
public class SelectPieceMenu extends Menu {
    private Rectangle rect;
    private Label LEVEL1Button;
    private Label LEVEL2Button;
    private Label LEVEL3Button;
    private Label DOMEButton;
    private PieceBag pieceBag;
    private Text lv1Count;
    private Text lv2Count;
    private Text lv3Count;
    private Text domeCount;
    private Text lv1Name;
    private Text lv2Name;
    private Text lv3Name;
    private Text domeName;


     /**
     * It creates and sets the menu graphics to chose the piece of build
     * @param widthResolution the width resolution of window
     * @param heightResolution hr the height resolution of window
     * @param actionFSM the action FSM of the turn
     * @param pieceBag the piece bag of the game
     */
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


        StackPane lv1NamePane = new StackPane();
        lv1NamePane.setTranslateX(rect.getTranslateX() + 20);
        lv1NamePane.setTranslateY(rect.getTranslateY() + 20);

        Rectangle lv1NameRect = new Rectangle(70, 30);
        lv1NameRect.setVisible(false);
        lv1NamePane.getChildren().add(lv1NameRect);

        lv1Name = new Text();
        lv1Name.setText("Lv1");
        lv1Name.setFont(new Font("Forte", 30));
        lv1Name.setFill(Color.GOLD);
        lv1Name.setStroke(Color.BLACK);
        lv1Name.setStrokeWidth(1);
        lv1Name.setTextAlignment(TextAlignment.CENTER);
        lv1Name.setTranslateY(0);
        lv1NamePane.getChildren().add(lv1Name);
        lv1NamePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
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

        StackPane lv2NamePane = new StackPane();
        lv2NamePane.setTranslateX(rect.getTranslateX() + 20+70+20);
        lv2NamePane.setTranslateY(rect.getTranslateY() + 20);

        Rectangle lv2NameRect = new Rectangle(70, 30);
        lv2NameRect.setVisible(false);
        lv2NamePane.getChildren().add(lv2NameRect);

        lv2Name = new Text();
        lv2Name.setText("Lv2");
        lv2Name.setFont(new Font("Forte", 30));
        lv2Name.setFill(Color.GOLD);
        lv2Name.setStroke(Color.BLACK);
        lv2Name.setStrokeWidth(1);
        lv2Name.setTextAlignment(TextAlignment.CENTER);
        lv2Name.setTranslateY(0);
        lv2NamePane.getChildren().add(lv2Name);
        lv2NamePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
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

        StackPane lv3NamePane = new StackPane();
        lv3NamePane.setTranslateX(rect.getTranslateX() + 20);
        lv3NamePane.setTranslateY(rect.getTranslateY() +20+70+20);

        Rectangle lv3NameRect = new Rectangle(70, 30);
        lv3NameRect.setVisible(false);
        lv3NamePane.getChildren().add(lv3NameRect);

        lv3Name = new Text();
        lv3Name.setText("Lv3");
        lv3Name.setFont(new Font("Forte", 30));
        lv3Name.setFill(Color.GOLD);
        lv3Name.setStroke(Color.BLACK);
        lv3Name.setStrokeWidth(1);
        lv3Name.setTextAlignment(TextAlignment.CENTER);
        lv3Name.setTranslateY(0);
        lv3NamePane.getChildren().add(lv3Name);
        lv3NamePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
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

        StackPane domeNamePane = new StackPane();
        domeNamePane.setTranslateX(rect.getTranslateX() + 20+70+20);
        domeNamePane.setTranslateY(rect.getTranslateY() +20+70+20);

        Rectangle domeNameRect = new Rectangle(70, 30);
        domeNameRect.setVisible(false);
        domeNamePane.getChildren().add(domeNameRect);

        domeName = new Text();
        domeName.setText("Dome");
        domeName.setFont(new Font("Forte", 30));
        domeName.setFill(Color.GOLD);
        domeName.setStroke(Color.BLACK);
        domeName.setStrokeWidth(1);
        domeName.setTextAlignment(TextAlignment.CENTER);
        domeName.setTranslateY(0);
        domeNamePane.getChildren().add(domeName);
        domeNamePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
        group.getChildren().add(lv1NamePane);
        group.getChildren().add(lv2NamePane);
        group.getChildren().add(lv3NamePane);
        group.getChildren().add(domeNamePane);

    }

    /**
     * It shows the menu and set the color of button based on the existing or not of the action
     * @param possibleActions the list of possible actions
     * @param worker_id the id of current worked selected for the action
     */
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
            lv1Name.setFill(Color.GOLD);
            lv1Image = GraphicsLoader.instance().getImage("lv1_button");
        }else {
            lv1Count.setFill(Color.LIGHTGRAY);
            lv1Name.setFill(Color.LIGHTGRAY);
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
            lv2Name.setFill(Color.GOLD);
            lv2Image = GraphicsLoader.instance().getImage("lv2_button");
        }else {
            lv2Count.setFill(Color.LIGHTGRAY);
            lv2Name.setFill(Color.LIGHTGRAY);
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
            lv3Name.setFill(Color.GOLD);
            lv3Image = GraphicsLoader.instance().getImage("lv3_button");
        }else {
            lv3Count.setFill(Color.LIGHTGRAY);
            lv3Name.setFill(Color.LIGHTGRAY);
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
            domeName.setFill(Color.GOLD);
            domeImage = GraphicsLoader.instance().getImage("dome_button");
        }else {
            domeCount.setFill(Color.LIGHTGRAY);
            domeName.setFill(Color.LIGHTGRAY);
            domeImage = GraphicsLoader.instance().getImage("dome_button_disabled");
        }
        ImageView domeImageView = new ImageView(domeImage);
        domeImageView.setFitHeight(70);
        domeImageView.setFitWidth(70);
        DOMEButton.setGraphic(domeImageView);

        super.show();
    }
}
