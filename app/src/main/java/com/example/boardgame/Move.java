package com.example.boardgame;

public class Move {
    public int x;
    public int y;
    public int player;

    public Move(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public static String positionToString(int x, int y) {
        return (char)(x+65) + String.valueOf(y+1);
    }

    @Override
    public String toString() {
        return "Player " + player + ": " + positionToString(x, y);
    }
}
