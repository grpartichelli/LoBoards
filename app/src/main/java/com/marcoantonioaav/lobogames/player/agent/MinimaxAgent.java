package com.marcoantonioaav.lobogames.player.agent;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.game.Game;

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
    private final float PLAYOUT_DEPTH_WEIGHT = 0.9f;

    private final int MAX = 1;
    private final int MIN = -MAX;
    private final int NEUTRAL = (MIN + MAX)/2;

    public MinimaxAgent(int player, String difficulty) {
        super(player, difficulty);
        setDifficulty(difficulty);
    }

    public void setDifficulty(String difficulty) {
        if(Objects.equals(difficulty, EASY_DIFFICULTY))
            imprecision = 0.8f;
        else if(Objects.equals(difficulty, MEDIUM_DIFFICULTY))
            imprecision = 0.4f;
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
        ArrayList<Move> filteredMoves = filterMovesByScores(ratedMoves);
        Collections.shuffle(filteredMoves);
        return filteredMoves.get(0);
    }

    private ArrayList<Move> filterMovesByScores(HashMap<Move, Float> ratedMoves) {
        ArrayList<Move> movesOrderedByScore = getMovesOrderedByScore(ratedMoves);
        float scoresSum = getScoresSum(ratedMoves);
        float maxInverseScoreRange = scoresSum*imprecision;
        float sum = 0f;
        ArrayList<Move> filteredMoves = new ArrayList<>();
        for(int i = movesOrderedByScore.size()-1; i >= 0; i--) {
            Move move = movesOrderedByScore.get(i);
            filteredMoves.add(move);
            sum += normalizeScore(ratedMoves.get(move));
            if (sum > maxInverseScoreRange)
                break;
        }
        return filteredMoves;
    }

    private ArrayList<Move> getMovesOrderedByScore(HashMap<Move, Float> scores) {
        ArrayList<Move> moves = new ArrayList<>(scores.keySet());
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move move1, Move move2) {
                return Float.compare(scores.get(move1), scores.get(move2));
            }
        });
        return moves;
    }

    private float getScoresSum(HashMap<Move, Float> scores) {
        float scoreSum = 0f;
        for(Move move : scores.keySet())
            scoreSum += normalizeScore(scores.get(move));
        return scoreSum;
    }

    private float normalizeScore(float score) {
        return score + MAX;
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
