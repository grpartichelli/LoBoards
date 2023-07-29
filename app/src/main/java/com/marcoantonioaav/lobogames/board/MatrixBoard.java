package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.List;

public class MatrixBoard extends Board {
    private final int[][] matrix;

    public MatrixBoard(int[][] matrix, Drawable image) {
        super(image, new ArrayList<>(), new ArrayList<>());
        this.matrix = matrix;
    }

    public MatrixBoard(int[][] matrix, Drawable image, List<Position> positions, List<Line> lines) {
        super(image, positions, lines);
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
        return new MatrixBoard(newBoardMatrix, this.image);
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
