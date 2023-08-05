package com.marcoantonioaav.lobogames.position;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Coordinate {
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
}
