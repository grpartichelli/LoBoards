package com.marcoantonioaav.lobogames.position;

import java.util.List;

public class Line {
    private final Position startPosition;
    private final Position endPosition;
    private final List<Coordinate> coordinatesBetween;

    public Line(Position startPosition, Position endPosition, List<Coordinate> coordinatesBetween) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.coordinatesBetween = coordinatesBetween;
    }
}
