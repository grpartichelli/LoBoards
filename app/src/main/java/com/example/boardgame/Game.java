package com.example.boardgame;

import java.util.ArrayList;

public abstract class Game {
    public abstract String getName();
    public abstract int[][] getInitialBoard();
    public abstract boolean isVictory(int[][] board, int player);
    public abstract boolean isDraw(int[][] board);
    public abstract boolean isLegalMove(Move move, int[][] board);
    public abstract ArrayList<Move> getLegalMoves(int[][] board, int player);
    public abstract boolean isInsertionGame(int[][] board);
    public abstract Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int player);

    public boolean isTerminalState(int[][] board) {
        return isVictory(board, Agent.PLAYER_1) || isVictory(board, Agent.PLAYER_2) || isDraw(board);
    }

    public int[][] applyMove(Move move, int[][] board) {
        int[][] newBoard = copyBoard(board);
        if(move != null)
            for(Movement movement : move.movements)
                newBoard = applyMovement(movement, newBoard);
        return newBoard;
    }

    protected int[][] applyMovement(Movement movement, int[][] board) {
        int[][] newBoard = copyBoard(board);
        if(movement.startX != Movement.OUT_OF_BOARD && movement.startY != Movement.OUT_OF_BOARD)
            newBoard[movement.startX][movement.startY] = Agent.EMPTY;
        if(movement.endX != Movement.OUT_OF_BOARD && movement.endY != Movement.OUT_OF_BOARD)
            newBoard[movement.endX][movement.endY] = movement.piece;
        return newBoard;
    }

    protected int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board[0].length][board.length];
        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
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
