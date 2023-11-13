package com.marcoantonioaav.lobogames.board;

import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Connection;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CircularBoardFactory {

    private CircularBoardFactory() {
    }

    private static final double PADDING_PERCENTAGE_HORIZONTAL = 0.05;
    private static final double PADDING_PERCENTAGE_VERTICAL = 0.08;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 18;
    private static final List<Connection> CONNECTIONS = new ArrayList<>();
    private static final List<Position> POSITIONS = new ArrayList<>();

    public static final String TOP_LEFT = "cima esquerda";
    public static final String TOP = "cima";
    public static final String TOP_RIGHT = "cima direita";

    public static final String LEFT = "esquerda";
    public static final String CENTER = "centro";
    public static final String RIGHT = "direta";

    public static final String BOTTOM_LEFT = "baixo esquerda";
    public static final String BOTTOM = "baixo";
    public static final String BOTTOM_RIGHT = "baixo direita";

    static {
        Position topLeft = new Position(new Coordinate(188, 153), TOP_LEFT, 0);
        Position top = new Position(new Coordinate(485, 50), TOP, 1);
        Position topRight = new Position(new Coordinate(777, 152), TOP_RIGHT, 2);
        Position left = new Position(new Coordinate(46, 441), LEFT, 3);
        Position center = new Position(new Coordinate(485, 438), CENTER, 4);
        Position right = new Position(new Coordinate(894, 438), RIGHT, 5);
        Position bottomLeft = new Position(new Coordinate(183, 743), BOTTOM_LEFT, 4);
        Position bottom = new Position(new Coordinate(484, 843), BOTTOM, 5);
        Position bottomRight = new Position(new Coordinate(775, 725), BOTTOM_RIGHT, 7);

        POSITIONS.addAll(Arrays.asList(topLeft, top, topRight, left, center, right, bottomLeft, bottom, bottomRight));

        CONNECTIONS.addAll(Arrays.asList(
            new Connection(topLeft, left), new Connection(topLeft, top),
            new Connection(topRight, top), new Connection(topRight, right),

            new Connection(bottomLeft, left), new Connection(bottomLeft, bottom),
            new Connection(bottomRight, bottom), new Connection(bottomRight, right),

            new Connection(left, center), new Connection(right, center),
            new Connection(bottom, center), new Connection(bottomLeft, center), new Connection(bottomRight, center),
            new Connection(top, center), new Connection(topLeft, center), new Connection(topRight, center)
            )
        );
    }

    public static StandardBoard from(List<Integer> initialPlayerIds) {
        if (initialPlayerIds.size() != POSITIONS.size()) {
            throw new IllegalArgumentException("Expected initial playerIds of size " + POSITIONS.size());
        }

        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < POSITIONS.size(); i++) {
            Position position = POSITIONS.get(i).copy();
            position.setPlayerId(initialPlayerIds.get(i));
            positions.add(position);
        }

        List<Connection> connections = new ArrayList<>();
        for (Connection connection : CONNECTIONS) {
            connections.add(connection.copy());
            connections.add(connection.reverseCopy());
        }

        return new StandardBoard(
                ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable.circular_board),
                PADDING_PERCENTAGE_HORIZONTAL,
                PADDING_PERCENTAGE_VERTICAL,
                POSITION_RADIUS_SCALE,
                positions,
                connections
        );
    }
}