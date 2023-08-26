package com.marcoantonioaav.lobogames.move;

import com.marcoantonioaav.lobogames.position.Position;
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

    public abstract Position getStartPosition();
    public abstract Position getEndPosition();

    @NotNull
    @Override
    public String toString() {
        if (getStartPosition().isOutOfBoard()) {
            return getEndPosition().getLabel();
        }
        if (getEndPosition().isOutOfBoard()) {
            return "";
        }
        return getStartPosition().getLabel() + " para " + getEndPosition().getLabel();
    }
}
