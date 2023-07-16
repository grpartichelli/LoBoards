package com.marcoantonioaav.lobogames.board;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class Board {
    private final int imageResourceId;
    private final List<Position> positions = new ArrayList<>();

    protected Board(int imageResourceId, List<Position> positions) {
        this.imageResourceId = imageResourceId;
    }

    public abstract void applyMove(Move move);

    public abstract void applyMovement(Movement movement);

    public abstract int countPlayerPieces(int playerId);

    public abstract Board copy();

    public int getImageResourceId() {
        return imageResourceId;
    }
}
