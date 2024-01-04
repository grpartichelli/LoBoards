package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoards;
import com.marcoantonioaav.lobogames.position.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class Matrix5x5BoardFactory {

    private Matrix5x5BoardFactory() {
    }

    private static final Drawable IMAGE = ContextCompat.getDrawable(LoBoards.getAppContext(), R.drawable._5x5);
    private static final double PADDING_PERCENTAGE = 0.05;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 20;
    private static final Map<Coordinate, Coordinate> coordinateMapper = new HashMap<>();

    static {
        coordinateMapper.put(Coordinate.instanceOutOfBoard(), Coordinate.instanceOutOfBoard());

        coordinateMapper.put(new Coordinate(0, 4), new Coordinate(34, 36));
        coordinateMapper.put(new Coordinate(1, 4), new Coordinate(284, 36));
        coordinateMapper.put(new Coordinate(2, 4), new Coordinate(514, 36));
        coordinateMapper.put(new Coordinate(3, 4), new Coordinate(748, 36));
        coordinateMapper.put(new Coordinate(4, 4), new Coordinate(991, 36));

        coordinateMapper.put(new Coordinate(0, 3), new Coordinate(34, 256));
        coordinateMapper.put(new Coordinate(1, 3), new Coordinate(284, 256));
        coordinateMapper.put(new Coordinate(2, 3), new Coordinate(514, 256));
        coordinateMapper.put(new Coordinate(3, 3), new Coordinate(748, 256));
        coordinateMapper.put(new Coordinate(4, 3), new Coordinate(991, 256));

        coordinateMapper.put(new Coordinate(0, 2), new Coordinate(34, 493));
        coordinateMapper.put(new Coordinate(1, 2), new Coordinate(284, 493));
        coordinateMapper.put(new Coordinate(2, 2), new Coordinate(514, 493));
        coordinateMapper.put(new Coordinate(3, 2), new Coordinate(748, 493));
        coordinateMapper.put(new Coordinate(4, 2), new Coordinate(991, 493));

        coordinateMapper.put(new Coordinate(0, 1), new Coordinate(34, 717));
        coordinateMapper.put(new Coordinate(1, 1), new Coordinate(284, 717));
        coordinateMapper.put(new Coordinate(2, 1), new Coordinate(514, 717));
        coordinateMapper.put(new Coordinate(3, 1), new Coordinate(748, 717));
        coordinateMapper.put(new Coordinate(4, 1), new Coordinate(991, 717));

        coordinateMapper.put(new Coordinate(0, 0), new Coordinate(34, 948));
        coordinateMapper.put(new Coordinate(1, 0), new Coordinate(284, 948));
        coordinateMapper.put(new Coordinate(2, 0), new Coordinate(514, 948));
        coordinateMapper.put(new Coordinate(3, 0), new Coordinate(748, 948));
        coordinateMapper.put(new Coordinate(4, 0), new Coordinate(991, 948));

    }

    public static MatrixBoard from(int[][] matrix) {
        if (matrix.length != 5) {
            throw new IllegalArgumentException("Expected matrix of width 5");
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

