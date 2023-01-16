package com.marcoantonioaav.lobogames.player.agent;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class MinimaxAgent extends Agent {
    private Game game;

    private String difficulty = MEDIUM_DIFFICULTY;

    private final int DEPTH = 2;
    private final int EVALUATION_PLAYOUTS = 25;
    private final float PLAYOUT_DEPTH_WEIGHT = 0.9f;

    private final int MAX = 1;
    private final int MIN = -MAX;
    private final int NEUTRAL = (MIN + MAX)/2;

    public MinimaxAgent(int player, String difficulty) {
        super(player, difficulty);
        setDifficulty(difficulty);
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public Move selectMove(Game game, int[][] board) {
        this.game = game;
        ArrayList<Move> moves = game.getLegalMoves(board, getId());
        if(moves.isEmpty())
            return null;
        HashMap<Move, Float> ratedMoves = rateMoves(moves, board);
        return selectMoveByScore(ratedMoves);
    }

    private HashMap<Move, Float> rateMoves(ArrayList<Move> moves, int[][] board) {
        HashMap<Move, Float> ratedMoves = new HashMap<>();
        for(Move move : moves)  {
            float score = minimax(Game.applyMove(move, board), DEPTH-1, MIN, MAX, false);
            ratedMoves.put(move, score);
        }
        return ratedMoves;
    }

    private Move selectMoveByScore(HashMap<Move, Float> ratedMoves) {
        ArrayList<Move> moves = new ArrayList<>(ratedMoves.keySet());
        float moveQuality = getRandomMoveQuality();
        Move selectedMove = null;
        float selectedQualityDifference = Float.POSITIVE_INFINITY;
        for(Move move : moves) {
            float qualityDifference = Math.abs(normalizeScore(ratedMoves.get(move)) - moveQuality);
            if(qualityDifference < selectedQualityDifference) {
                selectedMove = move;
                selectedQualityDifference = qualityDifference;
            }
        }
        return selectedMove;
    }

    private float getRandomMoveQuality() {
        float quality = getRandomBetween(0f, 1f);
        if(Objects.equals(difficulty, EASY_DIFFICULTY))
            return (float) Math.pow(quality, 2);
        if(Objects.equals(difficulty, MEDIUM_DIFFICULTY))
            return quality;
        return (float) Math.sqrt(quality);
    }

    private float getRandomBetween(float min, float max) {
        return min + new Random().nextFloat() * (max - min);
    }

    private float normalizeScore(float score) {
        return (score - MIN) / (MAX - MIN);
    }

    private float minimax(int[][] board, int depth, float alpha, float beta, boolean isMaximizing) {
        if(depth == 0 || game.isTerminalState(board))
            return evaluate(isMaximizing, board);
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

    private float evaluate(boolean isMaximizing, int[][] board) {
        float evaluation = 0f;
        int turn = getId();
        if(!isMaximizing)
            turn = getOpponentOf(turn);
        for (int p = 0; p < EVALUATION_PLAYOUTS; p++)
            evaluation += makePlayout(turn, board);
        return evaluation/EVALUATION_PLAYOUTS;
    }

    protected float makePlayout(int turn, int[][] board) {
        int[][] newBoard = Game.copyBoard(board);
        int currentPlayer = turn;
        int currentDepth = 0;
        while(!game.isTerminalState(newBoard)) {
            ArrayList<Move> legalMoves = game.getLegalMoves(newBoard, currentPlayer);
            if(legalMoves.isEmpty())
                return NEUTRAL;
            newBoard = Game.applyMove(legalMoves.get(0), newBoard);
            currentPlayer = getOpponentOf(currentPlayer);
            currentDepth++;
        }
        return (float) (evaluateTerminalState(newBoard)*Math.pow(PLAYOUT_DEPTH_WEIGHT, currentDepth));
    }

    private int evaluateTerminalState(int[][] board) {
        if(game.isVictory(board, getId()))
            return MAX;
        if(game.isVictory(board, getOpponent()))
            return MIN;
        return NEUTRAL;
    }
}
