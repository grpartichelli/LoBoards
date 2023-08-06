package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardBoard extends Board {

    private final Map<String, Position> positionsMap = new HashMap<>();
    private final List<Line> lines;

    public StandardBoard(
            Drawable image,
            double paddingPercentage,
            double positionRadiusScale,
            List<Position> positions,
            List<Line> lines
    ) {
        super(image, paddingPercentage, positionRadiusScale);
        for (Position position: positions) {
            this.positionsMap.put(position.getIdentifier(), position);
        }
        this.lines = lines;
    }

    @Override
    public void applyMove(Move move) {
        // TODO: Apply move
    }

    @Override
    public void applyMovement(Movement movement) {
        // TODO: Apply movement
    }


    @Override
    public Board copy() {
        return new StandardBoard(
                this.image,
                this.paddingPercentage,
                this.positionRadiusScale,
                new ArrayList<>(this.positionsMap.values()),
                new ArrayList<>(this.lines)
        );
    }

    @Override
    public int countPlayerPieces(int playerId) {
        int count = 0;
        for (Position position : this.getPositions()) {
            if (position.getOccupiedBy() == playerId) {
                count++;
            }
        }
        return count;
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
        positionsMap.put(position.getIdentifier(), position);
    }
}
