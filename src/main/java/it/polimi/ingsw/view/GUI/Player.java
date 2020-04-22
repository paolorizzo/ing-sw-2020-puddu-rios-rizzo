package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.Color;

public class Player {
    private int id;
    private String name;
    private Color color;

    public Player(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
