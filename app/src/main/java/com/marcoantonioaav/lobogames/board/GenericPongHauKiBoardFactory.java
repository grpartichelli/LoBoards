package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenericPongHauKiBoardFactory {

    private GenericPongHauKiBoardFactory(){}

    private static final Drawable IMAGE = ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable.pong_hau_ki);
    private static final double PADDING_PERCENTAGE_HORIZONTAL = 0.05;
    private static final double PADDING_PERCENTAGE_VERTICAL = 0.25;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 20;
    private static final List<Position> POSITIONS = new ArrayList<>();

    static {
        Position topLeft = new Position(new Coordinate(70, 70), "cima esquerda", 0);
        Position topRight = new Position(new Coordinate(1537,70), "cima direita", 1);
        Position center = new Position(new Coordinate(805,421), "centro", 2);
        Position bottomLeft = new Position(new Coordinate(70,773), "baixo esquerda", 3);
        Position bottomRight = new Position(new Coordinate(1537,770), "baixo direita", 4);

        topLeft.addAllConnectedPositions(Arrays.asList(bottomLeft, center));
        topRight.addAllConnectedPositions(Arrays.asList(bottomRight, center));
        center.addAllConnectedPositions(Arrays.asList(topLeft, topRight, bottomLeft, bottomRight));
        bottomLeft.addAllConnectedPositions(Arrays.asList(topLeft, center, bottomRight));
        bottomRight.addAllConnectedPositions(Arrays.asList(topRight, center, bottomLeft));

        POSITIONS.addAll(Arrays.asList(topLeft, topRight, center, bottomLeft, bottomRight));
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
                IMAGE,
                PADDING_PERCENTAGE_HORIZONTAL,
                PADDING_PERCENTAGE_VERTICAL,
                POSITION_RADIUS_SCALE,
                positions,
                Collections.emptyList()
        );
    }
}