package com.marcoantonioaav.lobogames.position;

import androidx.annotation.Nullable;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {

    public static Position instanceOutOfBoard() {
        return new Position(Coordinate.instanceOutOfBounds(), "", -1);
    }

    private List<Position> connectedPositions = new ArrayList<>();
    private Coordinate coordinate;
    private final String label;
    private int playerId = Player.EMPTY;
    private final int accessibilityOrder;

    public Position(Coordinate coordinate, String label, int accessibilityOrder) {
        this.coordinate = coordinate;
        this.label = label;
        this.accessibilityOrder = accessibilityOrder;
    }

    public int getAccessibilityOrder() {
        return accessibilityOrder;
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

    public int getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<Position> getConnectedPositions() {
        return connectedPositions;
    }

    public void setConnectedPositions(List<Position> connectedPositions) {
        this.connectedPositions = connectedPositions;
    }

    public Position copy() {
        Position newPosition = new Position(new Coordinate(this.coordinate.x(), this.coordinate.y()), this.label, this.accessibilityOrder);
        newPosition.setPlayerId(this.getPlayerId());
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
        return this.getLabel().equals(((Position) obj).label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.label);
    }

    public boolean isOutOfBoard() {
        return this.coordinate.equals(Coordinate.instanceOutOfBounds());
    }
}
