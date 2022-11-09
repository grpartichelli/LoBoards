package com.example.boardgame;

import java.util.ArrayList;
import java.util.Collections;

public class TicTacToe extends Game {
    @Override
    public String getName() {
        return "Jogo da Velha";
    }

    @Override
    protected int[][] getInitialBoard() {
        return new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    }

    @Override
    protected boolean isVictory(int[][] board, int player) {
        return isLineVictory(board, player) || isColumnVictory(board, player) || isDiagonalVictory(board, player);
    }

    private boolean isDiagonalVictory(int[][] board, int player) {
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }
    private boolean isColumnVictory(int[][] board, int player) {
        for(int x=0; x < 3; x++)
            if(board[x][0] == player && board[x][1] == player && board[x][2] == player)
                return true;
        return false;
    }
    private boolean isLineVictory(int[][] board, int player) {
        for(int y=0; y < 3; y++)
            if(board[0][y] == player && board[1][y] == player && board[2][y] == player)
                return true;
        return false;
    }

    @Override
    protected boolean isDraw(int[][] board) {
        for(int x=0; x < 3; x++)
            for(int y=0; y < 3; y++)
                if(board[x][y] == 0)
                    return false;
        return !isVictory(board, Agent.PLAYER_1) && !isVictory(board, Agent.PLAYER_2);
    }

    @Override
    protected boolean isLegalMove(Move move, int[][] board) {
        return board[move.x][move.y] == 0;
    }

    @Override
    protected int[][] applyMove(Move move, int[][] board) {
        int[][] newBoard = copyBoard(board);
        if(move != null)
            newBoard[move.x][move.y] = move.player;
        return newBoard;
    }

    @Override
    protected ArrayList<Move> getLegalMoves(int[][] board, int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < 3; x++) {
            for(int y=0; y < 3; y++) {
                Move newMove = new Move(x, y, player);
                if(isLegalMove(newMove, board))
                    moves.add(newMove);
            }
        }
        Collections.shuffle(moves);
        return moves;
    }
}
