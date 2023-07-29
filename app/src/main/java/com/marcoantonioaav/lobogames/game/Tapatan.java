package com.marcoantonioaav.lobogames.game;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;
import com.marcoantonioaav.lobogames.testconstants.TestConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tapatan extends Game<MatrixBoard> {
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
    public MatrixBoard getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2},
                {Player.PLAYER_2, Player.EMPTY, Player.PLAYER_1},
                {Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2}
        };
        Drawable image = ContextCompat.getDrawable(LoBoGames.getAppContext(), TestConstants.IMAGE_ID_3X3);
        return new MatrixBoard(matrix, image);
    }

    @Override
    public Game<MatrixBoard> newInstance() {
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
    public boolean isLegalMove(Move move) {
        return move.movements.size() == 1 && move.movements.get(0).isAdjacentInlineMovement(this.board);
    }

    @Override
    public List<Move> getLegalMoves(int playerId) {
        List<Move> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (this.board.valueAt(x, y) == playerId)
                    for (int[] eightRegion : new int[][]{{0, 1}, {1, 1}, {1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {1, -1}, {-1, 1}})
                        if (this.board.isOnLimits(x + eightRegion[0], y + eightRegion[1])) {
                            Move newMove = new Move(x, y, x + eightRegion[0], y + eightRegion[1], playerId);
                            if (isLegalMove(newMove))
                                moves.add(newMove);
                        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int playerId) {
        return new Move(startX, startY, endX, endY, playerId);
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(this, playerId, turn);
    }
}
