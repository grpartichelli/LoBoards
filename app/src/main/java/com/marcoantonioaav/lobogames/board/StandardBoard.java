package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.List;

public class StandardBoard extends Board {

    private final List<Position> positions;
    private final List<Line> lines;

    public StandardBoard(Drawable image, List<Position> positions, List<Line> lines) {
        super(image);
        this.positions = positions;
        this.lines = lines;
    }

    @Override
    public void applyMove(Move move) {
        // TODO: Apply move
    }

    @Override
    public void applyMovement(Movement movement) {
        // TODO: Apply movement
    }

    @Override
    public int countPlayerPieces(int playerId) {
        int count = 0;
        for (Position position: this.positions) {
            if (position.getOccupiedBy() == playerId) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Board copy() {
        return new StandardBoard(this.image, new ArrayList<>(this.positions), new ArrayList<>(this.lines));
    }

    @Override
    public List<Position> doGetPositions() {
        return this.positions;
    }

    @Override
    public List<Line> doGetLines() {
        return this.lines;
    }
}
