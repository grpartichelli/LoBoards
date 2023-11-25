package com.marcoantonioaav.lobogames.move;

import java.util.ArrayList;
import java.util.List;

public class StandardMove extends Move {
    private final List<StandardMovement> movements;

    public StandardMove(int playerId, List<StandardMovement> movements) {
        super(playerId);
        this.movements = movements;
    }

    @Override
    public List<Movement> getMovements() {
        return new ArrayList<>(this.movements);
    }
}
