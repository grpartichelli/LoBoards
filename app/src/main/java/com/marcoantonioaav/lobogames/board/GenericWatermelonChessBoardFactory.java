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

public class GenericWatermelonChessBoardFactory {

    private GenericWatermelonChessBoardFactory() {
    }

    private static final double PADDING_PERCENTAGE_HORIZONTAL = 0.04;
    private static final double PADDING_PERCENTAGE_VERTICAL = 0.04;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 20;
    private static final List<Connection> CONNECTIONS = new ArrayList<>();
    private static final List<Position> POSITIONS = new ArrayList<>();

    static {

        Position top = new Position(new Coordinate(0, 0), "cima", 0);
        Position topLeft = new Position(new Coordinate(0, 0), "cima esquerda", 1);
        Position topRight = new Position(new Coordinate(0, 0), "cima direita", 2);
        Position topCenter = new Position(new Coordinate(0, 0), "cima centro", 3);

        Position leftTop = new Position(new Coordinate(0, 0), "esquerda cima", 4);
        Position centerTop = new Position(new Coordinate(0, 0), "centro topo", 5);
        Position rightTop = new Position(new Coordinate(0, 0), "direita cima", 6);

        Position left = new Position(new Coordinate(0, 0), "esquerda", 7);
        Position leftCenter = new Position(new Coordinate(0, 0), "esquerda centro", 8);
        Position centerLeft = new Position(new Coordinate(0, 0), "centro esquerda", 9);

        Position center = new Position(new Coordinate(0, 0), "centro", 10);

        Position centerRight = new Position(new Coordinate(0, 0), "centro direita", 11);
        Position rightCenter = new Position(new Coordinate(0, 0), "direita centro", 12);
        Position right = new Position(new Coordinate(0, 0), "direita", 13);

        Position leftBottom = new Position(new Coordinate(0, 0), "esquerda baixo", 14);
        Position centerBottom = new Position(new Coordinate(0, 0), "centro baixo", 15);
        Position rightBottom = new Position(new Coordinate(0, 0), "direita baixo", 16);


        Position bottomCenter = new Position(new Coordinate(0, 0), "baixo centro", 17);
        Position bottomLeft = new Position(new Coordinate(0, 0), "baixo esquerda", 18);
        Position bottomRight = new Position(new Coordinate(0, 0), "baixo direita", 19);
        Position bottom = new Position(new Coordinate(0, 0), "baixo", 20);


        POSITIONS.addAll(
                Arrays.asList(
                        top, topLeft, topRight, topCenter,
                        leftTop, left, leftCenter, leftBottom,
                        centerTop, centerLeft, center, centerRight, centerBottom,
                        rightTop, rightCenter, right, rightBottom,
                        bottom, bottomLeft, bottomRight, bottomCenter
        ));


        CONNECTIONS.addAll(Arrays.asList(
                        new Connection(bottom, bottomCenter), new Connection(top, topCenter),
                        new Connection(left, leftCenter), new Connection(right, rightCenter),
                        new Connection(center, centerRight), new Connection(center, centerRight),
                        new Connection(center, centerBottom), new Connection(center, centerTop),

                        new Connection(topLeft, top),
                        new Connection(topLeft, topCenter),
                        new Connection(topRight, top),
                        new Connection(topRight, topCenter),

                        new Connection(bottomLeft, bottom),
                        new Connection(bottomLeft, bottomCenter),
                        new Connection(bottomRight, bottom),
                        new Connection(bottomRight, bottomCenter),

                        new Connection(centerTop, centerLeft),
                        new Connection(centerTop, centerRight),
                        new Connection(centerBottom, centerLeft),
                        new Connection(centerBottom, centerRight),

                        new Connection(leftTop, left),
                        new Connection(leftTop, leftCenter),
                        new Connection(leftBottom, left),
                        new Connection(leftBottom, leftCenter),

                        new Connection(rightTop, right),
                        new Connection(rightTop, rightCenter),
                        new Connection(rightBottom, right),
                        new Connection(rightBottom, rightCenter)
        ));
    }

    public static GenericBoard from(List<Integer> initialPlayerIds) {
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

        return new GenericBoard(
                ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable.watermelon_chess),
                PADDING_PERCENTAGE_HORIZONTAL,
                PADDING_PERCENTAGE_VERTICAL,
                POSITION_RADIUS_SCALE,
                positions,
                connections
        );
    }
}