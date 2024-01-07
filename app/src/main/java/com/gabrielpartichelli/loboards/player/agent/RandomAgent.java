package com.gabrielpartichelli.loboards.player.agent;

import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.game.Game;

public class RandomAgent extends Agent {
    public RandomAgent(int player) {
        super(player, Agent.EASY_DIFFICULTY);
    }

    @Override
    public Move selectMove(Game game) {
        if (game.getLegalMoves(getId()).isEmpty())
            return null;
        return game.getLegalMoves(getId()).get(0);
    }

}
