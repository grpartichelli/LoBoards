package com.gabrielpartichelli.loboards.board;

import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.move.Movement;
import com.gabrielpartichelli.loboards.position.Coordinate;
import com.gabrielpartichelli.loboards.position.Position;

import java.util.List;

public abstract class Board {
    protected final Drawable image;
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

    public Drawable getImageCopy()  {
        return image.getConstantState().newDrawable().mutate();
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

    public float getPositionRadius(int width) {
        return (float) (width * this.getPositionRadiusScale());
    }

    public float getPositionBorderRadius(int width) {
        // NOTE: slightly bigger than radius, shows as a ring around all positions
        return (float) (width * this.getPositionRadiusScale() * 1.09);
    }

    public float getSelectedPositionBorderRadius(int width) {
        // NOTE: slightly bigger than radius, shows as a ring around the selected position
        return (float) (width * this.getPositionRadiusScale() * 1.11);
    }
}
