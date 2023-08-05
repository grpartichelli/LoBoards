package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.position.Coordinate;
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
        Coordinate startCoordinate = this.board.mapPositionToMatrixCoordinate(startPosition);
        Coordinate endCoordinate = this.board.mapPositionToMatrixCoordinate(endPosition);
        return getPlayerMove(startCoordinate.x(), startCoordinate.y(), endCoordinate.x(), endCoordinate.y(), playerId);
    }

    public abstract Move getPlayerMove(int startX, int startY, int endX, int endY, int playerId);
}
