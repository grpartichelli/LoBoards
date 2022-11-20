package com.example.boardgame;

public class Move {
    public static final int OUT_OF_BOARD = -1;
    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public int player;

    public Move(int x, int y, int player) {
        this.startX = OUT_OF_BOARD;
        this.startY = OUT_OF_BOARD;
        this.endX = x;
        this.endY = y;
        this.player = player;
    }

    public Move(int startX, int startY, int endX, int endY, int player) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.player = player;
    }

    public static String positionToString(int x, int y) {
        return (char)(x+65) + String.valueOf(y+1);
    }

    @Override
    public String toString() {
        if(startX == OUT_OF_BOARD && startY == OUT_OF_BOARD)
            return Agent.getPlayerName(player) + ": " + positionToString(endX, endY);
        return Agent.getPlayerName(player) + ": " + positionToString(startX, startY) + " para " + positionToString(endX, endY);
    }
}
