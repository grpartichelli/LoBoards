package com.example.boardgame.player.agent;

import com.example.boardgame.move.Move;
import com.example.boardgame.game.Game;

import java.util.Objects;

public class MinimaxAgent extends Agent {
    private Game game;

    private int depth = 2;

    private final int MIN = -1;
    private final int MAX = 1;
    private final int NEUTRAL = 0;

    public MinimaxAgent(int player, String difficulty) {
        super(player, difficulty);
        setDifficulty(difficulty);
    }

    public void setDifficulty(String difficulty) {
        if(Objects.equals(difficulty, EASY_DIFFICULTY))
            depth = 1;
        else if(Objects.equals(difficulty, MEDIUM_DIFFICULTY))
            depth = 2;
        else
            depth = 5;
    }

    @Override
    public Move selectMove(Game game, int[][] board) {
        this.game = game;
        int bestValue = MIN;
        Move bestMove = null;
        for(Move move : game.getLegalMoves(board, getId())) {
            int value = minimax(Game.applyMove(move, board), depth, false);
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
            for(Move move : game.getLegalMoves(board, getId())) {
                int[][] newBoard = Game.applyMove(move, board);
                int newValue = minimax(newBoard, depth - 1, false);
                maxValue = Math.max(maxValue, newValue);
            }
            return maxValue;
        }
        else {
            int minValue = MAX;
            for(Move move : game.getLegalMoves(board, getOpponent())) {
                int[][] newBoard = Game.applyMove(move, board);
                int newValue = minimax(newBoard, depth - 1, true);
                minValue = Math.min(minValue, newValue);
            }
            return minValue;
        }
    }

    private int evaluate(int[][] board) {
        if(game.isVictory(board, getId()))
            return MAX;
        if(game.isVictory(board, getOpponent()))
            return MIN;
        return NEUTRAL;
    }
}
