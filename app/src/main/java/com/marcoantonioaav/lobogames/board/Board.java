package com.marcoantonioaav.lobogames.board;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;

public abstract class Board {
    private final int imageResourceId;

    protected Board(int imageResourceId) {
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
