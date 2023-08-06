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

    public abstract List<Line> getLines();

    public abstract List<Position> getPositions();

    public double getPositionRadiusScale() {
        return positionRadiusScale;
    }

    public Drawable getImage() {
        return image;
    }

    /**
     * Adds padding and scales the image and positions making it fit inside the canvas bounds
     * This is needed because the image size changes according to the users screen
     **/
    public void scaleToCanvas(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();
        Rect newImageBounds = new Rect(
                (int) (canvasBounds.right * paddingPercentage),
                (int) (canvasBounds.bottom * paddingPercentage),
                (int) (canvasBounds.right * (1 - paddingPercentage)),
                (int) (canvasBounds.bottom * (1 - paddingPercentage))
        );

        if (newImageBounds.equals(this.image.getBounds())) {
            return;
        }

        this.image.setBounds(newImageBounds);
        int imageWidth = ((BitmapDrawable) image).getBitmap().getWidth();
        int imageHeight = ((BitmapDrawable) image).getBitmap().getHeight();
        for (Position position : this.getPositions()) {
            Coordinate coordinate = new Coordinate(
                    (int) ((((float) position.getCoordinate().x() / imageWidth) * newImageBounds.width()) + newImageBounds.left),
                    (int) ((((float) position.getCoordinate().y() / imageHeight) * newImageBounds.height()) + newImageBounds.top)
            );
            updateCoordinateOfPosition(position, coordinate);
        }
    }

    public abstract void updateCoordinateOfPosition(Position position, Coordinate newCoordinate);
}
