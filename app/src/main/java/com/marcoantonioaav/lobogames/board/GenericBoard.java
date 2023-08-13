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
        super(image, paddingPercentage, positionRadiusScale);
        for (Position position: positions) {
            this.positionsMap.put(position.getLabel(), position);
        }
        this.lines = lines;
    }


    @Override
    public void applyMovement(Movement movement) {
        if (movement.getStartPosition().isOutOfBoard()) {
            Position currentStartPosition = this.positionsMap.get(movement.getStartPosition());
            currentStartPosition.setPlayerId(Player.EMPTY);
        }
    }


    @Override
    public Board copy() {
        return new GenericBoard(
                this.image,
                this.paddingPercentage,
                this.positionRadiusScale,
                new ArrayList<>(this.positionsMap.values()),
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
    public void updateCoordinateOfPosition(Position position, Coordinate newCoordinate) {
        position.setCoordinate(newCoordinate);
        positionsMap.put(position.getLabel(), position);
    }

    @Override
    public void updatePlayerIdOfPosition(Position position, int playerId) {
        position.setPlayerId(playerId);
        positionsMap.put(position.getLabel(), position);
    }
}
