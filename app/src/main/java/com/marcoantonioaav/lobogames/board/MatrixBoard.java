package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MatrixBoard extends Board {
    private final int[][] matrix;
    private final Map<Coordinate, Coordinate> coordinateMapper;

    public MatrixBoard(
            Drawable image,
            double paddingPercentage,
            double positionRadiusScale,
            int[][] matrix,
            Map<Coordinate, Coordinate> coordinateMapper
    ) {
        this(image, paddingPercentage, paddingPercentage, positionRadiusScale, matrix, coordinateMapper);
    }

    public MatrixBoard(
            Drawable image,
            double paddingPercentageHorizontal,
            double paddingPercentageVertical,
            double positionRadiusScale,
            int[][] matrix,
            Map<Coordinate, Coordinate> coordinateMapper
    ) {
        super(image, paddingPercentageHorizontal, paddingPercentageVertical, positionRadiusScale);
        this.matrix = matrix;
        this.coordinateMapper = coordinateMapper;
    }

    @Override
    public void applyMovement(Movement movement) {
        if (movement == null) {
            return;
        }

        Coordinate start = MatrixPositionFieldsConverter.resolveMatrixCoordinate(movement.getStartPositionId());
        Coordinate end = MatrixPositionFieldsConverter.resolveMatrixCoordinate(movement.getEndPositionId());

        if (!start.equals(Coordinate.instanceOutOfBoard())) {
            this.matrix[start.x()][start.y()] = Player.EMPTY;
        }

        if (!end.equals(Coordinate.instanceOutOfBoard())) {
            this.matrix[end.x()][end.y()] = movement.getPlayerId();
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
                this.paddingPercentageHorizontal,
                this.paddingPercentageVertical,
                this.positionRadiusScale,
                newBoardMatrix,
                coordinateMapper
        );
    }

    @Override
    public int countPlayerPieces(int playerId) {
        // NOTE: Overridden for performance
        int count = 0;
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (this.matrix[x][y] == playerId) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                positions.add(createPositionFromMatrixCoordinate(new Coordinate(x,y)));
            }
        }
        return positions;
    }

    private Position createPositionFromMatrixCoordinate(Coordinate coord) {
        if (coord.equals(Coordinate.instanceOutOfBoard())) {
            return Position.instanceOutOfBoard();
        }
        Position position = new Position(
                coordinateMapper.get(coord),
                MatrixPositionFieldsConverter.resolvePositionId(coord),
                MatrixPositionFieldsConverter.resolveAccessibilityOrder(coord)
        );
        position.setPlayerId(this.matrix[coord.x()][coord.y()]);
        return  position;
    }

    public void updateCoordinate(Position position, Coordinate newCoordinate) {
        Coordinate matrixCoord = MatrixPositionFieldsConverter.resolveMatrixCoordinate(position.getId());
        coordinateMapper.put(matrixCoord, newCoordinate);
    }


    @Override
    public void updateCoordinatesBetween(double imageWidth, double imageHeight, double left, double top, double right, double bottom) {
        // no-op, considers that all connections on matrix boards are straight lines
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

    @Override
    public List<Coordinate> findCoordinatesBetween(Position startPosition, Position endPosition) {
        // considers that all connections on matrix boards are straight lines
        return new ArrayList<>(Arrays.asList(startPosition.getCoordinate(), endPosition.getCoordinate()));
    }

    @Override
    public Position findPositionById(String positionId) {
        return createPositionFromMatrixCoordinate(MatrixPositionFieldsConverter.resolveMatrixCoordinate(positionId));
    }
}
