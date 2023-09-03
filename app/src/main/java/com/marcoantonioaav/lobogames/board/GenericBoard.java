package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericBoard extends Board {

    private final Map<String, Position> positionsMap = new HashMap<>();
    private final List<Line> lines;

    public GenericBoard(
            Drawable image,
            double paddingPercentage,
            double positionRadiusScale,
            List<Position> positions,
            List<Line> lines
    ) {
        this(image, paddingPercentage, paddingPercentage, positionRadiusScale, positions, lines);
    }

    public GenericBoard(
            Drawable image,
            double paddingPercentageHorizontal,
            double paddingPercentageVertical,
            double positionRadiusScale,
            List<Position> positions,
            List<Line> lines
    ) {
        super(image, paddingPercentageHorizontal, paddingPercentageVertical, positionRadiusScale);
        for (Position position: positions) {
            this.positionsMap.put(position.getId(), position);
        }
        this.positionsMap.put("", Position.instanceOutOfBoard());
        this.lines = lines;
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
        return new GenericBoard(
                this.image,
                this.paddingPercentageHorizontal,
                this.paddingPercentageVertical,
                this.positionRadiusScale,
                copiedPositions,
                new ArrayList<>(this.lines)
        );
    }

    @Override
    public List<Line> getLines() {
        return this.lines;
    }

    @Override
    public List<Position> getPositions() {
        return new ArrayList<>(this.positionsMap.values());
    }

    @Override
    public void updateCoordinate(Position position, Coordinate newCoordinate) {
        Position positionToUpdate = positionsMap.get(position.getId());
        positionToUpdate.setCoordinate(newCoordinate);
        positionsMap.put(positionToUpdate.getId(), positionToUpdate);
    }

    public boolean hasAnyEmptyConnectedPositions(Position position) {
        for (String connectedPositionId: position.getConnectedPositionsIds()) {
            Position connectedPosition = this.positionsMap.get(connectedPositionId);
            if (connectedPosition.getPlayerId() == Player.EMPTY) {
                return true;
            }
        }
        return false;
    }


    public List<Position> findConnectedPositionsForPlayerId(Position position, int playerId) {
        List<Position> connectedPositionsOfPlayer = new ArrayList<>();
        for (String connectedPositionId: position.getConnectedPositionsIds()) {
            Position connectedPosition = positionsMap.get(connectedPositionId);
            if (connectedPosition.getPlayerId() == playerId) {
                connectedPositionsOfPlayer.add(connectedPosition);
            }
        }
        return connectedPositionsOfPlayer;
    }

    public List<Position> findEmptyConnectedPositions(Position position) {
        return findConnectedPositionsForPlayerId(position, Player.EMPTY);
    }

    public Position findPositionById(String id) {
        return this.positionsMap.get(id);
    }
}
