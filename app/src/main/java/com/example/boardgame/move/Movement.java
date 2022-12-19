package com.example.boardgame.move;

import com.example.boardgame.player.Player;

public class Movement {
    public static final int OUT_OF_BOARD = -1;
    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public int piece;

    public Movement(int x, int y, int piece) {
        this.startX = OUT_OF_BOARD;
        this.startY = OUT_OF_BOARD;
        this.endX = x;
        this.endY = y;
        this.piece = piece;
    }

    public Movement(int startX, int startY, int endX, int endY, int piece) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.piece = piece;
    }

    public static String positionToString(int x, int y) {
        return (char)(x+65) + String.valueOf(y+1);
    }

    @Override
    public String toString() {
        if(startX == OUT_OF_BOARD && startY == OUT_OF_BOARD)
            return positionToString(endX, endY);
        if(endX == OUT_OF_BOARD && endY == OUT_OF_BOARD)
            return "";
        return positionToString(startX, startY) + " para " + positionToString(endX, endY);
    }

    public boolean isAdjacentInlineOpponentJump(int[][] board) {
        if(startX == OUT_OF_BOARD || startY == OUT_OF_BOARD || endX == OUT_OF_BOARD || endY == OUT_OF_BOARD)
            return false;
        return
                board[startX][startY] == piece &&
                        board[endX][endY] == Player.EMPTY &&
                        board[getRemovalFor(this).startX][getRemovalFor(this).startY] == Player.getOpponentOf(piece) &&
                        Math.abs(startX - endX) <= 2 &&
                        Math.abs(startY - endY) <= 2 &&
                        (startX % 2 == startY % 2 ||
                                Math.abs(startX - endX) + Math.abs(startY - endY) == 2);
    }

    public boolean isAdjacentInlineMovement(int[][] board) {
        if(startX == OUT_OF_BOARD || startY == OUT_OF_BOARD || endX == OUT_OF_BOARD || endY == OUT_OF_BOARD)
            return false;
        return
                board[startX][startY] == piece &&
                        board[endX][endY] == Player.EMPTY &&
                        Math.abs(startX - endX) <= 1 &&
                        Math.abs(startY - endY) <= 1 &&
                        (startX % 2 == startY % 2 ||
                                Math.abs(startX - endX) + Math.abs(startY - endY) == 1);
    }

    public static Movement getRemovalFor(Movement jump) {
        return new Movement(
                (jump.startX + jump.endX)/2,
                (jump.startY + jump.endY)/2,
                Movement.OUT_OF_BOARD,
                Movement.OUT_OF_BOARD,
                Player.getOpponentOf(jump.piece));
    }

    public boolean isInsertion(int[][] board) {
        if(endX == OUT_OF_BOARD || endY == OUT_OF_BOARD)
            return false;
        return startX == OUT_OF_BOARD && startY == OUT_OF_BOARD && board[endX][endY] == Player.EMPTY;
    }

    public boolean isRemoval(int[][] board) {
        if(startX == OUT_OF_BOARD || startY == OUT_OF_BOARD)
            return false;
        return endX == OUT_OF_BOARD && endY == OUT_OF_BOARD && board[startX][startY] == piece;
    }
}
