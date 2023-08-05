package com.marcoantonioaav.lobogames.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

public class BoardView extends View {
    private int cursorColor = Color.BLUE;
    private Position selectedPosition = new Position(new Coordinate(Movement.OUT_OF_BOARD, Movement.OUT_OF_BOARD), "Out of Board");
    private int player1Color = Color.GREEN;
    private int player2Color = Color.RED;
    private final Paint paint = new Paint();

    private Board board;
    private Move currentMove;


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
        drawPieces(canvas);
        if (currentMove != null && !currentMove.movements.isEmpty()) {
            this.board.applyMovement(currentMove.movements.get(0));
            currentMove.movements.remove(0);
            postInvalidateDelayed(300);
        }
    }

    public void draw() {
        invalidate();
    }

    // TODO: Is this necessary?
    public void drawSelectedPosition() {
        invalidate();
    }

    public void drawMove(Move move) {
        this.selectedPosition = new Position(new Coordinate(Movement.OUT_OF_BOARD, Movement.OUT_OF_BOARD), "Out of Board");
        this.currentMove = move;
        invalidate();
    }


    public void drawBoardImage(Canvas canvas) {
        board.fitImageToCanvas(canvas);
        board.getImage().draw(canvas);
    }

    private void drawPieces(Canvas canvas) {
        float radius = getPositionRadius();

        for (Position position: this.board.getPositions()) {
            if (position.getOccupiedBy() != Player.EMPTY) {
                paint.setColor(getPlayerColor(position.getOccupiedBy()));
                canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), radius, paint);
            }
        }
    }

    public void resizeToScreenSize() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;
        setLayoutParams(layoutParams);
    }

    public float getPositionRadius() {
        return (float) (getWidth() * this.board.getPositionRadiusScale());
    }

    public int getPlayerColor(int playerId) {
        if (playerId == Player.PLAYER_1)
            return player1Color;
        if (playerId == Player.PLAYER_2)
            return player2Color;
        return Color.GRAY;
    }

    public Position getSelectedPosition() {
        return this.selectedPosition;
    }

    // TODO: Add dark mode board?
    private int getPrimaryColor() {
        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES)
            return Color.WHITE;
        return Color.BLACK;
    }

    public void setBoard(MatrixBoard board) {
        this.board = board;
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
    }

    public void setPlayer1Color(int player1Color) {
        this.player1Color = player1Color;
    }

    public void setPlayer2Color(int player2Color) {
        this.player2Color = player2Color;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            for (Position position : this.board.getPositions()) {
                if (checkInsideCircle(position, x, y)) {
                    this.selectedPosition = position;
                }
            }
            performClick();
            return true;
        }
        return false;
    }

    private boolean checkInsideCircle(Position position, int xTouch, int yTouch) {
        float  radius = getPositionRadius();
        double centerX = position.getCoordinate().x();
        double centerY = position.getCoordinate().y();
        double distanceX = xTouch - centerX;
        double distanceY = yTouch - centerY;
        return (distanceX * distanceX) + (distanceY * distanceY) <= radius * radius;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
