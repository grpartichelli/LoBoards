package com.marcoantonioaav.lobogames.move;

import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.board.MatrixPositionFieldsConverter;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Coordinate;

public class MatrixMovement extends Movement {
    // NOTE: Used to map between matrix X and Y coordinates and board image positions
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;


    public MatrixMovement(int x, int y, int playerId) {
        super(playerId);
        this.startX = OUT_OF_BOARD;
        this.startY = OUT_OF_BOARD;
        this.endX = x;
        this.endY = y;
    }

    public MatrixMovement(int startX, int startY, int endX, int endY, int playerId) {
        super(playerId);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public String getStartPositionId() {
        return MatrixPositionFieldsConverter.resolvePositionId(new Coordinate(startX, startY));
    }

    @Override
    public String getEndPositionId() {
        return MatrixPositionFieldsConverter.resolvePositionId(new Coordinate(endX, endY));
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public boolean isAdjacentInlineOpponentJump(MatrixBoard board) {
        if (startX == OUT_OF_BOARD || startY == OUT_OF_BOARD || endX == OUT_OF_BOARD || endY == OUT_OF_BOARD)
            return false;
        return
                board.valueAt(startX, startY) == getPlayerId() &&
                        board.valueAt(endX, endY) == Player.EMPTY &&
                        board.valueAt(getRemovalFor(this).startX, getRemovalFor(this).startY) == Player.getOpponentOf(getPlayerId()) &&
                        Math.abs(startX - endX) <= 2 &&
                        Math.abs(startY - endY) <= 2 &&
                        (startX % 2 == startY % 2 ||
                                Math.abs(startX - endX) + Math.abs(startY - endY) == 2);
    }

    public boolean isAdjacentInlineMovement(MatrixBoard board) {
        if (startX == OUT_OF_BOARD || startY == OUT_OF_BOARD || endX == OUT_OF_BOARD || endY == OUT_OF_BOARD)
            return false;
        return
                board.valueAt(startX, startY) == getPlayerId() &&
                        board.valueAt(endX, endY) == Player.EMPTY &&
                        Math.abs(startX - endX) <= 1 &&
                        Math.abs(startY - endY) <= 1 &&
                        (startX % 2 == startY % 2 ||
                                Math.abs(startX - endX) + Math.abs(startY - endY) == 1);
    }

    public boolean isDiagonalMovement(MatrixBoard board) {
        if (startX == OUT_OF_BOARD || startY == OUT_OF_BOARD || endX == OUT_OF_BOARD || endY == OUT_OF_BOARD)
            return false;

        return
                board.valueAt(startX, startY) == getPlayerId() &&
                        board.valueAt(endX, endY) == Player.EMPTY &&
                        Math.abs(startX - endX) == 1 &&
                        Math.abs(startY - endY) == 1;
    }

    public static MatrixMovement getRemovalFor(MatrixMovement jump) {
        return new MatrixMovement(
                (jump.startX + jump.endX) / 2,
                (jump.startY + jump.endY) / 2,
                Movement.OUT_OF_BOARD,
                Movement.OUT_OF_BOARD,
                Player.getOpponentOf(jump.getPlayerId()));
    }

    public boolean isInsertion(MatrixBoard board) {
        if (endX == OUT_OF_BOARD || endY == OUT_OF_BOARD)
            return false;
        return startX == OUT_OF_BOARD && startY == OUT_OF_BOARD && board.valueAt(endX, endY) == Player.EMPTY;
    }

    public boolean isRemoval(MatrixBoard board) {
        if (startX == OUT_OF_BOARD || startY == OUT_OF_BOARD)
            return false;
        return endX == OUT_OF_BOARD && endY == OUT_OF_BOARD && board.valueAt(startX, startY) == getPlayerId();
    }
}
