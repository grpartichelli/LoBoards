package com.marcoantonioaav.lobogames.position;

import androidx.annotation.Nullable;
import com.marcoantonioaav.lobogames.move.Movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {

    public static Position instanceOutOfBounds() {
        return new Position(new Coordinate(Movement.OUT_OF_BOARD, Movement.OUT_OF_BOARD), "");
    }

    private List<Position> connectedPositions = new ArrayList<>();
    private Coordinate coordinate;
    private final String identifier;
    private int occupiedBy;

    public Position(Coordinate coordinate, String identifier) {
        this.coordinate = coordinate;
        this.identifier = identifier;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getIdentifier() {
        return identifier;
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

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        return this.getIdentifier().equals(((Position) obj).identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identifier);
    }
}
