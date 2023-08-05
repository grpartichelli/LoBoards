package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import android.util.Pair;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatrixBoard extends Board {
    private final int[][] matrix;

    public MatrixBoard(
        Drawable image,
        double paddingPercentage,
        double positionRadiusScale,
        int[][] matrix
    ) {
        super(image, paddingPercentage, positionRadiusScale);
        this.matrix = matrix;
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
        return new MatrixBoard(this.image, this.paddingPercentage, this.positionRadiusScale, newBoardMatrix);
    }

    @Override
    public int countPlayerPieces(int playerId) {
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
    public List<Position> doGetPositions() {
        List<Position> positions = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Position position = Objects.requireNonNull(Matrix3X3BoardFactory.POSITIONS_3X3.get(Pair.create(x, y))).copy();
                position.setOccupiedBy(this.matrix[x][y]);
                positions.add(position);
            }
        }
        return positions;
    }

    @Override
    public List<Line> doGetLines() {
        return null;
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
