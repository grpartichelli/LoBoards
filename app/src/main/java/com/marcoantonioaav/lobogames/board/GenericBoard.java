package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
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
            this.positionsMap.put(position.getLabel(), position);
        }
        this.lines = lines;
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
    public void updateCoordinateOfPosition(Position position, Coordinate newCoordinate) {
        Position positionToUpdate = positionsMap.get(position.getLabel());
        positionToUpdate.setCoordinate(newCoordinate);
        positionsMap.put(positionToUpdate.getLabel(), positionToUpdate);
    }

    @Override
    public void updatePlayerIdOfPosition(Position position, int playerId) {
        Position positionToUpdate = positionsMap.get(position.getLabel());
        positionToUpdate.setPlayerId(playerId);
        positionsMap.put(positionToUpdate.getLabel(), positionToUpdate);
    }
}
