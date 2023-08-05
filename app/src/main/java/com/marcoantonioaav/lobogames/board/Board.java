package com.marcoantonioaav.lobogames.board;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Line;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.List;

public abstract class Board {
    protected final Drawable image;
    protected final double paddingPercentage;
    protected final double positionRadiusScale;

    protected Board(Drawable image, double paddingPercentage, double positionRadiusScale) {
        this.image = image;
        this.paddingPercentage = paddingPercentage;
        this.positionRadiusScale = positionRadiusScale;
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
        int imageWidth = ((BitmapDrawable) image).getBitmap().getWidth();
        int imageHeight =  ((BitmapDrawable) image).getBitmap().getHeight();
        List<Position> positions = this.doGetPositions();
        for (Position position : positions) {
            Coordinate coord = new Coordinate(
                    (int) ((((float)position.getCoordinate().x() / imageWidth) * bounds.width()) + bounds.left),
                    (int) ((((float)position.getCoordinate().y() / imageHeight) * bounds.height()) + bounds.top)
            );
            position.setCoordinate(coord);
        }
        return positions;
    }

    public double getPositionRadiusScale() {
        return positionRadiusScale;
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
                (int) (canvasBounds.right * paddingPercentage),
                (int) (canvasBounds.bottom * paddingPercentage),
                (int) (canvasBounds.right * (1 - paddingPercentage)),
                (int) (canvasBounds.bottom * (1 - paddingPercentage))
        );
        this.image.setBounds(imageBounds);
    }
}
