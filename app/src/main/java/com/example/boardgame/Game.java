package com.example.boardgame;

import java.util.ArrayList;

public abstract class Game {
    public abstract int[][] enterCommandLine(String command, int[][] board, int player);
    protected abstract int[][] getInitialBoard();
    protected abstract boolean isVictory(int[][] board, int player);
    protected abstract boolean isDraw(int[][] board);
    protected abstract boolean isLegalMove(Move move, int[][] board);
    protected abstract int[][] applyMove(Move move, int[][] board);
    protected abstract ArrayList<Move> getLegalMoves(int[][] board, int player);

    public boolean isTerminalState(int[][] board) {
        return isVictory(board, Agent.PLAYER_1) || isVictory(board, Agent.PLAYER_2) || isDraw(board);
    }

    public String boardToString(int[][] board) {
        String s = "";
        for(int y=0; y < 3; y++) {
            for(int x=0; x < 3; x++) {
                s = s.concat(Integer.toString(board[x][y]));
            }
            s = s.concat("\n");
        }
        return s;
    }

    protected int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board[0].length][board.length];
        for(int x=0; x < 3; x++)
            for(int y=0; y < 3; y++)
                newBoard[x][y] = board[x][y];
        return newBoard;
    }
}
