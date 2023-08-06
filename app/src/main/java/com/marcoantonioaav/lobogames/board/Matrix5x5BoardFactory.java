package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;
import com.marcoantonioaav.lobogames.utils.TwoWayMap;

public class Matrix5x5BoardFactory {

    private Matrix5x5BoardFactory(){}

    private static final Drawable IMAGE = ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable._5x5);
    private static final double PADDING_PERCENTAGE = 0.05;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 20;
    public static final TwoWayMap<Coordinate, Position> POSITION_MAPPER = new TwoWayMap<>();

    static {
        POSITION_MAPPER.put(Coordinate.instanceOutOfBounds(), Position.instanceOutOfBounds());

        POSITION_MAPPER.put(new Coordinate(0, 4), new Position(new Coordinate(34, 36), "0x4"));
        POSITION_MAPPER.put(new Coordinate(1, 4), new Position(new Coordinate(284, 36), "1x4"));
        POSITION_MAPPER.put(new Coordinate(2, 4), new Position(new Coordinate(514, 36), "2x4"));
        POSITION_MAPPER.put(new Coordinate(3, 4), new Position(new Coordinate(748, 36), "3x4"));
        POSITION_MAPPER.put(new Coordinate(4, 4), new Position(new Coordinate(991, 36), "4x4"));

        POSITION_MAPPER.put(new Coordinate(0, 3), new Position(new Coordinate(34, 256), "0x3"));
        POSITION_MAPPER.put(new Coordinate(1, 3), new Position(new Coordinate(284, 256), "1x3"));
        POSITION_MAPPER.put(new Coordinate(2, 3), new Position(new Coordinate(514, 256), "2x3"));
        POSITION_MAPPER.put(new Coordinate(3, 3), new Position(new Coordinate(748, 256), "3x3"));
        POSITION_MAPPER.put(new Coordinate(4, 3), new Position(new Coordinate(991, 256), "4x3"));

        POSITION_MAPPER.put(new Coordinate(0, 2), new Position(new Coordinate(34, 493), "0x2"));
        POSITION_MAPPER.put(new Coordinate(1, 2), new Position(new Coordinate(284, 493), "1x2"));
        POSITION_MAPPER.put(new Coordinate(2, 2), new Position(new Coordinate(514, 493), "2x2"));
        POSITION_MAPPER.put(new Coordinate(3, 2), new Position(new Coordinate(748, 493), "3x2"));
        POSITION_MAPPER.put(new Coordinate(4, 2), new Position(new Coordinate(991, 493), "4x2"));

        POSITION_MAPPER.put(new Coordinate(0, 1), new Position(new Coordinate(34, 717), "0x1"));
        POSITION_MAPPER.put(new Coordinate(1, 1), new Position(new Coordinate(284, 717), "1x1"));
        POSITION_MAPPER.put(new Coordinate(2, 1), new Position(new Coordinate(514, 717), "2x1"));
        POSITION_MAPPER.put(new Coordinate(3, 1), new Position(new Coordinate(748, 717), "3x1"));
        POSITION_MAPPER.put(new Coordinate(4, 1), new Position(new Coordinate(991, 717), "4x1"));

        POSITION_MAPPER.put(new Coordinate(0, 0), new Position(new Coordinate(34, 948), "0x0"));
        POSITION_MAPPER.put(new Coordinate(1, 0), new Position(new Coordinate(284, 948), "1x0"));
        POSITION_MAPPER.put(new Coordinate(2, 0), new Position(new Coordinate(514, 948), "2x0"));
        POSITION_MAPPER.put(new Coordinate(3, 0), new Position(new Coordinate(748, 948), "3x0"));
        POSITION_MAPPER.put(new Coordinate(4, 0), new Position(new Coordinate(991, 948), "4x0"));
    } // TODO: Update labels

    public static MatrixBoard from(int[][] matrix) {
        if (matrix.length != 5) {
            throw new IllegalArgumentException("Expected matrix of width 5");
        }
        return new MatrixBoard(
                IMAGE,
                PADDING_PERCENTAGE,
                POSITION_RADIUS_SCALE,
                matrix,
                POSITION_MAPPER
        );
    }
}

