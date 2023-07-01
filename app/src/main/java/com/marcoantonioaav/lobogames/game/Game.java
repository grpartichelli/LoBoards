package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;

public abstract class Game {
    public abstract String getName();
    public abstract int[][] getInitialBoard();
    public abstract int getBoardImage();
    public abstract boolean isVictory(int[][] board, int playerId);
    public abstract boolean isDraw(int[][] board);
    public abstract boolean isLegalMove(Move move, int[][] board);
    public abstract ArrayList<Move> getLegalMoves(int[][] board, int playerId);
    public abstract Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int playerId);
    public abstract String getRules();
    /**Método que retorna a avaliação heurística de um estado do jogo,
     * utilizado pelo agente MinimaxAgent.
     * @param board tabuleiro
     * @param playerId id do agente
     * @param turn id do player da rodada (i.e. que fará a próxima jogada)
     * @return float - Avaliação no intervalo [MinimaxAgent.MIN, MinimaxAgent.MAX].
     * Dica: pode ser utilizado MinimaxAgent.normalizeToEvaluationLimits().
     */
    public abstract float getHeuristicEvaluationOf(int[][] board, int playerId, int turn);



    public boolean isTerminalState(int[][] board) {
        return isVictory(board, Player.PLAYER_1) || isVictory(board, Player.PLAYER_2) || isDraw(board);
    }

    public static int[][] applyMove(Move move, int[][] board) {
        int[][] newBoard = copyBoard(board);
        if(move != null)
            for(Movement movement : move.movements)
                newBoard = applyMovement(movement, newBoard);
        return newBoard;
    }

    public static int[][] applyMovement(Movement movement, int[][] board) {
        int[][] newBoard = copyBoard(board);
        if(movement.startX != Movement.OUT_OF_BOARD && movement.startY != Movement.OUT_OF_BOARD)
            newBoard[movement.startX][movement.startY] = Player.EMPTY;
        if(movement.endX != Movement.OUT_OF_BOARD && movement.endY != Movement.OUT_OF_BOARD)
            newBoard[movement.endX][movement.endY] = movement.piece;
        return newBoard;
    }

    public static int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board[0].length][board.length];
        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
                newBoard[x][y] = board[x][y];
        return newBoard;
    }

    protected int countPlayerPieces(int[][] board, int playerId) {
        int count = 0;
        for(int x=0; x<getBoardWidth(board); x++)
            for(int y=0; y<getBoardHeight(board); y++)
                if(board[x][y] == playerId)
                    count++;
        return count;
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
