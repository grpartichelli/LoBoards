package com.marcoantonioaav.lobogames.player.agent;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class MinimaxAgent extends Agent {
    private Game game;

    private static final int EVALUATION_PLAYOUTS = 25;
    private static final int MAX_PLAYOUT_DEPTH = 50;
    private final int SEARCH_TIME_MILLIS = 1500;

    public static final float MAX = 1;
    public static final float MIN = -MAX;
    public static final float NEUTRAL = (MIN + MAX)/2;

    private float easyMean = 0.4f;
    private float mediumMean = 0.6f;
    private float hardMean = 1f;
    private float easyStandardDeviation = 0.3f;
    private float mediumStandardDeviation = 0.3f;
    private float hardStandardDeviation = 0.3f;

    public MinimaxAgent(int player, String difficulty) { super(player, difficulty); }

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
        long startTime = System.currentTimeMillis();
        long timeSpent = 0;
        int depth = 0;
        while(timeSpent < SEARCH_TIME_MILLIS) {
            for(Move move : moves) {
                float score = minimax(Game.applyMove(move, board), depth, MIN, MAX, false);
                ratedMoves.put(move, normalizeTo0To1(score));
            }
            depth++;
            timeSpent = System.currentTimeMillis() - startTime;
        }
        return ratedMoves;
    }

    private Move selectMoveByScore(HashMap<Move, Float> ratedMoves) {
        ArrayList<Move> moves = new ArrayList<>(ratedMoves.keySet());
        float moveQuality = getRandomMoveQuality();
        Move selectedMove = null;
        float selectedQualityDifference = Float.POSITIVE_INFINITY;
        for(Move move : moves) {
            float qualityDifference = Math.abs(ratedMoves.get(move) - moveQuality);
            if(qualityDifference < selectedQualityDifference) {
                selectedMove = move;
                selectedQualityDifference = qualityDifference;
            }
        }
        return selectedMove;
    }

    private float getRandomMoveQuality() {
        if(Objects.equals(getDifficulty(), EASY_DIFFICULTY))
            return getRandomOnGaussian(easyMean, easyStandardDeviation);
        if(Objects.equals(getDifficulty(), MEDIUM_DIFFICULTY))
            return getRandomOnGaussian(mediumMean, mediumStandardDeviation);
        return getRandomOnGaussian(hardMean, hardStandardDeviation);
    }

    private float getRandomOnGaussian(float mean, float standardDeviation) {
        return (float)(new Random().nextGaussian()*standardDeviation)+mean;
    }

    private float normalizeTo0To1(float score) {
        return (score - MIN) / (MAX - MIN);
    }

    public static float normalizeToEvaluationLimits(float value, float valueMin, float valueMax) {
        return MIN + ((value - valueMin)*(MAX - MIN)) / (valueMax - valueMin);
    }

    private float minimax(int[][] board, int depth, float alpha, float beta, boolean isMaximizing) {
        if(depth == 0 || game.isTerminalState(board))
            return evaluate(board, isMaximizing);
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

    private float evaluate(int[][] board, boolean isMaximizing) {
        if(game.isTerminalState(board))
            return evaluateTerminalState(board, getId(), game);
        int turn = getId();
        if(!isMaximizing)
            turn = getOpponentOf(turn);
        return game.getHeuristicEvaluationOf(board, getId(), turn);
    }

    public static float evaluateWithPlayouts(int[][] board, int player, int turn, Game game) {
        float evaluation = 0f;
        for(int p = 0; p < EVALUATION_PLAYOUTS; p++)
            evaluation += makePlayout(board, player, turn, game);
        return evaluation/EVALUATION_PLAYOUTS;
    }

    protected static float makePlayout(int[][] board, int player, int turn, Game game) {
        int[][] newBoard = Game.copyBoard(board);
        int currentPlayer = turn;
        int depth = 0;
        while(!game.isTerminalState(newBoard) && depth < MAX_PLAYOUT_DEPTH) {
            ArrayList<Move> legalMoves = game.getLegalMoves(newBoard, currentPlayer);
            if(legalMoves.isEmpty())
                return NEUTRAL;
            newBoard = Game.applyMove(legalMoves.get(0), newBoard);
            currentPlayer = getOpponentOf(currentPlayer);
            depth++;
        }
        return evaluateTerminalState(newBoard, player, game);
    }

    protected static float evaluateTerminalState(int[][] board, int player, Game game) {
        if(game.isVictory(board, player))
            return MAX;
        if(game.isVictory(board, getOpponentOf(player)))
            return MIN;
        return NEUTRAL;
    }

    public void setEasyMean(float easyMean) {
        this.easyMean = easyMean;
    }

    public void setMediumMean(float mediumMean) {
        this.mediumMean = mediumMean;
    }

    public void setHardMean(float hardMean) {
        this.hardMean = hardMean;
    }

    public void setEasyStandardDeviation(float easyStandardDeviation) {
        if(easyStandardDeviation != 0)
            this.easyStandardDeviation = easyStandardDeviation;
    }

    public void setMediumStandardDeviation(float mediumStandardDeviation) {
        if(mediumStandardDeviation != 0)
            this.mediumStandardDeviation = mediumStandardDeviation;
    }

    public void setHardStandardDeviation(float hardStandardDeviation) {
        if(hardStandardDeviation != 0)
            this.hardStandardDeviation = hardStandardDeviation;
    }
}
