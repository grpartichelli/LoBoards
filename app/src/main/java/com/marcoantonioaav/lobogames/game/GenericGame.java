package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.GenericBoard;

public abstract class GenericGame extends Game {
    protected GenericBoard board;

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void setBoard(Board board) {
        if (board instanceof GenericBoard) {
            this.board = (GenericBoard) board;
        }
    }
}
