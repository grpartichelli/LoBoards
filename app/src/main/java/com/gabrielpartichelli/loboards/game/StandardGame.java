package com.gabrielpartichelli.loboards.game;

import com.gabrielpartichelli.loboards.board.Board;
import com.gabrielpartichelli.loboards.board.StandardBoard;

public abstract class StandardGame extends Game {
    protected StandardBoard board;

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void setBoard(Board board) {
        if (board instanceof StandardBoard) {
            this.board = (StandardBoard) board;
        }
    }
}
