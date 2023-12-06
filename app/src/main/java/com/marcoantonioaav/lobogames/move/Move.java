package com.marcoantonioaav.lobogames.move;

import java.util.List;

public abstract class Move {
    private final int playerId;

    protected Move(int playerId) {
        this.playerId = playerId;
    }

    public abstract List<Movement> getMovements();


    public int getPlayerId() {
        return playerId;
    }
}
