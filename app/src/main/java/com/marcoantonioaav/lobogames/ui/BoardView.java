package com.marcoantonioaav.lobogames.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;

public class BoardView extends View {
    private int selectedX = Movement.OUT_OF_BOARD;
    private int selectedY = Movement.OUT_OF_BOARD;
    private int player1Color = Color.GREEN;
    private int player2Color = Color.RED;
    private int cursorColor = Color.BLUE;
    private final Paint paint = new Paint();

    private Board board;
    private Drawable boardImage;
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

    public void setBoard(Board board) {
        this.board = board;
        this.boardImage = ResourcesCompat.getDrawable(getResources(), board.getImageResourceId(), null);
    }

    public void resizeToScreenSize() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;
        setLayoutParams(layoutParams);
    }

    public void draw() {
        invalidate();
    }

    public void drawSelectedPosition(int selectedX, int selectedY) {
        this.selectedX = selectedX;
        this.selectedY = selectedY;
        invalidate();
    }

    public void drawMove(Move move) {
        this.selectedX = Movement.OUT_OF_BOARD;
        this.selectedY = Movement.OUT_OF_BOARD;
        this.currentMove = move;
        invalidate();
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

    public void drawBoardImage(Canvas canvas) {
        double boardPaddingPercentage = 0.05;
        Rect clipBounds = canvas.getClipBounds();
        Rect imageBounds = new Rect(
                (int) (clipBounds.right * boardPaddingPercentage),
                (int) (clipBounds.bottom * boardPaddingPercentage),
                (int) (clipBounds.right * (1 - boardPaddingPercentage)),
                (int) (clipBounds.bottom * (1 - boardPaddingPercentage))
        );
        boardImage.setBounds(imageBounds);
        boardImage.draw(canvas);
    }

    private void drawPieces(Canvas canvas) {
        int cx, cy, radius;
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++) {
                cx = getCrescentPosition(x, getWidth(), this.board.getWidth());
                cy = getDecrescentPosition(y, getHeight(), this.board.getHeight());
                if (this.board.getMatrix()[x][y] != Player.EMPTY) {
                    if (selectedX == x && selectedY == y) {
                        paint.setColor(cursorColor);
                        radius = getPieceRadius() + (getPieceBorderWidth() * 2);
                    } else {
                        paint.setColor(getPrimaryColor());
                        radius = getPieceRadius() + getPieceBorderWidth();
                    }
                    canvas.drawCircle(cx, cy, radius, paint);
                }
                if (this.board.getMatrix()[x][y] == Player.EMPTY)
                    radius = getPieceRadius() / 2;
                else
                    radius = getPieceRadius();
                paint.setColor(getPlayerColor(this.board.getMatrix()[x][y]));
                canvas.drawCircle(cx, cy, radius, paint);
            }
    }

    private int getCrescentPosition(int index, int totalSize, int totalQuantity) {
        return (totalSize / (totalQuantity + 1)) * (index + 1);
    }

    private int getDecrescentPosition(int index, int totalSize, int totalQuantity) {
        return (totalSize / (totalQuantity + 1)) * (totalQuantity - index);
    }

    private int getPieceBorderWidth() {
        return Math.max(getPieceRadius() / 45, 2);
    }

    private int getPieceRadius() {
        return getWidth() / (this.board.getWidth() * 4);
    }

    public int getPlayerColor(int playerId) {
        if (playerId == Player.PLAYER_1)
            return player1Color;
        if (playerId == Player.PLAYER_2)
            return player2Color;
        return Color.GRAY;
    }

    public void setPlayer1Color(int player1Color) {
        this.player1Color = player1Color;
    }

    public void setPlayer2Color(int player2Color) {
        this.player2Color = player2Color;
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
    }

    private int getPrimaryColor() {
        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES)
            return Color.WHITE;
        return Color.BLACK;
    }
}
