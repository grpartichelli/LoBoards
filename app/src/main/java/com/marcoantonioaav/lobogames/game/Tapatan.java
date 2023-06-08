package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;

import java.util.ArrayList;
import java.util.Collections;

public class Tapatan extends Game {
    @Override
    public String getName() {
        return "Tapatan";
    }

    @Override
    public int[][] getInitialBoard() {
        return new int[][]{{Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2}, {Player.PLAYER_2, Player.EMPTY, Player.PLAYER_1}, {Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2}};
    }

    @Override
    public boolean isVictory(int[][] board, int playerId) {
        return new TicTacToe().isVictory(board, playerId);
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
    public ArrayList<Move> getLegalMoves(int[][] board, int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
                if(board[x][y] == playerId)
                    for(int[] eightRegion : new int[][]{{0,1}, {1,1}, {1,0}, {0,-1}, {-1,-1}, {-1, 0}, {1,-1}, {-1,1}})
                        if(isOnBoardLimits(x+eightRegion[0], y+eightRegion[1], board)) {
                            Move newMove = new Move(x, y, x+eightRegion[0], y+eightRegion[1], playerId);
                            if(isLegalMove(newMove, board))
                                moves.add(newMove);
                        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int playerId) {
        return new Move(startX, startY, endX, endY, playerId);
    }

    @Override
    public String getRules() {
        return "Cada jogador possui três peças posicionadas no tabuleiro. É permitido apenas o deslocamento das peças para posições adjacentes conectadas por uma das linhas do tabuleiro. Ganha aquele que conseguir alinhar três peças.";
    }

    @Override
    public float getHeuristicEvaluationOf(int[][] board, int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(board, playerId, turn, this);
    }
}
