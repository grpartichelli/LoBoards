package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;

import java.util.ArrayList;
import java.util.Collections;

public class TsoroYematatuV2 extends Game {
    @Override
    public String getName() {
        return "Tsoro Yematatu";
    }

    @Override
    public int[][] getInitialBoard() {
        return new int[][]{
            {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
            {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
            {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
            {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
            {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY}
        };
    }

    @Override
    public boolean isVictory(int[][] board, int playerId) {
        return isLineVictory(board, playerId) || isColumnVictory(board, playerId) || isDiagonalVictory(board, playerId);
    }

    private boolean isLineVictory(int[][] board, int player) {
        for(int y=0; y < getBoardHeight(board); y++)
            if((board[0][y] == player && board[1][y] == player && board[2][y] == player && board[3][y] == player) ||
                    (board[1][y] == player && board[2][y] == player && board[3][y] == player && board[4][y] == player))
                return true;
        return false;
    }

    /*private boolean isLineVictory(int[][] board, int playerId){
        // line victory
        int count = 0;
        for (int y = 0; y < getBoardHeight(board); y++){
            for (int x = 0; x < getBoardWidth(board); x++){
                if (board[x][y] == playerId) {
                    count++;
                }
            }
        }
        if (count == 4)
            return true;
        else return false;
    }*/

    private boolean isColumnVictory(int[][] board, int player) {
        for(int x=0; x < getBoardWidth(board); x++)
            if((board[x][0] == player && board[x][1] == player && board[x][2] == player && board[x][3] == player) ||
                    (board[x][1] == player && board[x][2] == player && board[x][3] == player && board[x][4] == player))
                return true;
        return false;
    }

    /*private boolean isColumnVictory(int[][] board, int playerId){
        int count = 0;
        // column victory
        for (int x = 0; x < getBoardWidth(board); x++){
            for (int y = 0; y < getBoardHeight(board); y++) {
                if (board[x][y] == playerId) {
                    count++;
                }
            }
        }
        if (count == 4)
            return true;
        else return false;
    }*/

    private boolean isDiagonalVictory(int[][] board, int player) {
        return ((board[1][1] == player && board[2][2] == player && board[3][3] == player && board[4][4] == player) ||
                (board[0][0] == player && board[1][1] == player && board[2][2] == player && board[3][3] == player) ||
                (board[0][4] == player && board[1][3] == player && board[2][2] == player && board[3][1] == player) ||
                (board[1][3] == player && board[2][2] == player && board[3][1] == player && board[4][0] == player));
    }

    /*private boolean isDiagonalVictory(int[][] board, int playerId){
        // diagonal victory
        int x = 0;
        int y = 0;
        int count = 0;
        do {
            if (board[x][y] == playerId) {
                count++;
            }
            x++;
            y++;
        } while (x < 5 && y < 5);

        if (count < 4) {
            x = 4;
            y = 4;
            count = 0;
            do {
                if (board[x][y] == playerId) {
                    count++;
                }
                x--;
                y--;
            } while (x > 0 && y > 0);
        }
        if (count == 4)
            return true;
        else return false;
    }*/

    @Override
    public boolean isDraw(int[][] board) {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        if (countPlayerPieces(board, move.playerId) < 4)
            return move.movements.length == 1 && move.movements[0].isInsertion(board);
        return move.movements.length == 1 && move.movements[0].isAdjacentInlineMovement(board);
    }

    @Override
    public ArrayList<Move> getLegalMoves(int[][] board, int playerId) {
        if (countPlayerPieces(board, playerId) < 4)
            return getLegalInsertionMoves(board, playerId);
        return getLegalMovementMoves(board, playerId);
    }

    protected ArrayList<Move> getLegalInsertionMoves(int board[][], int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < getBoardWidth(board); x++) {
            for(int y=0; y < getBoardHeight(board); y++) {
                Move newMove = new Move(x, y, playerId);
                if(isLegalMove(newMove, board))
                    moves.add(newMove);
            }
        }
        Collections.shuffle(moves);
        return moves;
    }

    private ArrayList<Move> getLegalMovementMoves(int board[][], int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < getBoardWidth(board); x++) {
            for(int y=0; y < getBoardHeight(board); y++) {
                if (board[x][y] == playerId) {
                    for(int[] eightRegion : new int[][]{{0,1}, {1,1}, {1,0}, {0,-1}, {-1,-1}, {-1, 0}, {1,-1}, {-1,1}})
                        if(isOnBoardLimits(x+eightRegion[0], y+eightRegion[1], board)) {
                            Move newMove = new Move(x, y, x+eightRegion[0], y+eightRegion[1], playerId);
                            if(isLegalMove(newMove, board))
                                moves.add(newMove);
                        }
                }
            }
        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int playerId) {
        if (countPlayerPieces(board, playerId) < 4)
            return new Move(endX, endY, playerId);
        else return new Move(startX, startY, endX, endY, playerId);
    }

    @Override
    public String getRules() {
        return "Em sua vez, o jogador pode inserir uma peça em uma posição vazia do tabuleiro, até atingir 4 peças. Após, pode movimentar uma peça por vez, apenas em áreas adjacentes à atual posição. Ganha aquele que conseguir alinhar 4 peças.";
    }

    @Override
    public float getHeuristicEvaluationOf(int[][] board, int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(board, playerId, turn, this);
    }
}
