package com.marcoantonioaav.lobogames.board;

import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.List;

public abstract class Board {
    protected Drawable image; // TODO: turn final
    protected final double paddingPercentageHorizontal;
    protected final double paddingPercentageVertical;
    protected final double positionRadiusScale;

    protected Board(Drawable image, double paddingPercentage, double positionRadiusScale) {
        this(image, paddingPercentage, paddingPercentage, positionRadiusScale);
    }

    protected Board(
            Drawable image,
            double paddingPercentageHorizontal,
            double paddingPercentageVertical,
            double positionRadiusScale) {
        this.image = image;
        this.paddingPercentageHorizontal = paddingPercentageHorizontal;
        this.paddingPercentageVertical = paddingPercentageVertical;
        this.positionRadiusScale = positionRadiusScale;
    }


    public void applyMove(Move move) {
        if (move == null) {
            return;
        }
        for (Movement movement : move.getMovements()) {
            applyMovement(movement);
        }
    }

    public abstract void applyMovement(Movement movement);

    public int countPlayerPieces(int playerId) {
        int count = 0;
        for (Position position : this.getPositions()) {
            if (position.getPlayerId() == playerId) {
                count++;
            }
        }
        return count;
    }

    public abstract Board copy();

    public abstract List<Position> getPositions();

    public double getPositionRadiusScale() {
        return positionRadiusScale;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    /**
     * Adds padding and scales the image and positions making it fit inside the layout params bounds
     * This is needed because the image size changes according to the users screen
     **/
    public void scaleToLayoutParams(ViewGroup.LayoutParams layoutParams) {
        double left = layoutParams.width * paddingPercentageHorizontal;
        double top = layoutParams.height * paddingPercentageVertical;
        double right = layoutParams.width * (1 - paddingPercentageHorizontal);
        double bottom = layoutParams.height * (1 - paddingPercentageVertical);
        Rect newImageBounds = new Rect((int) left, (int) top, (int) right, (int) bottom);

        if (newImageBounds.equals(this.image.getBounds())) {
            return;
        }

        this.image.setBounds(newImageBounds);
        double imageWidth = ((BitmapDrawable) image).getBitmap().getWidth();
        double imageHeight = ((BitmapDrawable) image).getBitmap().getHeight();
        for (Position position : this.getPositions()) {
            double currentX  = position.getCoordinate().x();
            double currentY = position.getCoordinate().y();
            int newX = (int) (((currentX / imageWidth) * (right - left)) + left);
            int newY = (int) (((currentY / imageHeight) * (bottom - top)) + top);
            updateCoordinate(position, new Coordinate(newX, newY));
        }
        updateCoordinatesBetween(imageWidth, imageHeight, left, top, right, bottom);
    }

    public abstract void updateCoordinate(Position position, Coordinate newCoordinate);

    public abstract void updateCoordinatesBetween(double imageWidth, double imageHeight, double left, double top, double right, double bottom);

    public abstract List<Coordinate> findCoordinatesBetween(Position startPosition, Position endPosition);
    public abstract Position findPositionById(String positionId);
}
