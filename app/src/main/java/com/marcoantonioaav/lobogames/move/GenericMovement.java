package com.marcoantonioaav.lobogames.move;

import com.marcoantonioaav.lobogames.position.Position;

public class GenericMovement extends Movement {

    private final Position startPosition;
    private final Position endPosition;

    public GenericMovement(Position startPosition, Position endPosition, int playerId) {
        super(playerId);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override
    public Position getStartPosition() {
        return startPosition;
    }

    @Override
    public Position getEndPosition() {
        return endPosition;
    }
}
