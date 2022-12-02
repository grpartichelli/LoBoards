package com.example.boardgame;

public class MinimaxAgent extends Agent {
    private Game game;

    private final int DEPTH = 2;

    private final int MIN = -1;
    private final int MAX = 1;
    private final int NEUTRAL = 0;

    public MinimaxAgent(int player) { super(player); }

    @Override
    public Move selectMove(Game game, int[][] board) {
        this.game = game;
        int bestValue = MIN;
        Move bestMove = null;
        for(Move move : game.getLegalMoves(board, getPlayer())) {
            int value = minimax(game.applyMove(move, board), DEPTH, false);
            if(value >= bestValue) {
                bestMove = move;
                bestValue = value;
            }
        }
        return bestMove;
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing) {
        if(depth == 0 || game.isTerminalState(board))
            return evaluate(board);
        if(isMaximizing) {
            int maxValue = MIN;
            for(Move move : game.getLegalMoves(board, getPlayer())) {
                int[][] newBoard = game.applyMove(move, board);
                int newValue = minimax(newBoard, depth - 1, false);
                maxValue = Math.max(maxValue, newValue);
            }
            return maxValue;
        }
        else {
            int minValue = MAX;
            for(Move move : game.getLegalMoves(board, getOpponent())) {
                int[][] newBoard = game.applyMove(move, board);
                int newValue = minimax(newBoard, depth - 1, true);
                minValue = Math.min(minValue, newValue);
            }
            return minValue;
        }
    }

    private int evaluate(int[][] board) {
        if(game.isVictory(board, getPlayer()))
            return MAX;
        if(game.isVictory(board, getOpponent()))
            return MIN;
        return NEUTRAL;
    }
}
