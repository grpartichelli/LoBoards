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

    private String getPlayerName() {
        if(player == Agent.PLAYER_1)
            return "Você";
        return "Oponente";
    }

    @Override
    public String toString() {
        return getPlayerName() + ": " + positionToString(x, y);
    }
}
