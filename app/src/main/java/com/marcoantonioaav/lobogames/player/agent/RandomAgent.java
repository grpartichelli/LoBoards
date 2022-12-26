package com.marcoantonioaav.lobogames.player.agent;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.game.Game;

public class RandomAgent extends Agent {
    public RandomAgent(int player) {
        super(player, Agent.EASY_DIFFICULTY);
    }

    @Override
    public Move selectMove(Game game, int[][] board) {
        if(game.getLegalMoves(board, getId()).isEmpty())
            return null;
        return game.getLegalMoves(board, getId()).get(0);
    }

}
