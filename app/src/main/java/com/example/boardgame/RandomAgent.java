package com.example.boardgame;

public class RandomAgent extends Agent {
    public RandomAgent(int player) {
        super(player);
    }

    @Override
    public Move selectMove(Game game, int[][] board) {
        if(game.getLegalMoves(board, getPlayer()).isEmpty())
            return null;
        return game.getLegalMoves(board, getPlayer()).get(0);
    }
}
