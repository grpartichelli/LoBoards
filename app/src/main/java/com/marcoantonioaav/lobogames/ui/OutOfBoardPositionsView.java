package com.marcoantonioaav.lobogames.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.marcoantonioaav.lobogames.board.Board;

public class OutOfBoardPositionsView extends View {

    Paint paint = new Paint();
    Board board;

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
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawPaint(paint);
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
}
