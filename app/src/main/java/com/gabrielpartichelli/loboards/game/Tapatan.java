package com.gabrielpartichelli.loboards.game;

import com.gabrielpartichelli.loboards.board.MatrixBoard;
import com.gabrielpartichelli.loboards.move.MatrixMove;
import com.gabrielpartichelli.loboards.player.Player;
import com.gabrielpartichelli.loboards.player.agent.MinimaxAgent;
import com.gabrielpartichelli.loboards.board.Matrix3X3BoardFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tapatan extends MatrixGame {
    public Tapatan() {
        super();
    }

    @Override
    public String getName() {
        return "Tapatan";
    }

    @Override
    public String getRules() {
        return "Cada jogador possui três peças posicionadas no tabuleiro. É permitido apenas o deslocamento das peças para posições adjacentes conectadas por uma das linhas do tabuleiro. Ganha aquele que conseguir alinhar três peças.";
    }

    @Override
    public GameModule getModule() {
        return GameModule.ALIGNMENT_OR_BLOCK;
    }

    @Override
    public MatrixBoard getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2},
                {Player.PLAYER_2, Player.EMPTY, Player.PLAYER_1},
                {Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2}
        };
        return Matrix3X3BoardFactory.from(matrix);
    }

    @Override
    public Game newInstance() {
        return new Tapatan();
    }

    @Override
    public boolean isVictory(int playerId) {
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.setBoard(this.board);
        return ticTacToe.isVictory(playerId);
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMatrixMove(MatrixMove move) {
        return move.getMatrixMovements().size() == 1 && move.getMatrixMovements().get(0).isAdjacentInlineMovement(this.board);
    }

    @Override
    public List<MatrixMove> getLegalMatrixMoves(int playerId) {
        List<MatrixMove> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (this.board.valueAt(x, y) == playerId)
                    for (int[] eightRegion : new int[][]{{0, 1}, {1, 1}, {1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {1, -1}, {-1, 1}})
                        if (this.board.isOnLimits(x + eightRegion[0], y + eightRegion[1])) {
                            MatrixMove newMove = new MatrixMove(x, y, x + eightRegion[0], y + eightRegion[1], playerId);
                            if (isLegalMove(newMove))
                                moves.add(newMove);
                        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public MatrixMove getPlayerMatrixMove(int startX, int startY, int endX, int endY, int playerId) {
        return new MatrixMove(startX, startY, endX, endY, playerId);
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(this, playerId, turn);
    }
}
