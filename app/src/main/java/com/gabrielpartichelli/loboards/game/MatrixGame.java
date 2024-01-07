package com.gabrielpartichelli.loboards.game;

import com.gabrielpartichelli.loboards.board.Board;
import com.gabrielpartichelli.loboards.board.MatrixBoard;
import com.gabrielpartichelli.loboards.board.MatrixPositionFieldsConverter;
import com.gabrielpartichelli.loboards.move.MatrixMove;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.position.Coordinate;

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

    @Override
    public int getMaxPlayerPositionsCount() {
        return this.board.getWidth() * this.board.getWidth();
    }

    @Override
    public String getTextUrl() {
        return "";
    }

    @Override
    public String getVideoUrl() {
        return "";
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
