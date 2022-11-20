package com.example.boardgame;

import java.util.ArrayList;

public abstract class Game {
    public abstract String getName();
    protected abstract int[][] getInitialBoard();
    protected abstract boolean isVictory(int[][] board, int player);
    protected abstract boolean isDraw(int[][] board);
    protected abstract boolean isLegalMove(Move move, int[][] board);
    protected abstract int[][] applyMove(Move move, int[][] board);
    protected abstract ArrayList<Move> getLegalMoves(int[][] board, int player);
    protected abstract boolean isMovementGame(int[][] board);

    public boolean isTerminalState(int[][] board) {
        return isVictory(board, Agent.PLAYER_1) || isVictory(board, Agent.PLAYER_2) || isDraw(board);
    }

    protected int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board[0].length][board.length];
        for(int x=0; x < 3; x++)
            for(int y=0; y < 3; y++)
                newBoard[x][y] = board[x][y];
        return newBoard;
    }

    public static boolean isOnBoardLimits(int x, int y, int[][] board) {
        return x >= 0 && y >= 0 && x < getBoardWidth(board) && y < getBoardHeight(board);
    }
    public static int getBoardWidth(int[][] board) {
        return board.length;
    }
    public static int getBoardHeight(int[][] board) {
        try {
            return board[0].length;
        } catch (Exception e) {
            return 0;
        }
    }
}
