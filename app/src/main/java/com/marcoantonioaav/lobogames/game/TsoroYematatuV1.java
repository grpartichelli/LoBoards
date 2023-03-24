package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;

public class TsoroYematatuV1 extends TsoroYematatuV2 {
    @Override
    public String getName() {
        return "Tsoro Yematatu V1";
    }
    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        if (getPlayerPieces(board, move.playerId) < 4) {
            return move.movements.length == 1 && move.movements[0].isInsertion(board);
        }
        return move.movements.length == 1 && board[move.movements[0].endX][move.movements[0].endY] == Player.EMPTY;
    }

    @Override
    public String getRules() {
        return "Em sua vez, o jogador pode inserir uma peça em uma posição vazia do tabuleiro, até atingir 4 peças. Após, pode movimentar uma peça por vez, para qualquer posição do tabuleiro. Ganha aquele que conseguir alinhar 4 peças.";
    }
}
