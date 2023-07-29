package com.marcoantonioaav.lobogames.board;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;
import com.marcoantonioaav.lobogames.testconstants.TestConstants;

import java.util.ArrayList;
import java.util.List;

public abstract class Board {
    protected Drawable image;
    private final List<Position> positions;
    private final List<Line> lines;

    protected Board(Drawable image, List<Position> positions, List<Line> lines) {
        this.positions = positions;
        this.lines = lines;
        this.image = image;
        scalePositions();

    }

    public abstract void applyMove(Move move);

    public abstract void applyMovement(Movement movement);

    public abstract int countPlayerPieces(int playerId);

    public abstract Board copy();


    public List<Position> getPositions() {
        return positions;
    }

    public List<Line> getLines() {
        return lines;
    }

    public Drawable getImage() {
        return image;
    }

    /**
    * Makes image fit inside canvas bounds and adds padding
     **/
    public void fitImageToCanvas(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();
        Rect imageBounds = new Rect(
                (int) (canvasBounds.right * TestConstants.PADDING_PERCENTAGE_3X3),
                (int) (canvasBounds.bottom * TestConstants.PADDING_PERCENTAGE_3X3),
                (int) (canvasBounds.right * (1 - TestConstants.PADDING_PERCENTAGE_3X3)),
                (int) (canvasBounds.bottom * (1 - TestConstants.PADDING_PERCENTAGE_3X3))
        );
        this.image.setBounds(imageBounds);
        scalePositions();
    }

    /**
     * Scales the positions of the board to fit new image bounds
     */
    private void scalePositions() {
        Rect bounds = image.getBounds();
        for (Position position : positions) {
            Coordinate coord = new Coordinate(
                    (int) ((position.getCoordinate().x() / TestConstants.IMAGE_WIDTH_3X3) * bounds.width()) + bounds.left,
                    (int) ((position.getCoordinate().y() / TestConstants.IMAGE_HEIGHT_3X3) * bounds.height()) + bounds.top
            );
            position.setCoordinate(coord);
        }
    }
}
