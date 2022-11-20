package com.example.boardgame;

import java.util.ArrayList;
import java.util.Collections;

public class Tapatan extends Game {
    @Override
    public String getName() {
        return "Tapatan";
    }

    @Override
    protected int[][] getInitialBoard() {
        return new int[][]{{1, 0, 2}, {2, 0, 1}, {1, 0, 2}};
    }

    @Override
    protected boolean isVictory(int[][] board, int player) {
        return new TicTacToe().isVictory(board, player);
    }

    @Override
    protected boolean isDraw(int[][] board) {
        return false;
    }

    @Override
    protected boolean isLegalMove(Move move, int[][] board) {
        if(move.startX == Move.OUT_OF_BOARD && move.startY == Move.OUT_OF_BOARD)
            return false;
        return
                board[move.startX][move.startY] == move.player &&
                board[move.endX][move.endY] == 0 &&
                Math.abs(move.startX - move.endX) <= 1 &&
                Math.abs(move.startY - move.endY) <= 1 &&
                        (move.startX % 2 == move.startY % 2 || Math.abs(move.startX - move.endX) + Math.abs(move.startY - move.endY) == 1);
    }

    @Override
    protected int[][] applyMove(Move move, int[][] board) {
        int[][] newBoard = copyBoard(board);
        if(move != null) {
            newBoard[move.startX][move.startY] = 0;
            newBoard[move.endX][move.endY] = move.player;
        }
        return newBoard;
    }

    @Override
    protected ArrayList<Move> getLegalMoves(int[][] board, int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < 3; x++)
            for(int y=0; y < 3; y++)
                if(board[x][y] == player)
                    for(int[] eightRegion : new int[][]{{0,1}, {1,1}, {1,0}, {0,-1}, {-1,-1}, {-1, 0}, {1,-1}, {-1,1}})
                        if(isOnBoardLimits(x+eightRegion[0], y+eightRegion[1], board)) {
                            Move newMove = new Move(x, y, x+eightRegion[0], y+eightRegion[1], player);
                            if(isLegalMove(newMove, board))
                                moves.add(newMove);
                        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    protected boolean isMovementGame(int[][] board) {
        return true;
    }
}
