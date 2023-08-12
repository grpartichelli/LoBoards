package com.marcoantonioaav.lobogames.player;

import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.position.Position;

public class Human extends Player {
    private Position lastCursor = Position.instanceOutOfBoard();
    private Position cursor = Position.instanceOutOfBoard();

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
        this.cursor = position;
        ready = true;
    }
}
