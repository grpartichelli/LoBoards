package com.marcoantonioaav.lobogames.position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Connection {
    private final String startPositionId;
    private final String endPositionId;
    private final List<Coordinate> coordinatesBetween;

    public Connection(Position startPosition, Position endPosition) {
        // constructor used for straight lines
        this(startPosition, endPosition, Arrays.asList(startPosition.getCoordinate().copy(), endPosition.getCoordinate().copy()));
    }

    public Connection(Position startPosition, Position endPosition, List<Coordinate> coordinatesBetween) {
        this.startPositionId = startPosition.getId();
        this.endPositionId = endPosition.getId();
        this.coordinatesBetween = coordinatesBetween;
    }

    private Connection(String startPositionId, String endPositionId, List<Coordinate> coordinatesBetween) {
        this.startPositionId = startPositionId;
        this.endPositionId = endPositionId;
        this.coordinatesBetween = coordinatesBetween;
    }

    public String getStartPositionId() {
        return startPositionId;
    }

    public String getEndPositionId() {
        return endPositionId;
    }

    public List<Coordinate> getCoordinatesBetween() {
        return coordinatesBetween;
    }

    public Connection copy() {
        List<Coordinate> copiedCoordinates = new ArrayList<>();
        for (Coordinate coordinate: coordinatesBetween) {
            copiedCoordinates.add(coordinate.copy());
        }
        return new Connection(startPositionId, endPositionId, copiedCoordinates);
    }

    public Connection reverseCopy() {
        List<Coordinate> reversedCopiedCoordinates = new ArrayList<>();

        for (int i = coordinatesBetween.size() - 1; i >= 0; i--) {
            reversedCopiedCoordinates.add(coordinatesBetween.get(i).copy());
        }
        return new Connection(endPositionId, startPositionId, reversedCopiedCoordinates);
    }
}
