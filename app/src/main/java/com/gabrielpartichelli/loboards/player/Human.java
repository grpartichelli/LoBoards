package com.gabrielpartichelli.loboards.player;

import com.gabrielpartichelli.loboards.game.Game;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.position.Position;

public class Human extends Player {
    private String lastPositionId = Position.instanceOutOfBoard().getId();
    private String currentPositionId = Position.instanceOutOfBoard().getId();

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
        return game.getPlayerMove(lastPositionId, currentPositionId, getId());
    }

    public void setCurrentPositionId(Position position) {
        this.lastPositionId = this.currentPositionId;
        this.currentPositionId = position.getId();
        ready = true;
    }

    public void clearCursor() {
        this.lastPositionId = Position.instanceOutOfBoard().getId();;
        this.currentPositionId = Position.instanceOutOfBoard().getId();
    }
}
