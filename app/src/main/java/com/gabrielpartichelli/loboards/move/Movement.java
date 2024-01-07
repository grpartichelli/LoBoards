package com.gabrielpartichelli.loboards.move;

import com.gabrielpartichelli.loboards.position.Position;
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
        if (isIdOutOfBoard(getStartPositionId())) {
            return getEndPositionId();
        }
        if (isIdOutOfBoard(getEndPositionId())) {
            return "";
        }
        return getStartPositionId() + " para " + getEndPositionId();
    }

    private boolean isIdOutOfBoard(String id) {
        return id.startsWith(Position.OUT_OF_BOARD_PREFIX);
    }
}
