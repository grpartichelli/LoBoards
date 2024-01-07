package com.gabrielpartichelli.loboards.player;

import com.gabrielpartichelli.loboards.game.Game;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.position.Position;

public class Human extends Player {
    private String lastCursor = Position.instanceOutOfBoard().getId();
    private String cursor = Position.instanceOutOfBoard().getId();

    private boolean ready = false;

    public Human(int id) {
        super(id);
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public Move getMove(Game game) {
        ready = false;
        return game.getPlayerMove(lastCursor, cursor, getId());
    }

    public void setCursor(Position position) {
        this.lastCursor = this.cursor;
        this.cursor = position.getId();
        ready = true;
    }

    public void clearCursor() {
        this.lastCursor = Position.instanceOutOfBoard().getId();;
        this.cursor = Position.instanceOutOfBoard().getId();
    }
}
