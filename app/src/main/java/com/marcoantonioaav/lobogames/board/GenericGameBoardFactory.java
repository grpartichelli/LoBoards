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

public class GenericGameBoardFactory {

    private GenericGameBoardFactory() {}

    private static final double PADDING_PERCENTAGE_HORIZONTAL = 0.05;
    private static final double PADDING_PERCENTAGE_VERTICAL = 0.25;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 20;
    private static final List<Connection> CONNECTIONS = new ArrayList<>();
    private static final List<Position> POSITIONS = new ArrayList<>();

    static {
        Position topLeft = new Position(new Coordinate(70, 70), "cima esquerda", 0);
        Position topRight = new Position(new Coordinate(1537,70), "cima direita", 1);
        Position center = new Position(new Coordinate(805,421), "centro", 2);
        Position bottomLeft = new Position(new Coordinate(70,773), "baixo esquerda", 3);
        Position bottomRight = new Position(new Coordinate(1537,770), "baixo direita", 4);

        POSITIONS.addAll(Arrays.asList(topLeft, topRight, center, bottomLeft, bottomRight));
        CONNECTIONS.addAll(Arrays.asList(
                        new Connection(topLeft, center), new Connection(topRight, center),
                        new Connection(bottomLeft, center), new Connection(bottomRight, center),
                        new Connection(topLeft, bottomLeft), new Connection(topRight, bottomRight),
                        new Connection(bottomLeft, bottomRight)
                )
        );
    }

    public static StandardBoard fromConfigFile(String filePath) {
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < POSITIONS.size(); i++) {
            Position position = POSITIONS.get(i).copy();
            positions.add(position);
        }

        List<Connection> connections = new ArrayList<>();
        for (Connection connection : CONNECTIONS) {
            connections.add(connection.copy());
            connections.add(connection.reverseCopy());
        }

        return new StandardBoard(
                ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable.pong_hau_ki),
                PADDING_PERCENTAGE_HORIZONTAL,
                PADDING_PERCENTAGE_VERTICAL,
                POSITION_RADIUS_SCALE,
                positions,
                connections
        );
    }
}
