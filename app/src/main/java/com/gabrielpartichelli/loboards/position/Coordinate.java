package com.gabrielpartichelli.loboards.position;

import androidx.annotation.Nullable;
import com.gabrielpartichelli.loboards.move.MatrixMovement;
import com.gabrielpartichelli.loboards.move.Movement;

import java.util.Objects;

public class Coordinate {
    public static Coordinate instanceOutOfBoard() {
        return new Coordinate(Movement.OUT_OF_BOARD, MatrixMovement.OUT_OF_BOARD);
    }

    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Coordinate)) {
            return  false;
        }
        return this.x == ((Coordinate) obj).x() && this.y == ((Coordinate) obj).y();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public Coordinate copy() {
        return new Coordinate(x, y);
    }

    public boolean isOutOfBoard() {
        return this.equals(instanceOutOfBoard());
    }
}
