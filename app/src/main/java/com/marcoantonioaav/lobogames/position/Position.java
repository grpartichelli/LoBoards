package com.marcoantonioaav.lobogames.position;

import com.marcoantonioaav.lobogames.player.Player;

public class Position {

    private final int x;
    private final int y;
    private final String label;
    private Player occupiedBy;

    public Position(int x, int y, String label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getLabel() {
        return label;
    }

    public Player getOccupiedBy() {
        return this.occupiedBy;
    }

    public void setOccupiedBy(Player occupiedBy) {
        this.occupiedBy = occupiedBy;
    }
}
