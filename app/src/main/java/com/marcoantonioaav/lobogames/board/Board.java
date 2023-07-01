package com.marcoantonioaav.lobogames.board;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;

public class Board {
    private int[][] matrix;
    private final int imageResourceId;

    public Board(int[][] matrix, int imageResourceId) {
        this.matrix = matrix;
        this.imageResourceId = imageResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void applyMove(Move move) {
        if (move == null) {
            return;
        }
        for (Movement movement : move.movements) {
            applyMovement(movement);
        }
    }

    public void applyMovement(Movement movement) {
        if (movement.startX != Movement.OUT_OF_BOARD && movement.startY != Movement.OUT_OF_BOARD) {
            this.matrix[movement.startX][movement.startY] = Player.EMPTY;
        }
        if (movement.endX != Movement.OUT_OF_BOARD && movement.endY != Movement.OUT_OF_BOARD) {
            this.matrix[movement.endX][movement.endY] = movement.piece;
        }
    }

    public Board copy() {
        int[][] newBoardMatrix = new int[this.matrix[0].length][this.matrix.length];
        for (int x = 0; x < getWidth(); x++) {
            System.arraycopy(this.matrix[x], 0, newBoardMatrix[x], 0, getHeight());
        }
        return new Board(
                newBoardMatrix,
                this.imageResourceId
        );
    }

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
        try {
            return this.matrix[0].length;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getWidth() {
        return this.matrix.length;
    }
}
