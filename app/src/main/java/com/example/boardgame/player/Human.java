package com.example.boardgame.player;

import com.example.boardgame.move.Move;
import com.example.boardgame.move.Movement;
import com.example.boardgame.game.Game;

public class Human extends Player {
    private int lastCursorX = Movement.OUT_OF_BOARD;
    private int lastCursorY = Movement.OUT_OF_BOARD;
    private int cursorX = Movement.OUT_OF_BOARD;
    private int cursorY = Movement.OUT_OF_BOARD;

    private boolean ready = false;

    public Human(int id) {
        super(id);
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public Move getMove(Game game, int[][] board) {
        ready = false;
        return game.getPlayerMove(lastCursorX, lastCursorY, cursorX, cursorY, board, getId());
    }

    public void setCursor(int x, int y) {
        this.lastCursorX = this.cursorX;
        this.lastCursorY = this.cursorY;
        this.cursorX = x;
        this.cursorY = y;
        ready = true;
    }
}
