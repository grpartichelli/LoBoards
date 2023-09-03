package com.marcoantonioaav.lobogames.position;

import androidx.annotation.Nullable;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Position {

    public static Position instanceOutOfBoard() {
        return new Position(Coordinate.instanceOutOfBoard(), "", -1);
    }

    private List<String> connectedPositionsIds = new ArrayList<>();
    private Coordinate coordinate;
    private final String id;
    private int playerId = Player.EMPTY;
    private final int accessibilityOrder;

    public Position(Coordinate coordinate, String id, int accessibilityOrder) {
        this.coordinate = coordinate;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void addAllConnectedPositions(Collection<? extends Position> positions) {
        List<String> positionsIds = new ArrayList<>();
        for (Position position: positions) {
            positionsIds.add(position.getId());
        }
        this.connectedPositionsIds.addAll(positionsIds);
    }

    public List<String> getConnectedPositionsIds() {
        return connectedPositionsIds;
    }

    public void setConnectedPositionsIds(List<String> connectedPositionsIds) {
        this.connectedPositionsIds = connectedPositionsIds;
    }

    public Position copy() {
        Position newPosition = new Position(this.coordinate.copy(), this.id, this.accessibilityOrder);
        newPosition.setPlayerId(this.getPlayerId());
        newPosition.setConnectedPositionsIds(new ArrayList<>(this.getConnectedPositionsIds()));
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
        return this.getId().equals(((Position) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public boolean isOutOfBoard() {
        return this.coordinate.equals(Coordinate.instanceOutOfBoard());
    }

    public boolean isConnectedTo(Position other) {
        for (String connectedPositionId: this.connectedPositionsIds) {
            if (connectedPositionId.equals(other.getId())) {
                return true;
            }
        }
        return false;
    }
}
