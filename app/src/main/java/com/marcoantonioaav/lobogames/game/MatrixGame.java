package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.board.MatrixPositionFieldsConverter;
import com.marcoantonioaav.lobogames.move.MatrixMove;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.position.Coordinate;

import java.util.ArrayList;
import java.util.List;

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
    public Move getPlayerMove(String startPositionId, String endPositionId, int playerId) {
        Coordinate startCoordinate = MatrixPositionFieldsConverter.resolveMatrixCoordinate(startPositionId);
        Coordinate endCoordinate = MatrixPositionFieldsConverter.resolveMatrixCoordinate(endPositionId);
        return getPlayerMatrixMove(startCoordinate.x(), startCoordinate.y(), endCoordinate.x(), endCoordinate.y(), playerId);
    }

    public abstract MatrixMove getPlayerMatrixMove(int startX, int startY, int endX, int endY, int playerId);

    @Override
    public boolean isLegalMove(Move move) {
        if (move instanceof MatrixMove) {
            return isLegalMatrixMove((MatrixMove) move);
        }
        return false;
    }

    public abstract boolean isLegalMatrixMove(MatrixMove move);

    @Override
    public List<Move> getLegalMoves(int playerId) {
        return new ArrayList<>(getLegalMatrixMoves(playerId));
    }

    public abstract List<MatrixMove> getLegalMatrixMoves(int playerId);
}
