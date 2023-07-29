package com.marcoantonioaav.lobogames.testconstants;

import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.Arrays;
import java.util.List;

public class TestConstants {
    private TestConstants() {
    }

    public static final int IMAGE_HEIGHT_3X3 = 655; // TODO: Can we obtain these somehow?
    public static final int IMAGE_WIDTH_3X3 = 655;
    public static final int IMAGE_ID_3X3 = R.drawable._3x3;
    public static final double PADDING_PERCENTAGE_3X3 = 0.05;
    public static final List<Position> POSITIONS_3X3 = Arrays.asList(
            new Position(new Coordinate(36, 50), "0x0"),
            new Position(new Coordinate(327, 50), "0x1"),
            new Position(new Coordinate(618, 50), "0x2"),
            new Position(new Coordinate(36, 323), "1x0"),
            new Position(new Coordinate(327, 323), "1x1"),
            new Position(new Coordinate(618, 323), "1x2"),
            new Position(new Coordinate(36, 596), "2x0"),
            new Position(new Coordinate(327, 596), "2x1"),
            new Position(new Coordinate(618, 596), "2x2")
    );

    public static final int IMAGE_ID_5X5 = R.drawable._3x3;

}

