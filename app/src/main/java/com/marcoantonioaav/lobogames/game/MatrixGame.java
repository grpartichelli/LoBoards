package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.position.Position;

public abstract class MatrixGame extends Game {
    protected MatrixBoard board;

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void setBoard(Board board) {
        if (board instanceof MatrixBoard){
            this.board = (MatrixBoard) board;
        }
    }

    @Override
    public Move getPlayerMove(Position startPosition, Position endPosition, int playerId) {
        int lastX = Movement.OUT_OF_BOARD;
        int lastY = Movement.OUT_OF_BOARD;
        int x = Movement.OUT_OF_BOARD;
        int y = Movement.OUT_OF_BOARD;

        if (!startPosition.getLabel().isEmpty()) {
            lastX = Integer.parseInt(startPosition.getLabel().split("x")[0]);
            lastY = Integer.parseInt(startPosition.getLabel().split("x")[1]);
        }
        if (!endPosition.getLabel().isEmpty()) {
            x = Integer.parseInt(endPosition.getLabel().split("x")[0]);
            y = Integer.parseInt(endPosition.getLabel().split("x")[1]);
        }
        return getPlayerMove(lastX, lastY, x, y, playerId);
    }

    public abstract Move getPlayerMove(int startX, int startY, int endX, int endY, int playerId);
}
