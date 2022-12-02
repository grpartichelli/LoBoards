package com.example.boardgame;

import java.util.ArrayList;
import java.util.Collections;

public class Tapatan extends Game {
    @Override
    public String getName() {
        return "Tapatan";
    }

    @Override
    public int[][] getInitialBoard() {
        return new int[][]{{Agent.PLAYER_1, Agent.EMPTY, Agent.PLAYER_2}, {Agent.PLAYER_2, Agent.EMPTY, Agent.PLAYER_1}, {Agent.PLAYER_1, Agent.EMPTY, Agent.PLAYER_2}};
    }

    @Override
    public boolean isVictory(int[][] board, int player) {
        return new TicTacToe().isVictory(board, player);
    }

    @Override
    public boolean isDraw(int[][] board) {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        return move.movements.length == 1 && move.movements[0].isAdjacentInlineMovement(board);
    }

    @Override
    public ArrayList<Move> getLegalMoves(int[][] board, int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
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
    public boolean isInsertionGame(int[][] board) {
        return false;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int player) {
        return new Move(startX, startY, endX, endY, player);
    }
}
