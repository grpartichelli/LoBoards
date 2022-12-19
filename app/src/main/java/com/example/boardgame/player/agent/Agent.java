package com.example.boardgame.player.agent;

import com.example.boardgame.move.Move;
import com.example.boardgame.game.Game;
import com.example.boardgame.player.Player;

public abstract class Agent extends Player {
    public static String EASY_DIFFICULTY = "Fácil";
    public static String MEDIUM_DIFFICULTY = "Médio";
    public static String HARD_DIFFICULTY = "Difícil";

    public Agent(int id, String difficulty) {
        super(id);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public final Move getMove(Game game, int[][] board) {
        return selectMove(game, board);
    }

    public abstract Move selectMove(Game game, int[][] board);
}
