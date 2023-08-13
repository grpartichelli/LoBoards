package com.marcoantonioaav.lobogames.move;

import com.marcoantonioaav.lobogames.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Move {
    private final int playerId;

    protected Move(int playerId) {
        this.playerId = playerId;
    }

    public abstract List<Movement> getMovements();
    public abstract void removeMomentByIndex(int index);

    @NotNull
    @Override
    public String toString() {
        String result = "";
        if(!getMovements().isEmpty()) {
            result = Player.getName(playerId) + ":";
            for(Movement movement : getMovements())
                result += " " + movement.toString();
        }
        return result;
    }

    public int getPlayerId() {
        return playerId;
    }
}
