package com.marcoantonioaav.lobogames.position;

import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Position {

    private final List<Position> connectedPositions = new ArrayList<>();
    private Coordinate coordinate;
    private final String label;
    private Player occupiedBy;

    public Position(Coordinate coordinate, String label) {
        this.coordinate = coordinate;
        this.label = label;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
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
