package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import android.util.Pair;
import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.HashMap;
import java.util.Map;

public class Matrix3X3BoardFactory {

    private Matrix3X3BoardFactory() {
    }

    private static final int IMAGE_HEIGHT = 655;
    private static final int IMAGE_WIDTH = 655;
    private static final Drawable IMAGE = ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable._3x3);
    private static final double PADDING_PERCENTAGE = 0.08;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 14;
    public static final Map<Pair<Integer, Integer>, Position> POSITIONS_3X3 = new HashMap<>();

    static {
        POSITIONS_3X3.put(Pair.create(0, 2), new Position(new Coordinate(36, 50), "0x2"));
        POSITIONS_3X3.put(Pair.create(1, 2), new Position(new Coordinate(327, 50), "1x2"));
        POSITIONS_3X3.put(Pair.create(2, 2), new Position(new Coordinate(618, 50), "2x2"));
        POSITIONS_3X3.put(Pair.create(0, 1), new Position(new Coordinate(36, 323), "0x1"));
        POSITIONS_3X3.put(Pair.create(1, 1), new Position(new Coordinate(327, 323), "1x1"));
        POSITIONS_3X3.put(Pair.create(2, 1), new Position(new Coordinate(618, 323), "2x1"));
        POSITIONS_3X3.put(Pair.create(0, 0), new Position(new Coordinate(36, 596), "0x0"));
        POSITIONS_3X3.put(Pair.create(1, 0), new Position(new Coordinate(327, 596), "1x0"));
        POSITIONS_3X3.put(Pair.create(2, 0), new Position(new Coordinate(618, 596), "2x0"));
    } // NOTE: Flipped Y axis
    // TODO: Update labels

    public static MatrixBoard from(int[][] matrix) {
        if (matrix.length != 3) {
            throw new IllegalArgumentException("Expected matrix of width 3");
        }
        return new MatrixBoard(
                IMAGE,
                PADDING_PERCENTAGE,
                POSITION_RADIUS_SCALE,
                matrix
        );
    }
}

