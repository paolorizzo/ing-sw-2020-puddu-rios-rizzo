package it.polimi.ingsw.view.gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Menu to voluntary end the turn
 */
public class EndOfTurnMenu extends Menu {
    private Rectangle endOfTurn;

    /**
     * It creates and sets the menu graphics to do a voluntary end of turn
     * @param widthResolution the width resolution of window
     * @param heightResolution hr the height resolution of window
     * @param actionFSM the action FSM of the turn
     */
    public EndOfTurnMenu(int widthResolution, int heightResolution, final ActionFSM actionFSM) {
        super(widthResolution, heightResolution);


        endOfTurn = new Rectangle(150, 30);
        endOfTurn.setFill(Color.RED);
        endOfTurn.setStroke(Color.BLACK);
        endOfTurn.setStrokeWidth(2);
        endOfTurn.setTranslateX(widthResolution - 150 - endOfTurn.getWidth());
        endOfTurn.setTranslateY(heightResolution - 100 - endOfTurn.getHeight());

        StackPane endOfTurnPane = new StackPane();
        endOfTurnPane.setTranslateX(endOfTurn.getTranslateX());
        endOfTurnPane.setTranslateY(endOfTurn.getTranslateY());
        endOfTurnPane.setMaxHeight(endOfTurn.getHeight());
        endOfTurnPane.setMinHeight(endOfTurn.getHeight());
        endOfTurnPane.setMaxWidth(endOfTurn.getWidth());
        endOfTurnPane.setMinWidth(endOfTurn.getWidth());

        Text endOfTurnText = new Text();
        endOfTurnText.setText("END TURN");
        endOfTurnText.setFont(new Font("Forte", 25));
        endOfTurnText.setFill(Color.WHITE);
        endOfTurnText.setStroke(Color.BLACK);
        endOfTurnText.setStrokeWidth(1);
        endOfTurnText.setTextAlignment(TextAlignment.CENTER);
        endOfTurnPane.getChildren().add(endOfTurnText);

        endOfTurnPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                actionFSM.voluntaryEndOfTurn();
            }
        });
        group.getChildren().add(endOfTurn);
        group.getChildren().add(endOfTurnPane);

    }
}
