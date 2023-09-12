package com.marcoantonioaav.lobogames.move;

import java.util.ArrayList;
import java.util.List;

public class GenericMove extends Move {
    private final List<GenericMovement> movements;

    public GenericMove(int playerId, List<GenericMovement> movements) {
        super(playerId);
        this.movements = movements;
    }

    @Override
    public List<Movement> getMovements() {
        return new ArrayList<>(this.movements);
    }

    public void addMovement(GenericMovement movement) {
        this.movements.add(movement);
    }
}
