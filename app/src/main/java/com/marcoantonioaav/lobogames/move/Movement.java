package com.marcoantonioaav.lobogames.move;

import org.jetbrains.annotations.NotNull;

public abstract class Movement {
    public static final int OUT_OF_BOARD = -1;
    private final int playerId;

    protected Movement(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public abstract String getStartPositionId();
    public abstract String getEndPositionId();


    @NotNull
    @Override
    public String toString() {
        if (getStartPositionId().isEmpty()) {
            return getEndPositionId();
        }
        if (getEndPositionId().isEmpty()) {
            return "";
        }
        return getStartPositionId() + " para " + getEndPositionId();
    }
}
