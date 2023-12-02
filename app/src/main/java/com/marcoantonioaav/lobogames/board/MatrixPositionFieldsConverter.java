package com.marcoantonioaav.lobogames.board;

import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

public class MatrixPositionFieldsConverter {

    public static Coordinate resolveMatrixCoordinate(String positionId) {
        if (positionId.startsWith(Position.OUT_OF_BOARD_PREFIX)) {
            return Coordinate.instanceOutOfBoard();
        }
        int x = positionId.charAt(0) - 65;
        int y = (positionId.charAt(1) - '0') - 1;
        return new Coordinate(x, y);
    }

    public static String resolvePositionId(Coordinate coordinate) {
        if (coordinate.equals(Coordinate.instanceOutOfBoard())) {
            return Position.OUT_OF_BOARD_PREFIX + "0";
        }
        return (char)(coordinate.x() + 65) + String.valueOf(coordinate.y() + 1);
    }

    public static int resolveAccessibilityOrder(Coordinate coordinate) {
        // NOTE: This simply makes it order by y first then x.
        return -(coordinate.y() * 10 + coordinate.x());
    }
}
