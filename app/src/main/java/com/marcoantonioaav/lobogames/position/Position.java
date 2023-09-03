package com.marcoantonioaav.lobogames.position;

import androidx.annotation.Nullable;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Position {

    public static Position instanceOutOfBoard() {
        return new Position(Coordinate.instanceOutOfBounds(), "", -1);
    }

    private final List<Position> connectedPositions = new ArrayList<>();
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

    public void addAllConnectedPositions(Collection<? extends Position> positions) {
        this.connectedPositions.addAll(positions);
    }

    public void addConnectedPosition(Position position) {
        this.connectedPositions.add(position);
    }

    public Position copy() {
        Position newPosition = new Position(this.coordinate.copy(), this.label, this.accessibilityOrder);
        newPosition.setPlayerId(this.getPlayerId());
        newPosition.addAllConnectedPositions(new ArrayList<>(connectedPositions));
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

    public boolean hasAnyEmptyConnectedPositions() {
        for (Position connectedPosition: this.connectedPositions) {
            if (connectedPosition.getPlayerId() == Player.EMPTY) {
                return true;
            }
        }
        return false;
    }

    public boolean isConnectedTo(Position other) {
        for (Position connectedPosition: this.connectedPositions) {
            if (connectedPosition.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public List<Position> findAllConnectedPositionsForPlayerId(int playerId) {
        List<Position> connectedPositionsOfPlayer = new ArrayList<>();
        for (Position connectedPosition: this.connectedPositions) {
            if (connectedPosition.getPlayerId() == playerId) {
                connectedPositionsOfPlayer.add(connectedPosition);
            }
        }
        return connectedPositionsOfPlayer;
    }

    public List<Position> findAllEmptyConnectedPosition() {
        return findAllConnectedPositionsForPlayerId(Player.EMPTY);
    }
}
