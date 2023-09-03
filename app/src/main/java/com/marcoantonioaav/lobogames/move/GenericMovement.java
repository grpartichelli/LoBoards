package com.marcoantonioaav.lobogames.move;

import com.marcoantonioaav.lobogames.position.Position;

public class GenericMovement extends Movement {

    private final String startPositionId;
    private final String endPositionId;

    public GenericMovement(String startPositionId, String endPositionId, int playerId) {
        super(playerId);
        this.startPositionId = startPositionId;
        this.endPositionId= endPositionId;
    }

    public GenericMovement(Position startPosition, Position endPosition, int playerId) {
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
