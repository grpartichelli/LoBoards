package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.Collections;

public class TsoroYematatuV1 extends TsoroYematatuV2 {
    @Override
    public String getName() {
        return "Tsoro Yematatu V1";
    }
    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        if (countPlayerPieces(board, move.playerId) < 4)
            return move.movements.length == 1 && move.movements[0].isInsertion(board);
        return move.movements.length == 1 && board[move.movements[0].endX][move.movements[0].endY] == Player.EMPTY;
    }

    @Override
    public ArrayList<Move> getLegalMoves(int[][] board, int playerId) {
        if (countPlayerPieces(board, playerId) < 4)
            return getLegalInsertionMoves(board, playerId);
        return getLegalMovementMoves(board, playerId);
    }

    private ArrayList<Move> getLegalMovementMoves(int board[][], int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < getBoardWidth(board); x++) {
            for(int y=0; y < getBoardHeight(board); y++) {
                if (board[x][y] == playerId) {
                    for(int[] eightRegion : new int[][]{{0,1}, {1,1}, {1,0}, {0,-1}, {-1,-1}, {-1, 0}, {1,-1}, {-1,1}, {0,2}, {2,2}, {2,0}, {0,-2}, {-2,-2}, {-2, 0}, {2,-2}, {-2,2}})
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
    public String getRules() {
        return "Em sua vez, o jogador pode inserir uma peça em uma posição vazia do tabuleiro, até atingir 4 peças. Após, pode movimentar uma peça por vez, para qualquer posição do tabuleiro. Ganha aquele que conseguir alinhar 4 peças.";
    }
}
