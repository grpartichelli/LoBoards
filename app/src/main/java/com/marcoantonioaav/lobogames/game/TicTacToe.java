package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;

import java.util.ArrayList;
import java.util.Collections;

public class TicTacToe extends Game<MatrixBoard> {

    public TicTacToe() {
        super();
    }

    @Override
    public String getName() {
        return "Jogo da Velha";
    }

    @Override
    public String getRules() {
        return "Em sua vez, o jogador pode inserir uma peça em uma posição vazia do tabuleiro. Ganha aquele que conseguir alinhar três peças.";
    }

    @Override
    public MatrixBoard getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY}
        };
        int boardImageId = R.drawable._3x3;
        return new MatrixBoard(matrix, boardImageId);
    }

    @Override
    public Game<MatrixBoard> newInstance() {
        return new TicTacToe();
    }

    @Override
    public boolean isVictory(int playerId) {
        return isLineVictory(playerId) || isColumnVictory(playerId) || isDiagonalVictory(playerId);
    }

    private boolean isDiagonalVictory(int player) {
        return (this.board.valueAt(0, 0) == player
                && this.board.valueAt(1, 1) == player
                && this.board.valueAt(2, 2) == player)
                || (this.board.valueAt(0, 2) == player
                && this.board.valueAt(1, 1) == player
                && this.board.valueAt(2, 0) == player);
    }

    private boolean isColumnVictory(int player) {
        for (int x = 0; x < 3; x++)
            if (this.board.valueAt(x, 0) == player
                    && this.board.valueAt(x, 1) == player
                    && this.board.valueAt(x, 2) == player)
                return true;
        return false;
    }

    private boolean isLineVictory(int player) {
        for (int y = 0; y < 3; y++)
            if (this.board.valueAt(0, y) == player
                    && this.board.valueAt(1, y) == player
                    && this.board.valueAt(2, y) == player)
                return true;
        return false;
    }

    @Override
    public boolean isDraw() {
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                if (this.board.valueAt(x, y) == Player.EMPTY)
                    return false;
        return !isVictory(Player.PLAYER_1) && !isVictory(Player.PLAYER_2);
    }

    @Override
    public boolean isLegalMove(Move move) {
        return move.movements.size() == 1 && move.movements.get(0).isInsertion(this.board);
    }

    @Override
    public ArrayList<Move> getLegalMoves(int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Move newMove = new Move(x, y, playerId);
                if (isLegalMove(newMove))
                    moves.add(newMove);
            }
        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int playerId) {
        return new Move(endX, endY, playerId);
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(this, playerId, turn);
    }
}
