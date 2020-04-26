package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.view.ClientView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AskCardMenu extends Group {
    Rectangle rect;
    final ClientView cw;

    HashMap<Integer, Label> cardLabels;
    public AskCardMenu(ClientView clientView) {
        this.cw = clientView;
        rect = new Rectangle(200, 100);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        rect.setTranslateX(-rect.getWidth()/2);
        rect.setTranslateY(-100);
        rect.setTranslateZ(-500);

        this.getChildren().add(rect);
        //TODO file json with all cards
        cardLabels = new HashMap<>();
        cardLabels.put(1, new Label(1+" Apollo "+"Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));
        cardLabels.get(1).setVisible(false);
        cardLabels.get(1).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(1);
            }
        });
        this.getChildren().add(cardLabels.get(1));

        cardLabels.put(2, new Label(2+" Artemis"+" Your Move: Your Worker may move one additional time, but not back to its initial space. "));
        cardLabels.get(2).setVisible(false);
        this.getChildren().add(cardLabels.get(2));
        cardLabels.get(2).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(2);
            }
        });

        cardLabels.put(3, new Label(3+" Athena"+" Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."));
        cardLabels.get(3).setVisible(false);
        this.getChildren().add(cardLabels.get(3));
        cardLabels.get(3).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(3);
            }
        });

        cardLabels.put(4, new Label(4+" Atlas"+" Your Build: Your Worker may build a dome at any level."));
        cardLabels.get(4).setVisible(false);
        this.getChildren().add(cardLabels.get(4));
        cardLabels.get(4).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(4);
            }
        });

        cardLabels.put(5, new Label(5+" Demeter"+" Your Build: Your Worker may build one additional time, but not on the same space."));
        cardLabels.get(5).setVisible(false);
        this.getChildren().add(cardLabels.get(5));
        cardLabels.get(5).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(5);
            }
        });

        cardLabels.put(6, new Label(6+" Hephaestus"+" Your Build: Your Worker may build one additional block (not dome) on top of your first block."));
        cardLabels.get(6).setVisible(false);
        this.getChildren().add(cardLabels.get(6));
        cardLabels.get(6).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(6);
            }
        });

        cardLabels.put(8, new Label(8+" Minotaur"+" Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."));
        cardLabels.get(8).setVisible(false);
        this.getChildren().add(cardLabels.get(8));
        cardLabels.get(8).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(8);
            }
        });

        cardLabels.put(9, new Label(9+" Pan"+" Win Condition: You also win if your Worker moves down two or more levels."));
        cardLabels.get(9).setVisible(false);
        this.getChildren().add(cardLabels.get(9));
        cardLabels.get(9).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(9);
            }
        });

        cardLabels.put(10, new Label(10+" Prometheus"+" Your Turn: If your Worker does not move up, it may build both before and after moving."));
        cardLabels.get(10).setVisible(false);
        this.getChildren().add(cardLabels.get(10));
        cardLabels.get(10).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                cw.updateReadNumCard(10);
            }
        });
        hide();
    }
    public void setDeck(Deck deck){
        int offsetX = 30, offsetY = 20;
        List<Card> cards = deck.getCards();
        for(Card card: cards){
            cardLabels.get(card.getNum()).setVisible(true);
            cardLabels.get(card.getNum()).setTranslateX(rect.getTranslateX() + offsetX);
            cardLabels.get(card.getNum()).setTranslateY(rect.getTranslateY() + offsetY);
            cardLabels.get(card.getNum()).setTranslateZ(-500);
            offsetY +=20;
        }
    }

    public void show(){
        rect.setVisible(true);
        this.setVisible(true);
    }
    public void hide(){
        this.setVisible(false);
        rect.setVisible(false);
        for(Label label: cardLabels.values())
            label.setVisible(false);
    }
}