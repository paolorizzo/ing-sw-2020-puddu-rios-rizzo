package it.polimi.ingsw.view.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class ErrorVisualizer extends Group {
    StackPane errorPane;
    Text errorText;
    Rectangle rect;
    public ErrorVisualizer(){
        errorPane = new StackPane();
        rect = new Rectangle(1400, 50);
        rect.setFill(Color.RED);
        rect.setVisible(false);

        errorPane.setTranslateY(800-rect.getHeight());

        errorText = new Text();
        errorText.setText("");
        errorText.setFont(new Font("Forte", 30));
        errorText.setFill(Color.RED);
        errorText.setStroke(Color.BLACK);
        errorText.setStrokeWidth(1);
        errorText.setTextAlignment(TextAlignment.CENTER);

        errorPane.getChildren().add(rect);
        errorPane.getChildren().add(errorText);
        this.getChildren().add(errorPane);
    }
    public void showError(String error){
        errorText.setText(error);
        errorText.setOpacity(1.0);
        errorPane.setTranslateY(800-rect.getHeight());

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000), errorText);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        Duration duration = Duration.millis(2500);

        TranslateTransition translateTransition = new TranslateTransition(duration, errorText);
        translateTransition.setFromY(0);
        translateTransition.setByY(-50);

        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, translateTransition);
        parallelTransition.play();
    }
}
