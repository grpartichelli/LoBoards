package com.marcoantonioaav.lobogames.board;

import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenericCircularBoardFactory {

    private GenericCircularBoardFactory() {
    }

    private static final double PADDING_PERCENTAGE_HORIZONTAL = 0.05;
    private static final double PADDING_PERCENTAGE_VERTICAL = 0.08;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 18;
    private static final List<Position> POSITIONS = new ArrayList<>();

    public static final String TOP_LEFT = "cima esquerda";
    public static final String TOP = "cima";
    public static final String TOP_RIGHT= "cima direita";

    public static final String LEFT = "esquerda";
    public static final String CENTER = "centro";
    public static final String RIGHT = "direta";

    public static final String BOTTOM_LEFT = "baixo esquerda";
    public static final String BOTTOM = "baixo";
    public static final String BOTTOM_RIGHT = "baixo direita";

    static {
        Position topLeft = new Position(new Coordinate(188,153), TOP_LEFT, 0);
        Position top = new Position(new Coordinate(485, 50), TOP, 1);
        Position topRight = new Position(new Coordinate(777, 152), TOP_RIGHT, 2);
        Position left = new Position(new Coordinate(46, 441), LEFT, 3);
        Position center = new Position(new Coordinate(485, 438), CENTER, 4);
        Position right = new Position(new Coordinate(894, 438), RIGHT, 5);
        Position bottomLeft = new Position(new Coordinate(183, 743), BOTTOM_LEFT, 4);
        Position bottom = new Position(new Coordinate(484, 843), BOTTOM, 5);
        Position bottomRight = new Position(new Coordinate(775, 725), BOTTOM_RIGHT, 7);

        topLeft.addAllConnectedPositions(Arrays.asList(left, center, top));
        top.addAllConnectedPositions(Arrays.asList(topLeft, center, topRight));
        topRight.addAllConnectedPositions(Arrays.asList(top, center, right));

        left.addAllConnectedPositions(Arrays.asList(topLeft, center, bottomLeft));
        center.addAllConnectedPositions(Arrays.asList(topLeft, left, topRight, bottomLeft, right, bottomRight, bottom, top));
        right.addAllConnectedPositions(Arrays.asList(topRight, center, bottomRight));

        bottomLeft.addAllConnectedPositions(Arrays.asList(left, center, bottom));
        bottom.addAllConnectedPositions(Arrays.asList(bottomLeft, center, bottomRight));
        bottomRight.addAllConnectedPositions(Arrays.asList(bottom, center, right));

        POSITIONS.addAll(Arrays.asList(topLeft, top, topRight, left, center, right, bottomLeft, bottom, bottomRight));
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

        return new GenericBoard(
                ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable.circular_board),
                PADDING_PERCENTAGE_HORIZONTAL,
                PADDING_PERCENTAGE_VERTICAL,
                POSITION_RADIUS_SCALE,
                positions,
                Collections.emptyList()
        );
    }
}