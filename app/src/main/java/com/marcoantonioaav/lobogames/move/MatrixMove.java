package com.marcoantonioaav.lobogames.move;

import com.marcoantonioaav.lobogames.board.MatrixBoard;

import java.util.ArrayList;
import java.util.List;

public class MatrixMove extends Move {
    private final List<MatrixMovement> movements;

    public MatrixMove(int x, int y, int playerId) {
        super(playerId);
        movements = new ArrayList<>();
        movements.add(new MatrixMovement(x, y, playerId));
    }

    public MatrixMove(int startX, int startY, int endX, int endY, int playerId) {
        super(playerId);
        movements = new ArrayList<>();
        movements.add(new MatrixMovement(startX, startY, endX, endY, playerId));
    }

    public MatrixMove(List<MatrixMovement> movements, int playerId) {
        super(playerId);
        this.movements = movements;
    }

    @Override
    public List<Movement> getMovements() {
        return new ArrayList<>(this.movements);
    }

    public List<MatrixMovement> getMatrixMovements() {
        return movements;
    }

    @Override
    public void removeMomentByIndex(int index) {
        this.movements.remove(index);
    }

    public int getRemovalCount(MatrixBoard board) {
        int count = 0;
        for(MatrixMovement movement : movements)
            if(movement.isRemoval(board))
                count++;
        return count;
    }

    public void addMovement(MatrixMovement movement) {
        this.movements.add(movement);
    }
}
