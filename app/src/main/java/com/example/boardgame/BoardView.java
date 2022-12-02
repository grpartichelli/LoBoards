package com.example.boardgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BoardView extends View {
    private int[][] board = new TicTacToe().getInitialBoard();
    private int selectedX = Movement.OUT_OF_BOARD;
    private int selectedY = Movement.OUT_OF_BOARD;

    private final Paint paint = new Paint();

    public BoardView(Context context) {
        super(context);
    }
    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void drawBoard(int[][] board) {
        this.board = board;
        this.selectedX = Movement.OUT_OF_BOARD;
        this.selectedY = Movement.OUT_OF_BOARD;
        update();
    }

    public void drawBoard(int[][] board, int selectedX, int selectedY) {
        this.board = board;
        this.selectedX = selectedX;
        this.selectedY = selectedY;
        update();
    }

    private void update() {
        setVisibility(View.INVISIBLE);
        setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
        drawPieces(canvas);
        drawCoordinates(canvas);
    }

    private void drawLines(Canvas canvas) {
        paint.setColor(Color.BLACK);
        int startX, startY, stopX, stopY;
        for(int x=0; x < getBoardWidth(); x++) {
            startX = getPosition(x, getWidth(), getBoardWidth());
            startY = getPosition(0, getHeight(), getBoardHeight());
            stopX = startX;
            stopY = getPosition(getBoardHeight()-1, getHeight(), getBoardHeight());
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
        for(int y=0; y< getBoardHeight(); y++) {
            startX = getPosition(0, getWidth(), getBoardWidth());
            startY = getPosition(y, getHeight(), getBoardHeight());
            stopX = getPosition(getBoardWidth()-1, getWidth(), getBoardWidth());
            stopY = startY;
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
        for(int x=0; x < getBoardWidth()-1; x++)
            for(int y=0; y < getBoardHeight()-1; y++)
                if((x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1)) {
                    startX = getPosition(x, getWidth(), getBoardWidth());
                    startY = getPosition(y, getHeight(), getBoardHeight());
                    stopX = getPosition(x+1, getWidth(), getBoardWidth());
                    stopY = getPosition(y+1, getHeight(), getBoardHeight());
                    canvas.drawLine(startX, startY, stopX, stopY, paint);
                }
        for(int x=1; x < getBoardWidth(); x++)
            for(int y=0; y < getBoardHeight()-1; y++)
                if((x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1)) {
                    startX = getPosition(x, getWidth(), getBoardWidth());
                    startY = getPosition(y, getHeight(), getBoardHeight());
                    stopX = getPosition(x-1, getWidth(), getBoardWidth());
                    stopY = getPosition(y+1, getHeight(), getBoardHeight());
                    canvas.drawLine(startX, startY, stopX, stopY, paint);
                }
    }

    private void drawPieces(Canvas canvas) {
        int cx, cy, radius;
        for(int x=0; x < getBoardWidth(); x++)
            for(int y=0; y < getBoardHeight(); y++) {
                cx = getPosition(x, getWidth(), getBoardWidth());
                cy = getPosition(y, getHeight(), getBoardHeight());
                if(board[x][y] != 0){
                    if(selectedX == x && selectedY == y) {
                        paint.setColor(Color.BLUE);
                        radius = getPieceRadius()+4;
                    }
                    else {
                        paint.setColor(Color.BLACK);
                        radius = getPieceRadius()+2;
                    }
                    canvas.drawCircle(cx, cy, radius, paint);
                }
                switch (board[x][y]) {
                    case Agent.PLAYER_1:
                        radius = getPieceRadius();
                        paint.setColor(Color.GREEN);
                        break;
                    case Agent.PLAYER_2:
                        radius = getPieceRadius();
                        paint.setColor(Color.RED);
                        break;
                    default:
                        radius = getPieceRadius()/2;
                        paint.setColor(Color.GRAY);
                        break;
                }
                canvas.drawCircle(cx, cy, radius, paint);
            }
    }

    private void drawCoordinates(Canvas canvas) {
        int cx, cy, padding = (3*getPieceRadius())/2, textSize = getPieceRadius()/2;
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        for(int x=0; x < getBoardWidth(); x++) {
            cx = getPosition(x, getWidth(), getBoardWidth());
            cy = getPosition(0, getHeight(), getBoardHeight()) - padding;
            canvas.drawText(String.valueOf(Movement.positionToString(x, 0).charAt(0)), cx, cy, paint);
        }
        for(int y=0; y < getBoardHeight(); y++) {
            cx = getPosition(0, getWidth(), getBoardWidth()) - padding;
            cy = getPosition(y, getHeight(), getBoardHeight()) + textSize/3;
            canvas.drawText(String.valueOf(Movement.positionToString(0, y).charAt(1)), cx, cy, paint);
        }
    }

    private int getPosition(int index, int totalSize, int totalQuantity) {
        return (totalSize/(totalQuantity+1)) * (index+1);
    }

    private int getPieceRadius() {
        return getWidth()/(getBoardWidth()*4);
    }

    private int getBoardWidth() {
        return Game.getBoardWidth(board);
    }
    private int getBoardHeight() {
        return Game.getBoardHeight(board);
    }
}
