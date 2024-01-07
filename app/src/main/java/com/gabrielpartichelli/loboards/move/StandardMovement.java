package com.gabrielpartichelli.loboards.move;

import com.gabrielpartichelli.loboards.position.Position;

public class StandardMovement extends Movement {

    private final String startPositionId;
    private final String endPositionId;

    public StandardMovement(String startPositionId, String endPositionId, int playerId) {
        super(playerId);
        this.startPositionId = startPositionId;
        this.endPositionId= endPositionId;
    }

    public StandardMovement(Position startPosition, Position endPosition, int playerId) {
        super(playerId);
        this.startPositionId = startPosition.getId();
        this.endPositionId= endPosition.getId();
    }

    @Override
    public String getStartPositionId() {
        return startPositionId;
    }

    @Override
    public String getEndPositionId() {
        return endPositionId;
    }
}
