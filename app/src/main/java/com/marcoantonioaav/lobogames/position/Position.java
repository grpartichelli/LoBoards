package com.marcoantonioaav.lobogames.position;

import androidx.annotation.Nullable;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {

    private List<Position> connectedPositions = new ArrayList<>();
    private Coordinate coordinate;
    private final String label;
    private int occupiedBy;

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

    public int getOccupiedBy() {
        return this.occupiedBy;
    }

    public void setOccupiedBy(int occupiedBy) {
        this.occupiedBy = occupiedBy;
    }

    public List<Position> getConnectedPositions() {
        return connectedPositions;
    }

    public void setConnectedPositions(List<Position> connectedPositions) {
        this.connectedPositions = connectedPositions;
    }

    public Position copy() {
        Position newPosition = new Position(new Coordinate(this.coordinate.x(), this.coordinate.y()), this.label);
        newPosition.setOccupiedBy(this.getOccupiedBy());
        newPosition.setConnectedPositions(new ArrayList<>(connectedPositions));
        return newPosition;
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        return this.coordinate.equals(((Position) obj).getCoordinate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.coordinate.x(), this.coordinate.y());
    }
}
