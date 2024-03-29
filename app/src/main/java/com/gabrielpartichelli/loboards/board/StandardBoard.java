package com.gabrielpartichelli.loboards.board;

import android.graphics.drawable.Drawable;
import com.gabrielpartichelli.loboards.move.Movement;
import com.gabrielpartichelli.loboards.player.Player;
import com.gabrielpartichelli.loboards.position.Connection;
import com.gabrielpartichelli.loboards.position.Coordinate;
import com.gabrielpartichelli.loboards.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardBoard extends Board {

    private final Map<String, Position> positionsMap = new HashMap<>();
    private final List<Connection> connections = new ArrayList<>();

    public StandardBoard(
            Drawable image,
            double paddingPercentage,
            double positionRadiusScale,
            List<Position> positions,
            List<Connection> connections
    ) {
        this(image, paddingPercentage, paddingPercentage, positionRadiusScale, positions, connections);
    }

    public StandardBoard(
            Drawable image,
            double paddingPercentageHorizontal,
            double paddingPercentageVertical,
            double positionRadiusScale,
            List<Position> positions,
            List<Connection> connections
    ) {
        super(image, paddingPercentageHorizontal, paddingPercentageVertical, positionRadiusScale);
        for (Position position: positions) {
            this.positionsMap.put(position.getId(), position);
        }
        this.connections.addAll(connections);
    }

    @Override
    public void applyMovement(Movement movement) {
        if (movement == null) {
            return;
        }

        Position startPosition = this.findPositionById(movement.getStartPositionId());
        Position endPosition = this.findPositionById(movement.getEndPositionId());

        if (!startPosition.isOutOfBoard()) {
            startPosition.setPlayerId(Player.EMPTY);
            this.positionsMap.put(startPosition.getId(), startPosition);
        }

        if (!endPosition.isOutOfBoard()) {
            endPosition.setPlayerId(movement.getPlayerId());
            this.positionsMap.put(endPosition.getId(), endPosition);
        }
    }

    @Override
    public Board copy() {
        List<Position> copiedPositions = new ArrayList<>();
        for (Position position: this.positionsMap.values()) {
            copiedPositions.add(position.copy());
        }
        List<Connection> copiedConnections =  new ArrayList<>();
        for (Connection connection: this.connections) {
            copiedConnections.add(connection.copy());
        }
        return new StandardBoard(
                this.image,
                this.paddingPercentageHorizontal,
                this.paddingPercentageVertical,
                this.positionRadiusScale,
                copiedPositions,
                copiedConnections
        );
    }

    @Override
    public List<Position> getPositions() {
        return new ArrayList<>(this.positionsMap.values());
    }

    @Override
    public void updateCoordinate(Position position, Coordinate newCoordinate) {
        Position positionToUpdate = findPositionById(position.getId());
        positionToUpdate.setCoordinate(newCoordinate);
        positionsMap.put(positionToUpdate.getId(), positionToUpdate);
    }

    @Override
    public void updateCoordinatesBetween(double imageWidth, double imageHeight, double left, double top, double right, double bottom) {
        for (Connection connection: connections) {
            List<Coordinate> newCoordinatesBetween = new ArrayList<>();
            for (Coordinate coordinate: connection.getCoordinatesBetween()) {
                double currentX = coordinate.x();
                double currentY = coordinate.y();
                int newX = (int) (((currentX / imageWidth) * (right - left)) + left);
                int newY = (int) (((currentY / imageHeight) * (bottom - top)) + top);
                newCoordinatesBetween.add(new Coordinate(newX, newY));
            }
            connection.getCoordinatesBetween().clear();
            connection.getCoordinatesBetween().addAll(newCoordinatesBetween);
        }
    }

    @Override
    public List<Coordinate> findCoordinatesBetween(Position startPosition, Position endPosition) {
        for (Connection connection: connections) {
            if (connection.getStartPositionId().equals(startPosition.getId()) 
            && connection.getEndPositionId().equals(endPosition.getId())) {
                return connection.getCoordinatesBetween();
            }
        }
        return new ArrayList<>(Arrays.asList(startPosition.getCoordinate(), endPosition.getCoordinate()));
    }

    public boolean areConnected(Position position, Position otherPosition) {
        for (Connection connection: connections) {
            if (connection.getStartPositionId().equals(position.getId()) && connection.getEndPositionId().equals(otherPosition.getId())
            || connection.getStartPositionId().equals(otherPosition.getId()) && connection.getEndPositionId().equals(position.getId())) {
                return true;
            }
        }
        return false;
    }

    public List<Position> findConnectedPositions(Position position) {
        List<Position> connectedPositionsOfPlayer = new ArrayList<>();
        for (Connection connection: connections) {
            if (!connection.getStartPositionId().equals(position.getId())) {
                continue;
            }
            connectedPositionsOfPlayer.add(findPositionById(connection.getEndPositionId()));
        }
        return connectedPositionsOfPlayer;
    }

    public List<Position> findConnectedPositionsWithPlayerId(Position position, int playerId) {
        List<Position> connectedPositionsOfPlayer = new ArrayList<>();
        for (Connection connection: connections) {
            if (!connection.getStartPositionId().equals(position.getId())) {
                continue;
            }

            Position connectedPosition = findPositionById(connection.getEndPositionId());
            if (connectedPosition.getPlayerId() == playerId) {
                connectedPositionsOfPlayer.add(connectedPosition);
            }
        }
        return connectedPositionsOfPlayer;
    }

    public List<Position> findEmptyConnectedPositions(Position position) {
        return findConnectedPositionsWithPlayerId(position, Player.EMPTY);
    }

    public Position findPositionById(String id) {
        if (id.startsWith(Position.OUT_OF_BOARD_PREFIX)) {
            return Position.instanceOutOfBoardForId(id);
        }
        return this.positionsMap.get(id);
    }

    public List<String> findAllPositionsIdsWithPlayerId(int playerId) {
        List<String> positionsForPlayerId = new ArrayList<>();
        for (Position position: positionsMap.values()) {
            if (position.getPlayerId() == playerId) {
                positionsForPlayerId.add(position.getId());
            }
        }
        return positionsForPlayerId;
    }

    public boolean hasAnyEmptyConnectedPositions(Position position) {
        return !findEmptyConnectedPositions(position).isEmpty();
    }

    public boolean hasAnyPositionsWithPlayerId(int playerId) {
        for (Position position: positionsMap.values()) {
            if (position.getPlayerId() == playerId) {
                return  true;
            }
        }
        return false;
    }
}
