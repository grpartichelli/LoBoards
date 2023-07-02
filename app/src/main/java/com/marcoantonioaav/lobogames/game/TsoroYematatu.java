package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;

import java.util.ArrayList;
import java.util.Collections;

public class TsoroYematatu extends Game {

    public TsoroYematatu() {
        super();
    }

    @Override
    public String getName() {
        return "Tsoro Yematatu";
    }

    @Override
    public String getRules() {
        return "Em sua vez, o jogador pode inserir uma peça em uma posição vazia do tabuleiro, até atingir 4 peças. Após, pode movimentar uma peça por vez, apenas em áreas adjacentes à atual posição. Ganha aquele que conseguir alinhar 4 peças.";
    }

    @Override
    public Board getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY}
        };
        int boardImageId = R.drawable._5x5;
        return new Board(matrix, boardImageId);
    }


    @Override
    public boolean isVictory(int playerId) {
        return isLineVictory(playerId) || isColumnVictory(playerId) || isDiagonalVictory(playerId);
    }

    private boolean isLineVictory(int player) {
        int[][] matrix = this.board.getMatrix();
        for (int y = 0; y < this.board.getHeight(); y++)
            if ((matrix[0][y] == player && matrix[1][y] == player && matrix[2][y] == player && matrix[3][y] == player) ||
                    (matrix[1][y] == player && matrix[2][y] == player && matrix[3][y] == player && matrix[4][y] == player))
                return true;
        return false;
    }

    private boolean isColumnVictory(int player) {
        int[][] matrix = this.board.getMatrix();
        for (int x = 0; x < this.board.getWidth(); x++)
            if ((matrix[x][0] == player && matrix[x][1] == player && matrix[x][2] == player && matrix[x][3] == player) ||
                    (matrix[x][1] == player && matrix[x][2] == player && matrix[x][3] == player && matrix[x][4] == player))
                return true;
        return false;
    }

    private boolean isDiagonalVictory(int player) {
        int[][] matrix = this.board.getMatrix();
        return ((matrix[1][1] == player && matrix[2][2] == player && matrix[3][3] == player && matrix[4][4] == player) ||
                (matrix[0][0] == player && matrix[1][1] == player && matrix[2][2] == player && matrix[3][3] == player) ||
                (matrix[0][4] == player && matrix[1][3] == player && matrix[2][2] == player && matrix[3][1] == player) ||
                (matrix[1][3] == player && matrix[2][2] == player && matrix[3][1] == player && matrix[4][0] == player));
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move) {
        if (this.board.countPlayerPieces(move.playerId) < 4)
            return move.movements.size() == 1 && move.movements.get(0).isInsertion(board);
        return move.movements.size() == 1 && move.movements.get(0).isAdjacentInlineMovement(board);
    }

    @Override
    public ArrayList<Move> getLegalMoves(int playerId) {
        if (this.board.countPlayerPieces(playerId) < 4)
            return getLegalInsertionMoves(playerId);
        return getLegalMovementMoves(playerId);
    }

    protected ArrayList<Move> getLegalInsertionMoves(int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++) {
            for (int y = 0; y < this.board.getHeight(); y++) {
                Move newMove = new Move(x, y, playerId);
                if (isLegalMove(newMove))
                    moves.add(newMove);
            }
        }
        Collections.shuffle(moves);
        return moves;
    }

    private ArrayList<Move> getLegalMovementMoves(int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++) {
            for (int y = 0; y < this.board.getHeight(); y++) {
                if (this.board.getMatrix()[x][y] == playerId) {
                    for (int[] eightRegion : new int[][]{{0, 1}, {1, 1}, {1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {1, -1}, {-1, 1}})
                        if (this.board.isOnLimits(x + eightRegion[0], y + eightRegion[1])) {
                            Move newMove = new Move(x, y, x + eightRegion[0], y + eightRegion[1], playerId);
                            if (isLegalMove(newMove))
                                moves.add(newMove);
                        }
                }
            }
        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int playerId) {
        if (this.board.countPlayerPieces(playerId) < 4)
            return new Move(endX, endY, playerId);
        else return new Move(startX, startY, endX, endY, playerId);
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(this, playerId, turn);
    }
}
