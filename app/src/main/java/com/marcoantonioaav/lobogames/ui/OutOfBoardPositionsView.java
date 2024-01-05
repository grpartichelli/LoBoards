package com.marcoantonioaav.lobogames.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.marcoantonioaav.lobogames.application.LoBoards;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Coordinate;

import java.util.concurrent.Callable;

// TODO: ADD CEMETERY
public class OutOfBoardPositionsView extends View {

    Paint paint = new Paint();
    Board board;
    int playerColor;
    int cursorColor;
    boolean isTop = false;
    RelativeLayout buttonsLayout;
    BoardView boardView;
    boolean isSelected = false;
    Button button;
    int positionsCount = 0;
    int maxPlayerPositionsCount = 0;
    boolean isPositionEnabled = true;
    Callable<Void> clickCallable;
    Button firstAccessibilityButton;
    Button lastAccessibilityButton;

    public OutOfBoardPositionsView(Context context) {
        super(context);
    }

    public OutOfBoardPositionsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OutOfBoardPositionsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Coordinate coordinate = resolveStoppedCoordinate();
        drawPosition(coordinate, canvas, isSelected);
        setupButton();
    }

    private void drawPosition(Coordinate coordinate, Canvas canvas, boolean isSelected) {
        float positionRadius = this.board.getPositionRadius(getWidth());
        float positionBorderRadius = this.board.getPositionBorderRadius(getWidth());
        float selectedPositionBorderRadius = this.board.getSelectedPositionBorderRadius(getWidth());

        // TODO: Consider big buttons
        // paint position border
        if (isSelected) {
            paint.setColor(cursorColor);
            canvas.drawCircle(coordinate.x(), coordinate.y(), selectedPositionBorderRadius, paint);
        } else {
            paint.setColor(Color.BLACK);
            canvas.drawCircle(coordinate.x(), coordinate.y(), positionBorderRadius, paint);
        }

        // paint position
        paint.setColor(isPositionEnabled ? playerColor : Color.GRAY);
        canvas.drawCircle(coordinate.x(), coordinate.y(), positionRadius, paint);


        float textSize = 24 * getResources().getDisplayMetrics().density;
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        int positionsLeftCount = maxPlayerPositionsCount - positionsCount;

        canvas.drawText(" x" + positionsLeftCount,
                coordinate.x() + positionRadius,
                coordinate.y() + (textSize / 3),
                paint
        );

    }

    public void setupButton() {
        if (button != null) {
            return;
        }

        float selectedPositionBorderRadius = this.board.getSelectedPositionBorderRadius(getWidth());
        Coordinate coordinate = resolveStoppedCoordinate();
        double buttonSize = selectedPositionBorderRadius * 2.5;
        button = new Button(LoBoards.getAppContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.height = (int) buttonSize;
        layoutParams.width = (int) buttonSize;

        int topOffsetHeight = (buttonsLayout.getHeight() - boardView.getHeight() - 2 * getHeight()) / 2;
        int offsetHeight = isTop
                ? topOffsetHeight
                : getHeight() + boardView.getHeight() + topOffsetHeight;
        int offsetWidth = (buttonsLayout.getWidth() - boardView.getWidth()) / 2;
        layoutParams.topMargin = offsetHeight + coordinate.y() - (int) (buttonSize / 2);
        layoutParams.leftMargin = offsetWidth + coordinate.x() - (int) (buttonSize / 2);


        button.setOnClickListener(view -> click());
        button.setBackgroundColor(Color.TRANSPARENT);

        if (isTop) {
            ViewCompat.setAccessibilityDelegate(firstAccessibilityButton, new BoardButtonDelegate(button));
        } else {
            ViewCompat.setAccessibilityDelegate(button, new BoardButtonDelegate(lastAccessibilityButton));
        }

        button.setContentDescription("Peças do jogador " + getMovePlayerId());
        buttonsLayout.addView(button, layoutParams);
    }

    private void click() {
        try {
            clickCallable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Coordinate resolveStoppedCoordinate() {
        int offsetWidth = getWidth() / 5;
        int x = isTop ? offsetWidth : getWidth() - offsetWidth;
        int y = getHeight() / 2;
        return new Coordinate(x, y);
    }

    public int getMovePlayerId() {
        return isTop ? Player.PLAYER_2 : Player.PLAYER_1;
    }


    public void resize(int width, int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }


    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }


    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    public void setButtonsLayout(RelativeLayout buttonsLayout) {
        this.buttonsLayout = buttonsLayout;
    }

    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }


    public void setCursorColor(int selectedColor) {
        this.cursorColor = selectedColor;
    }

    public void setSelection(boolean selected) {
        isSelected = selected;
        invalidate();
    }

    public void updatePositionsCount(int playerPositionsCount) {
        isPositionEnabled = maxPlayerPositionsCount > playerPositionsCount;
        this.positionsCount = playerPositionsCount;
        invalidate();
    }

    public void setMaxPlayerPositionsCount(int maxPlayerPositionsCount) {
        this.maxPlayerPositionsCount = maxPlayerPositionsCount;
    }

    public void setOnClickListener(Callable<Void> clickCallback) {
        this.clickCallable = clickCallback;
    }

    public boolean isPositionEnabled() {
        return isPositionEnabled;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setFirstAccessibilityButton(Button firstAccessibilityButton) {
        this.firstAccessibilityButton = firstAccessibilityButton;
    }

    public void setLastAccessibilityButton(Button lastAccessibilityButton) {
        this.lastAccessibilityButton = lastAccessibilityButton;
    }
}
