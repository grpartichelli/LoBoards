package com.marcoantonioaav.lobogames.board;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;
import com.marcoantonioaav.lobogames.testconstants.Standard3X3Board;

import java.util.List;

public abstract class Board {
    protected Drawable image;

    protected Board(Drawable image) {
        this.image = image;
    }

    public abstract void applyMove(Move move);

    public abstract void applyMovement(Movement movement);

    public abstract int countPlayerPieces(int playerId);

    public abstract Board copy();

    public abstract List<Position> doGetPositions();

    public abstract List<Line> doGetLines();

    /**
     * Return boards positions
     * Before returning scales the positions of the board to fit new image bounds
     */
    public List<Position> getPositions() {
        Rect bounds = image.getBounds();
        List<Position> positions = this.doGetPositions();
        for (Position position : positions) {
            Coordinate coord = new Coordinate(
                    (int) ((((float)position.getCoordinate().x() / Standard3X3Board.IMAGE_WIDTH) * bounds.width()) + bounds.left),
                    (int) ((((float)position.getCoordinate().y() / Standard3X3Board.IMAGE_HEIGHT) * bounds.height()) + bounds.top)
            );
            position.setCoordinate(coord);
        }
        return positions;
    }

    public double getPositionRadiusScale() {
        return Standard3X3Board.POSITION_RADIUS_SCALE;
    }

    public List<Line> getLines() {
        return this.doGetLines();
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
                (int) (canvasBounds.right * Standard3X3Board.PADDING_PERCENTAGE),
                (int) (canvasBounds.bottom * Standard3X3Board.PADDING_PERCENTAGE),
                (int) (canvasBounds.right * (1 - Standard3X3Board.PADDING_PERCENTAGE)),
                (int) (canvasBounds.bottom * (1 - Standard3X3Board.PADDING_PERCENTAGE))
        );
        this.image.setBounds(imageBounds);
    }
}
