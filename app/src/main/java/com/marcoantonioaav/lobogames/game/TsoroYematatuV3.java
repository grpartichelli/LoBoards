package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.move.Move;

public class TsoroYematatuV3 extends TsoroYematatuV2 {
    @Override
    public String getName() {
        return "Tsoro Yematatu V3";
    }
    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        if (countPlayerPieces(board, move.playerId) < 4) {
            return move.movements.length == 1 && move.movements[0].isInsertion(board);
        }
        return (move.movements.length == 1 && move.movements[0].isAdjacentInlineMovement(board));
                //|| (move.movements.length == 1 && move.movements[0].isAdjacentInlineOrOpponentJump(board));
    }

    @Override
    public String getRules() {
        return "Em sua vez, o jogador pode inserir uma peça em uma posição vazia do tabuleiro, até atingir 4 peças. Após, pode movimentar uma peça por vez, para posições vizinhas, ou pulando as suas peças ou as do oponente, caso o outro lado esteja vazio, porém sempre seguindo as linhas do tabuleiro. Ganha aquele que conseguir alinhar 4 peças.";
    }
}
