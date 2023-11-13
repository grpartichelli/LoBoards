package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.StandardBoard;

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
