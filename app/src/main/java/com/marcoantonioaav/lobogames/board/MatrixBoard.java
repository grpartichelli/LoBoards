package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;
import com.marcoantonioaav.lobogames.utils.TwoWayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatrixBoard extends Board {
    private final int[][] matrix;
    // NOTE: Used to map between matrix X and Y coordinates and board image positions
    private final TwoWayMap<Coordinate, Position> positionMapper;

    public MatrixBoard(
            Drawable image,
            double paddingPercentage,
            double positionRadiusScale,
            int[][] matrix,
            TwoWayMap<Coordinate, Position> positionMapper
    ) {
        super(image, paddingPercentage, positionRadiusScale);
        this.matrix = matrix;
        this.positionMapper = positionMapper;
    }

    @Override
    public void applyMove(Move move) {
        if (move == null) {
            return;
        }
        for (Movement movement : move.movements) {
            applyMovement(movement);
        }
    }

    @Override
    public void applyMovement(Movement movement) {
        if (movement == null) {
            return;
        }
        if (movement.startX != Movement.OUT_OF_BOARD && movement.startY != Movement.OUT_OF_BOARD) {
            this.matrix[movement.startX][movement.startY] = Player.EMPTY;
        }
        if (movement.endX != Movement.OUT_OF_BOARD && movement.endY != Movement.OUT_OF_BOARD) {
            this.matrix[movement.endX][movement.endY] = movement.piece;
        }
    }

    @Override
    public MatrixBoard copy() {
        int[][] newBoardMatrix = new int[this.matrix[0].length][this.matrix.length];
        for (int x = 0; x < getWidth(); x++) {
            System.arraycopy(this.matrix[x], 0, newBoardMatrix[x], 0, getHeight());
        }
        return new MatrixBoard(
                this.image,
                this.paddingPercentage,
                this.positionRadiusScale,
                newBoardMatrix,
                positionMapper
        );
    }

    @Override
    public List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Position position = mapMatrixCoordinatesToPosition(x, y);
                position.setOccupiedBy(this.matrix[x][y]);
                positions.add(position);
            }
        }
        return positions;
    }

    @Override
    public List<Line> getLines() {
        return Collections.emptyList();
    }

    public Coordinate mapPositionToMatrixCoordinate(Position position) {
        return positionMapper.getBackward(position);
    }

    public Position mapMatrixCoordinatesToPosition(int x, int y) {
        return positionMapper.getForward(new Coordinate(x, y));
    }

    @Override
    public void updateCoordinateOfPosition(Position position, Coordinate newCoordinate) {
        Coordinate currentMatrixCoordinate = this.positionMapper.getBackward(position);
        position.setCoordinate(newCoordinate);
        this.positionMapper.put(currentMatrixCoordinate, position);
    }

    public boolean isOnLimits(int x, int y) {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    public int getHeight() {
        return this.matrix[0].length;
    }

    public int getWidth() {
        return this.matrix.length;
    }

    public int valueAt(int x, int y) {
        return this.matrix[x][y];
    }
}
