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
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Position;

public class BoardView extends View {
    private int cursorColor = Color.BLUE;
    private Position selectedPosition = Position.instanceOutOfBoard();
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
        drawPositions(canvas);
        if (currentMove != null && !currentMove.getMovements().isEmpty()) {
            this.board.applyMovement(currentMove.getMovements().get(0));
            currentMove.removeMomentByIndex(0);
            postInvalidateDelayed(300);
        }
    }

    public void draw() {
        invalidate();
    }

    public void drawMove(Move move) {
        this.selectedPosition = Position.instanceOutOfBoard();
        this.currentMove = move;
        invalidate();
    }

    public void drawSelectedPosition() {
        invalidate();
    }


    private void drawBoardImage(Canvas canvas) {
        board.scaleToLayoutParams(this.getLayoutParams());
        board.getImage().draw(canvas);
    }

    private void drawPositions(Canvas canvas) {
        float positionRadius = getPositionRadius();
        float positionBorderRadius = getPositionBorderRadius();
        float selectedPositionBorderRadius = getSelectedPositionBorderRadius();

        for (Position position: this.board.getPositions()) {
            if (position.getPlayerId() != Player.EMPTY) {
                if (selectedPosition.equals(position)) {
                    paint.setColor(cursorColor);
                    canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), selectedPositionBorderRadius, paint);
                } else {
                    paint.setColor(getPrimaryColor());
                    canvas.drawCircle(position.getCoordinate().x(), position.getCoordinate().y(), positionBorderRadius, paint);
                }
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

    public void setBoard(Board board) {
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            int x = (int) event.getX();
//            int y = (int) event.getY();
//
//            for (Position position : this.board.getPositions()) {
//                if (checkInsideCircle(position, x, y)) {
//                    this.selectedPosition = position;
//                    performClick();
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean checkInsideCircle(Position position, int xTouch, int yTouch) {
//        float  radius = getPositionRadius();
//        double centerX = position.getCoordinate().x();
//        double centerY = position.getCoordinate().y();
//        double distanceX = xTouch - centerX;
//        double distanceY = yTouch - centerY;
//        return (distanceX * distanceX) + (distanceY * distanceY) <= radius * radius;
//    }
//
//    @Override
//    public boolean performClick() {
//        super.performClick();
//        return true;
//    }
}
