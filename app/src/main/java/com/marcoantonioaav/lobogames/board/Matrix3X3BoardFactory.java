package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class Matrix3X3BoardFactory {

    private Matrix3X3BoardFactory() {
    }

    private static final Drawable IMAGE = ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable._3x3);
    private static final double PADDING_PERCENTAGE = 0.08;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 14;
    private static final Map<Coordinate, Coordinate> coordinateMapper = new HashMap<>();

    
    static {
        coordinateMapper.put(Coordinate.instanceOutOfBoard(), Coordinate.instanceOutOfBoard());

        coordinateMapper.put(new Coordinate(0, 2), new Coordinate(36, 50));
        coordinateMapper.put(new Coordinate(1, 2), new Coordinate(327, 50));
        coordinateMapper.put(new Coordinate(2, 2), new Coordinate(618, 50));

        coordinateMapper.put(new Coordinate(0, 1), new Coordinate(36, 323));
        coordinateMapper.put(new Coordinate(1, 1), new Coordinate(327, 323));
        coordinateMapper.put(new Coordinate(2, 1), new Coordinate(618, 323));

        coordinateMapper.put(new Coordinate(0, 0), new Coordinate(36, 596));
        coordinateMapper.put(new Coordinate(1, 0), new Coordinate(327, 596));
        coordinateMapper.put(new Coordinate(2, 0), new Coordinate(618, 596));

    }

    public static MatrixBoard from(int[][] matrix) {
        if (matrix.length != 3) {
            throw new IllegalArgumentException("Expected matrix of width 3");
        }
        return new MatrixBoard(
                IMAGE,
                PADDING_PERCENTAGE,
                POSITION_RADIUS_SCALE,
                matrix,
                coordinateMapper
        );
    }
}

