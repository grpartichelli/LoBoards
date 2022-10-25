package com.example.boardgame;

public abstract class Agent {
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;

    private int player;

    public Agent(int player) {
        this.player = player;
    }

    public abstract Move selectMove(Game game, int[][] board);

    public int getPlayer() {
        return player;
    }

    public int getOpponent() {
        return getOpponentOf(this.player);
    }

    public static int getOpponentOf(int player) {
        if(player == PLAYER_1)
            return PLAYER_2;
        return PLAYER_1;
    }
}
