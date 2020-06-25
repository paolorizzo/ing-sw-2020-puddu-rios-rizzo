package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.BuildAction;
import it.polimi.ingsw.model.MoveAction;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.util.List;

public class SelectTypeActionMenu extends Menu {
    Rectangle rect;
    Label moveButton;
    Label buildButton;
    Rectangle unselectButton;
    public SelectTypeActionMenu(int widthResolution, int heightResolution, final ActionFSM actionFSM) {
        super(widthResolution, heightResolution);

        rect = new Rectangle(200, 100);
        ImagePattern patt = new ImagePattern(GraphicsLoader.instance().getImage("background_menu"), 0, 0, 750, 750, false);
        rect.setFill(patt);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(widthResolution-rect.getWidth()-50);
        rect.setTranslateY(50);

        Image moveImage = GraphicsLoader.instance().getImage("move_button");
        ImageView moveImageView = new ImageView(moveImage);
        moveImageView.setFitHeight(70);
        moveImageView.setFitWidth(70);
        moveButton = new Label("");
        moveButton.setTranslateX(rect.getTranslateX() + 20);
        moveButton.setTranslateY(rect.getTranslateY() + 15);
        moveButton.setGraphic(moveImageView);
        moveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("move");
            }
        });

        Image buildImage = GraphicsLoader.instance().getImage("build_button");
        ImageView buildImageView = new ImageView(buildImage);
        buildImageView.setFitHeight(70);
        buildImageView.setFitWidth(70);
        buildButton = new Label("");
        buildButton.setTranslateX(rect.getTranslateX() + 110);
        buildButton.setTranslateY(rect.getTranslateY() + 15);
        buildButton.setGraphic(buildImageView);
        buildButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("build");
            }
        });

        unselectButton = new Rectangle(15, 15);
        unselectButton.setTranslateX(rect.getTranslateX() + rect.getWidth() - unselectButton.getWidth());
        unselectButton.setTranslateY(rect.getTranslateY());
        unselectButton.setFill(Color.RED);
        unselectButton.setStroke(Color.BLACK);
        unselectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                actionFSM.execute("unselect");
            }
        });


        group.getChildren().add(rect);
        group.getChildren().add(moveButton);
        group.getChildren().add(buildButton);
        group.getChildren().add(unselectButton);

    }
    public void show(List<Action> possibleActions, String worker_id){
        boolean move = false;
        boolean build = false;
        for(Action action: possibleActions){
            if(action.matches(worker_id)){
                if(action instanceof MoveAction)
                    move = true;
                if(action instanceof BuildAction)
                    build = true;
            }
        }
        Image moveImage;
        if(move)
            moveImage = GraphicsLoader.instance().getImage("move_button");
        else
            moveImage = GraphicsLoader.instance().getImage("move_button_disabled");
        ImageView moveImageView = new ImageView(moveImage);
        moveImageView.setFitHeight(70);
        moveImageView.setFitWidth(70);
        moveButton.setGraphic(moveImageView);

        Image buildImage;
        if(build)
            buildImage = GraphicsLoader.instance().getImage("build_button");
        else
            buildImage = GraphicsLoader.instance().getImage("build_button_disabled");
        ImageView buildImageView = new ImageView(buildImage);
        buildImageView.setFitHeight(70);
        buildImageView.setFitWidth(70);
        buildButton.setGraphic(buildImageView);

        super.show();
    }
}
