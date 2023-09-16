package com.marcoantonioaav.lobogames.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.move.GenericMovement;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

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
    private Position movingPosition = Position.instanceOutOfBoard();
    private int i = 0;
    private static final int MAX_I = 30;

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

        // TODO: Refactor this
        if (currentMovementIndex < movements.size()) {
            Movement movement = movements.get(currentMovementIndex);

            Position startPosition = board.findPositionById(movement.getStartPositionId());
            Position endPosition = board.findPositionById(movement.getEndPositionId());

            double progress = i / (double) MAX_I;
            double startX = startPosition.getCoordinate().x();
            double endX = endPosition.getCoordinate().x();
            int movingPositionX = (int) (startX + ((endX - startX) * progress));

            double startY = startPosition.getCoordinate().y();
            double endY = endPosition.getCoordinate().y();
            int movingPositionY = (int) (startY + ((endY - startY) * progress));

            movingPosition = new Position(
                    new Coordinate(movingPositionX, movingPositionY),
                    startPosition.getId(),
                    startPosition.getAccessibilityOrder()
            );
            movingPosition.setPlayerId(startPosition.getPlayerId());

            i++;
            if (i >= MAX_I) {
                movingPosition = Position.instanceOutOfBoard();
                this.board.applyMovement(movement);
                currentMovementIndex++;
                i = 0;
            }
            postInvalidateDelayed(10);
        }
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
        float positionRadius = getPositionRadius();
        float positionBorderRadius = getPositionBorderRadius();
        float selectedPositionBorderRadius = getSelectedPositionBorderRadius();

        for (Position position: this.board.getPositions()) {
            position = movingPosition.equals(position) ? movingPosition : position;

            if (position.getPlayerId() != Player.EMPTY) {
                // paint position border
                if (selectedPosition.equals(position)) {
                    paint.setColor(cursorColor);
                    canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), selectedPositionBorderRadius, paint);
                } else {
                    paint.setColor(getPrimaryColor());
                    canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), positionBorderRadius, paint);
                }

                // paint position
                paint.setColor(getPlayerColor(position.getPlayerId()));
                canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), positionRadius, paint);
            }
        }
    }

    public void resizeToScreenSize() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;
        setLayoutParams(layoutParams);
    }

    public float getPositionRadius() {
        return (float) (getWidth() * this.board.getPositionRadiusScale());
    }

    public float getPositionBorderRadius() {
        // NOTE: slightly bigger than radius, shows as a ring around all positions
        return (float) (getWidth() * this.board.getPositionRadiusScale() * 1.08);
    }

    public float getSelectedPositionBorderRadius() {
        // NOTE: slightly bigger than radius, shows as a ring around the selected position
        return (float) (getWidth() * this.board.getPositionRadiusScale() * 1.11);
    }

    public int getPlayerColor(int playerId) {
        if (playerId == Player.PLAYER_1)
            return player1Color;
        if (playerId == Player.PLAYER_2)
            return player2Color;
        return Color.GRAY;
    }


    // TODO: Add dark mode board?
    private int getPrimaryColor() {
        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES)
            return Color.WHITE;
        return Color.BLACK;
    }

    public void setBoard(Board board) {
        this.board = board;
        invalidate();
    }

    public void reset() {
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
}
