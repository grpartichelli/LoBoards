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

public class TsoroYematatu extends MatrixGame {

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
    public MatrixBoard getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY}
        };
        Drawable image = ContextCompat.getDrawable(LoBoGames.getAppContext(), TestConstants.IMAGE_ID_5X5);
        return new MatrixBoard(matrix, image);
    }

    @Override
    public Game newInstance() {
        return new TsoroYematatu();
    }

    @Override
    public boolean isVictory(int playerId) {
        return isLineVictory(playerId) || isColumnVictory(playerId) || isDiagonalVictory(playerId);
    }

    private boolean isLineVictory(int player) {
        for (int y = 0; y < this.board.getHeight(); y++)
            if ((this.board.valueAt(0, y) == player
                    && this.board.valueAt(1, y) == player
                    && this.board.valueAt(2, y) == player
                    && this.board.valueAt(3, y) == player)
                    || (this.board.valueAt(1, y) == player
                    && this.board.valueAt(2, y) == player
                    && this.board.valueAt(3, y) == player
                    && this.board.valueAt(4, y) == player)
            )
                return true;
        return false;
    }

    private boolean isColumnVictory(int player) {
        for (int x = 0; x < this.board.getWidth(); x++)
            if ((this.board.valueAt(x, 0) == player
                    && this.board.valueAt(x, 1) == player
                    && this.board.valueAt(x, 2) == player
                    && this.board.valueAt(x, 3) == player)
                    || (this.board.valueAt(x, 1) == player
                    && this.board.valueAt(x, 2) == player
                    && this.board.valueAt(x, 3) == player
                    && this.board.valueAt(x, 4) == player))
                return true;
        return false;
    }

    private boolean isDiagonalVictory(int player) {
        return ((this.board.valueAt(1, 1) == player
                && this.board.valueAt(2, 2) == player
                && this.board.valueAt(3, 3) == player
                && this.board.valueAt(4, 4) == player)
                || (this.board.valueAt(0, 0) == player
                && this.board.valueAt(1, 1) == player
                && this.board.valueAt(2, 2) == player
                && this.board.valueAt(3, 3) == player)
                || (this.board.valueAt(0, 4) == player
                && this.board.valueAt(1, 3) == player
                && this.board.valueAt(2, 2) == player
                && this.board.valueAt(3, 1) == player)
                || (this.board.valueAt(1, 3) == player
                && this.board.valueAt(2, 2) == player
                && this.board.valueAt(3, 1) == player
                && this.board.valueAt(4, 0) == player));
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
    public List<Move> getLegalMoves(int playerId) {
        if (this.board.countPlayerPieces(playerId) < 4)
            return getLegalInsertionMoves(playerId);
        return getLegalMovementMoves(playerId);
    }

    protected List<Move> getLegalInsertionMoves(int playerId) {
        List<Move> moves = new ArrayList<>();
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

    private List<Move> getLegalMovementMoves(int playerId) {
        List<Move> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++) {
            for (int y = 0; y < this.board.getHeight(); y++) {
                if (this.board.valueAt(x, y) == playerId) {
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
