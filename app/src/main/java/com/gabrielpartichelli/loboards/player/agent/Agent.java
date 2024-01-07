package com.gabrielpartichelli.loboards.player.agent;

import com.gabrielpartichelli.loboards.game.Game;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.player.Player;

public abstract class Agent extends Player {
    public static String EASY_DIFFICULTY = "Fácil";
    public static String MEDIUM_DIFFICULTY = "Médio";
    public static String HARD_DIFFICULTY = "Difícil";

    private String difficulty = MEDIUM_DIFFICULTY;

    public Agent(int id, String difficulty) {
        super(id);
        setDifficulty(difficulty);
    }

    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    protected String getDifficulty() { return difficulty; }

    @Override
    public boolean isReady() { return true; }

    @Override
    public final Move getMove(Game game) {
        return selectMove(game);
    }

    public abstract Move selectMove(Game game);
}
