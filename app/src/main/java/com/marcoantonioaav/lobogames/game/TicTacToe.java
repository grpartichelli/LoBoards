package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.Collections;

public class TicTacToe extends Game {
    @Override
    public String getName() {
        return "Jogo da Velha";
    }

    @Override
    public int[][] getInitialBoard() {
        return new int[][]{{Player.EMPTY, Player.EMPTY, Player.EMPTY}, {Player.EMPTY, Player.EMPTY, Player.EMPTY}, {Player.EMPTY, Player.EMPTY, Player.EMPTY}};
    }

    @Override
    public boolean isVictory(int[][] board, int playerId) {
        return isLineVictory(board, playerId) || isColumnVictory(board, playerId) || isDiagonalVictory(board, playerId);
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
    public boolean isDraw(int[][] board) {
        for(int x=0; x < 3; x++)
            for(int y=0; y < 3; y++)
                if(board[x][y] == Player.EMPTY)
                    return false;
        return !isVictory(board, Player.PLAYER_1) && !isVictory(board, Player.PLAYER_2);
    }

    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        return move.movements.length == 1 && move.movements[0].isInsertion(board);
    }

    @Override
    public ArrayList<Move> getLegalMoves(int[][] board, int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < 3; x++) {
            for(int y=0; y < 3; y++) {
                Move newMove = new Move(x, y, playerId);
                if(isLegalMove(newMove, board))
                    moves.add(newMove);
            }
        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int playerId) {
        return new Move(endX, endY, playerId);
    }

    @Override
    public String getRules() {
        return "Em sua vez, o jogador pode inserir uma peça em uma posição vazia do tabuleiro. Ganha aquele que conseguir alinhar três peças.";
    }
}
