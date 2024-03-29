package com.gabrielpartichelli.loboards.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.gabrielpartichelli.loboards.board.Board;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.move.Movement;
import com.gabrielpartichelli.loboards.player.Player;
import com.gabrielpartichelli.loboards.position.Coordinate;
import com.gabrielpartichelli.loboards.position.Position;

import java.util.ArrayList;
import java.util.List;

public class BoardView extends View {
    private int cursorColor = Color.BLUE;
    private Position selectedPosition = Position.instanceOutOfBoard();
    private int player1Color = Color.GREEN;
    private int player2Color = Color.RED;
    private final Paint paint = new Paint();
    private Board board;

    private final List<Movement> movements = new ArrayList<>();
    private int currentMovementIndex = 0;

    private int currentAnimationStep = 0;
    private static final int ANIMATION_DURATION_IN_MS = 300;
    private static final int ANIMATION_STEPS_TOTAL = 30;
    private Position animatingPosition = Position.instanceOutOfBoard();

    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoardImage(canvas);
        drawPositions(canvas);

        if (currentMovementIndex < movements.size()) {
            Movement movement = movements.get(currentMovementIndex);
            Position startPosition = board.findPositionById(movement.getStartPositionId());
            Position endPosition = board.findPositionById(movement.getEndPositionId());

            if (ANIMATION_STEPS_TOTAL <= currentAnimationStep) {
                this.board.applyMovement(movement);
                currentMovementIndex++;
                animatingPosition = Position.instanceOutOfBoard();
                currentAnimationStep = 0;
            } else {
                animatingPosition = calculateAnimatingPosition(startPosition, endPosition);
                currentAnimationStep++;
            }
            postInvalidateDelayed(ANIMATION_DURATION_IN_MS / ANIMATION_STEPS_TOTAL);
        }
    }

    private Position calculateAnimatingPosition(Position startPosition, Position endPosition) {
        List<Coordinate> coordinatesBetween = this.board.findCoordinatesBetween(startPosition, endPosition);
        enrichStartAndEndCoordinatesOutOfBoard(startPosition, endPosition, coordinatesBetween);
        double numberOfStepsPerCoordinatePair = (double) ANIMATION_STEPS_TOTAL / (coordinatesBetween.size() - 1);

        int currentCoordinatePairStartIndex = (int) (currentAnimationStep / numberOfStepsPerCoordinatePair);
        double currentCoordinatePairProgress = (currentAnimationStep % numberOfStepsPerCoordinatePair) / numberOfStepsPerCoordinatePair;

        Coordinate animatingCoordinate = calculateCoordinateBetweenCoordinatePairByProgress(
                coordinatesBetween.get(currentCoordinatePairStartIndex),
                coordinatesBetween.get(currentCoordinatePairStartIndex + 1),
                currentCoordinatePairProgress
        );

        Position newAnimatingPosition = new Position(
                animatingCoordinate,
                startPosition.isOutOfBoard() ? endPosition.getId() : startPosition.getId(),
                startPosition.getAccessibilityOrder()
        );
        newAnimatingPosition.setPlayerId(startPosition.getPlayerId());
        return newAnimatingPosition;
    }

    private void enrichStartAndEndCoordinatesOutOfBoard(Position startPosition, Position endPosition, List<Coordinate> coordinatesBetween) {


        Coordinate firstCoordinate = coordinatesBetween.get(0);
        if (firstCoordinate.isOutOfBoard()) {
            Coordinate newFirstCoordinate = resolveCoordinateOutOfBoard(startPosition);
            coordinatesBetween.set(0, newFirstCoordinate);
        }

        Coordinate lastCoordinate = coordinatesBetween.get(coordinatesBetween.size() - 1);
        if (lastCoordinate.isOutOfBoard()) {
            Coordinate newLastCoordinate = resolveCoordinateOutOfBoard(endPosition);
            coordinatesBetween.set(coordinatesBetween.size() - 1, newLastCoordinate);
        }
    }

    private Coordinate resolveCoordinateOutOfBoard(Position position) {
        int offsetWidth =  getWidth() / 3;
        boolean isTop = position.getPlayerId() == Player.PLAYER_2;
        int width;
        int height;
        int borderRadius = (int) this.board.getPositionBorderRadius(getWidth());
        if (isTop) {
            width = offsetWidth;
            height = borderRadius;
        } else {
            width = getWidth() - offsetWidth;
            height = getHeight() -borderRadius;
        }
        return new Coordinate(width, height);
    }

    private Coordinate calculateCoordinateBetweenCoordinatePairByProgress(
            Coordinate startCoordinate,
            Coordinate endCoordinate,
            double progress
    ) {
        double startX = startCoordinate.x();
        double endX = endCoordinate.x();
        int progressPositionX = (int) (startX + ((endX - startX) * progress));

        double startY = startCoordinate.y();
        double endY = endCoordinate.y();
        int progressPositionY = (int) (startY + ((endY - startY) * progress));

        return new Coordinate(progressPositionX, progressPositionY);
    }

    public void drawMove(Move move) {
        this.selectedPosition = Position.instanceOutOfBoard();
        this.movements.addAll(move.getMovements());
        invalidate();
    }

    public void drawSelectedPosition() {
        invalidate();
    }

    private void drawBoardImage(Canvas canvas) {
        board.getImage().draw(canvas);
    }

    private void drawPositions(Canvas canvas) {
        float positionRadius = this.board.getPositionRadius(getWidth());
        float positionBorderRadius = this.board.getPositionBorderRadius(getWidth());
        float selectedPositionBorderRadius = getSelectedPositionBorderRadius();


        List<Position> positionsWithAnimation = resolvePositionsWithAnimation();
        for (Position position : positionsWithAnimation) {
            if (position.getPlayerId() != Player.EMPTY) {
                // paint position border
                if (selectedPosition.equals(position)) {
                    paint.setColor(cursorColor);
                    canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), selectedPositionBorderRadius, paint);
                } else {
                    paint.setColor(Color.BLACK);
                    canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), positionBorderRadius, paint);
                }

                // paint position
                paint.setColor(getPlayerColor(position.getPlayerId()));
                canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), positionRadius, paint);
            }
        }
    }

    public List<Position> resolvePositionsWithAnimation() {
        List<Position> positions = this.board.getPositions();
        Position existingPosition = null;
        for (Position position : positions) {
            if (animatingPosition.equals(position)) {
                existingPosition = position;
            }

        }

        if (existingPosition != null) {
            positions.remove(existingPosition);
            positions.add(animatingPosition);
        }

        return positions;
    }

    public void resize(int size) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        setLayoutParams(layoutParams);
    }

    public int getPlayerColor(int playerId) {
        if (playerId == Player.PLAYER_1)
            return player1Color;
        if (playerId == Player.PLAYER_2)
            return player2Color;
        return Color.GRAY;
    }


    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
        invalidate();
    }

    public void reset() {
        animatingPosition = Position.instanceOutOfBoard();
        currentAnimationStep = 0;
        currentMovementIndex = 0;
        movements.clear();
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
    }

    public Position setSelectedPosition(Position selectedPosition) {
        return this.selectedPosition = selectedPosition;
    }

    public void setPlayer1Color(int player1Color) {
        this.player1Color = player1Color;
    }

    public void setPlayer2Color(int player2Color) {
        this.player2Color = player2Color;
    }

    public float getSelectedPositionBorderRadius() {
        return this.board.getSelectedPositionBorderRadius(getWidth());
    }
}
