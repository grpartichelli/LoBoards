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
        POSITION_MAPPER.put(Coordinate.instanceOutOfBounds(), Position.instanceOutOfBoard());

        POSITION_MAPPER.put(new Coordinate(0, 4), new Position(new Coordinate(34, 36), "5A",0));
        POSITION_MAPPER.put(new Coordinate(1, 4), new Position(new Coordinate(284, 36), "5B",1));
        POSITION_MAPPER.put(new Coordinate(2, 4), new Position(new Coordinate(514, 36), "5C",2));
        POSITION_MAPPER.put(new Coordinate(3, 4), new Position(new Coordinate(748, 36), "5D",3));
        POSITION_MAPPER.put(new Coordinate(4, 4), new Position(new Coordinate(991, 36), "5E",4));

        POSITION_MAPPER.put(new Coordinate(0, 3), new Position(new Coordinate(34, 256), "4A",5));
        POSITION_MAPPER.put(new Coordinate(1, 3), new Position(new Coordinate(284, 256), "4B",6));
        POSITION_MAPPER.put(new Coordinate(2, 3), new Position(new Coordinate(514, 256), "4C",7));
        POSITION_MAPPER.put(new Coordinate(3, 3), new Position(new Coordinate(748, 256), "4D",8));
        POSITION_MAPPER.put(new Coordinate(4, 3), new Position(new Coordinate(991, 256), "4E",9));

        POSITION_MAPPER.put(new Coordinate(0, 2), new Position(new Coordinate(34, 493), "3A", 10));
        POSITION_MAPPER.put(new Coordinate(1, 2), new Position(new Coordinate(284, 493), "3B", 11));
        POSITION_MAPPER.put(new Coordinate(2, 2), new Position(new Coordinate(514, 493), "3C", 12));
        POSITION_MAPPER.put(new Coordinate(3, 2), new Position(new Coordinate(748, 493), "3D", 13));
        POSITION_MAPPER.put(new Coordinate(4, 2), new Position(new Coordinate(991, 493), "3E",14));

        POSITION_MAPPER.put(new Coordinate(0, 1), new Position(new Coordinate(34, 717), "2A",15));
        POSITION_MAPPER.put(new Coordinate(1, 1), new Position(new Coordinate(284, 717), "2B",16));
        POSITION_MAPPER.put(new Coordinate(2, 1), new Position(new Coordinate(514, 717), "2C",17));
        POSITION_MAPPER.put(new Coordinate(3, 1), new Position(new Coordinate(748, 717), "2D",18));
        POSITION_MAPPER.put(new Coordinate(4, 1), new Position(new Coordinate(991, 717), "2E",19));

        POSITION_MAPPER.put(new Coordinate(0, 0), new Position(new Coordinate(34, 948), "1A", 20));
        POSITION_MAPPER.put(new Coordinate(1, 0), new Position(new Coordinate(284, 948), "1B", 21));
        POSITION_MAPPER.put(new Coordinate(2, 0), new Position(new Coordinate(514, 948), "1C", 22));
        POSITION_MAPPER.put(new Coordinate(3, 0), new Position(new Coordinate(748, 948), "1D",23));
        POSITION_MAPPER.put(new Coordinate(4, 0), new Position(new Coordinate(991, 948), "1E",24));
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
                POSITION_MAPPER
        );
    }
}

