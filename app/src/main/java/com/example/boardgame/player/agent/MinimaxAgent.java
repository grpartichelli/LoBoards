package com.example.boardgame.player.agent;

import com.example.boardgame.move.Move;
import com.example.boardgame.game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public class MinimaxAgent extends Agent {
    private Game game;

    private float imprecision = 0f;

    private final int DEPTH = 2;
    private final int EVALUATION_PLAYOUTS = 25;

    private final int MIN = -1;
    private final int MAX = 1;
    private final int NEUTRAL = (MIN + MAX)/2;

    public MinimaxAgent(int player, String difficulty) {
        super(player, difficulty);
        setDifficulty(difficulty);
    }

    public void setDifficulty(String difficulty) {
        if(Objects.equals(difficulty, EASY_DIFFICULTY))
            imprecision = 0.5f;
        else if(Objects.equals(difficulty, MEDIUM_DIFFICULTY))
            imprecision = 0.25f;
        else
            imprecision = 0f;
    }

    @Override
    public Move selectMove(Game game, int[][] board) {
        this.game = game;
        ArrayList<Move> moves = game.getLegalMoves(board, getId());
        if(moves.isEmpty())
            return null;
        HashMap<Move, Float> ratedMoves = rateMoves(moves, board);
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move move1, Move move2) {
                return Float.compare(ratedMoves.get(move1), ratedMoves.get(move2));
            }
        });

        float scoreSum = 0f;
        for(Move move : moves)
            scoreSum += ratedMoves.get(move) + MAX;

        float maxInverseScoreRange = scoreSum*imprecision;
        float sum = 0f;
        ArrayList<Move> newMoves = new ArrayList<>();
        for(int i = moves.size()-1; i >= 0; i--) {
            Move move = moves.get(i);
            newMoves.add(move);
            sum += ratedMoves.get(move) + MAX;
            if (sum > maxInverseScoreRange)
                break;
        }
        Collections.shuffle(newMoves);
        return newMoves.get(0);
    }

    private HashMap<Move, Float> rateMoves(ArrayList<Move> moves, int[][] board) {
        HashMap<Move, Float> ratedMoves = new HashMap<>();
        for(Move move : moves)  {
            float score = minimax(Game.applyMove(move, board), DEPTH, MIN, MAX, false);
            ratedMoves.put(move, score);
        }
        return ratedMoves;
    }

    private float minimax(int[][] board, int depth, float alpha, float beta, boolean isMaximizing) {
        if(depth == 0 || game.isTerminalState(board))
            return evaluate(board);
        if(isMaximizing) {
            float maxValue = MIN;
            for(Move move : game.getLegalMoves(board, getId())) {
                int[][] newBoard = Game.applyMove(move, board);
                float newValue = minimax(newBoard, depth - 1, alpha, beta, false);
                maxValue = Math.max(maxValue, newValue);
                if(maxValue >= beta)
                    break;
                alpha = Math.max(alpha, maxValue);
            }
            return maxValue;
        }
        else {
            float minValue = MAX;
            for(Move move : game.getLegalMoves(board, getOpponent())) {
                int[][] newBoard = Game.applyMove(move, board);
                float newValue = minimax(newBoard, depth - 1, alpha, beta, true);
                minValue = Math.min(minValue, newValue);
                if(minValue <= alpha)
                    break;
                beta = Math.min(beta, minValue);
            }
            return minValue;
        }
    }

    private float evaluate(int[][] board) {
        float evaluation = 0f;
        for (int p = 0; p < EVALUATION_PLAYOUTS; p++)
            evaluation += evaluateTerminalState(makePlayout(game, board));
        return evaluation/ EVALUATION_PLAYOUTS;
    }

    private float evaluateTerminalState(int[][] board) {
        if(game.isVictory(board, getId()))
            return MAX;
        if(game.isVictory(board, getOpponent()))
            return MIN;
        return NEUTRAL;
    }
}
