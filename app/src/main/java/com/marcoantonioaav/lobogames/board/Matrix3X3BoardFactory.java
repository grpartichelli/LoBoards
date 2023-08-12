package com.marcoantonioaav.lobogames.board;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;
import com.marcoantonioaav.lobogames.utils.TwoWayMap;

public class Matrix3X3BoardFactory {

    private Matrix3X3BoardFactory() {}

    private static final Drawable IMAGE = ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable._3x3);
    private static final double PADDING_PERCENTAGE = 0.08;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 14;
    public static final TwoWayMap<Coordinate, Position> POSITION_MAPPER = new TwoWayMap<>();

    static {
        POSITION_MAPPER.put(Coordinate.instanceOutOfBounds(), Position.instanceOutOfBoard());

        POSITION_MAPPER.put(new Coordinate(0, 2), new Position(new Coordinate(36, 50), "0x2"));
        POSITION_MAPPER.put(new Coordinate(1, 2), new Position(new Coordinate(327, 50), "1x2"));
        POSITION_MAPPER.put(new Coordinate(2, 2), new Position(new Coordinate(618, 50), "2x2"));

        POSITION_MAPPER.put(new Coordinate(0, 1), new Position(new Coordinate(36, 323), "0x1"));
        POSITION_MAPPER.put(new Coordinate(1, 1), new Position(new Coordinate(327, 323), "1x1"));
        POSITION_MAPPER.put(new Coordinate(2, 1), new Position(new Coordinate(618, 323), "2x1"));

        POSITION_MAPPER.put(new Coordinate(0, 0), new Position(new Coordinate(36, 596), "0x0"));
        POSITION_MAPPER.put(new Coordinate(1, 0), new Position(new Coordinate(327, 596), "1x0"));
        POSITION_MAPPER.put(new Coordinate(2, 0), new Position(new Coordinate(618, 596), "2x0"));

    } // TODO: Update labels

    public static MatrixBoard from(int[][] matrix) {
        if (matrix.length != 3) {
            throw new IllegalArgumentException("Expected matrix of width 3");
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

