package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;
import com.marcoantonioaav.lobogames.utils.TwoWayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenericCircularBoardFactory {

    private GenericCircularBoardFactory() {
    }

    private static final Drawable IMAGE = ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable.circular_board);
    private static final double PADDING_PERCENTAGE_HORIZONTAL = 0.05;
    private static final double PADDING_PERCENTAGE_VERTICAL = 0.08;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 18;
    private static final List<Position> positions = new ArrayList<>();

    static {
        Position topLeft = new Position(new Coordinate(188,153), "cima esquerda", 0);
        Position top = new Position(new Coordinate(485, 0), "cima", 1);
        Position topRight = new Position(new Coordinate(777, 152), "cima direita", 2);
        Position left = new Position(new Coordinate(46, 441), "esquerda", 3);
        Position center = new Position(new Coordinate(485, 438), "centro", 4);
        Position right = new Position(new Coordinate(894, 438), "direta", 5);
        Position bottomLeft = new Position(new Coordinate(183, 743), "baixo esquerda", 4);
        Position bottom = new Position(new Coordinate(484, 843), "baixo", 5);
        Position bottomRight = new Position(new Coordinate(775, 725), "baixo direita", 7);

        topLeft.addAllConnectedPositions(Arrays.asList(left, center, top));
        top.addAllConnectedPositions(Arrays.asList(topLeft, center, topRight));
        topRight.addAllConnectedPositions(Arrays.asList(top, center, right));

        left.addAllConnectedPositions(Arrays.asList(topLeft, center, bottomLeft));
        center.addAllConnectedPositions(Arrays.asList(left, right));
        right.addAllConnectedPositions(Arrays.asList(topRight, center, bottomRight));

        bottomLeft.addAllConnectedPositions(Arrays.asList(left, center, bottom));
        top.addAllConnectedPositions(Arrays.asList(bottomLeft, center, bottomRight));
        bottomRight.addAllConnectedPositions(Arrays.asList(bottom, center, right));

        positions.addAll(Arrays.asList(topLeft, top, topRight, left, center, right, bottomLeft, bottom, bottomRight));
    }

    public static GenericBoard from(List<Integer> initialPlayerIds) {
        if (initialPlayerIds.size() != positions.size()) {
            throw new IllegalArgumentException("Expected initial playerIds of size " + positions.size());
        }

        for (int i = 0; i < positions.size(); i++) {
            positions.get(i).setPlayerId(initialPlayerIds.get(i));
        }

        return new GenericBoard(
                IMAGE,
                PADDING_PERCENTAGE_HORIZONTAL,
                PADDING_PERCENTAGE_VERTICAL,
                POSITION_RADIUS_SCALE,
                positions,
                Collections.emptyList()
        );
    }
}