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

public class FiveFieldKono extends Game<MatrixBoard> {

    public FiveFieldKono() {
        super();
    }

    @Override
    public String getName() {
        return "Five Field Kono";
    }

    @Override
    public String getRules() {
        return "Cada jogador possui sete peças posicionadas no tabuleiro. É permitido apenas o deslocamento das peças para posições adjacentes conectadas por uma das linhas do tabuleiro. O jogador ganha caso as posições iniciais do oponente estejam completamente preenchidas por peças, sejam suas ou do oponente.";
    }

    @Override
    public MatrixBoard getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.PLAYER_1, Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.PLAYER_2},
                {Player.PLAYER_1, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.PLAYER_2},
                {Player.PLAYER_1, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_2}
        };
        Drawable image = ContextCompat.getDrawable(LoBoGames.getAppContext(), TestConstants.IMAGE_ID_5X5);
        return new MatrixBoard(matrix, image);
    }

    @Override
    public Game<MatrixBoard> newInstance() {
        return new FiveFieldKono();
    }

    @Override
    public boolean isVictory(int playerId) {
        List<Move> moves = getLegalMoves(Player.getOpponentOf(playerId));
        if (moves.isEmpty())
            return true;

        if (isPlayerOnInitialPosition(this.board, Player.PLAYER_1) || isPlayerOnInitialPosition(this.board, Player.PLAYER_2))
            return false;

        MatrixBoard initialBoard = getInitialBoard();
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (initialBoard.valueAt(x, y) == Player.getOpponentOf(playerId) && board.valueAt(x, y) == Player.EMPTY)
                    return false;
        return true;
    }

    private boolean isPlayerOnInitialPosition(MatrixBoard otherBoard, int playerId) {
        MatrixBoard initialBoard = getInitialBoard();
        for (int x = 0; x < initialBoard.getWidth(); x++)
            for (int y = 0; y < initialBoard.getHeight(); y++)
                if ((otherBoard.valueAt(x, y) == playerId && initialBoard.valueAt(x, y) != playerId)
                        || (initialBoard.valueAt(x, y) == playerId && otherBoard.valueAt(x, y) != playerId))
                    return false;
        return true;
    }


    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move) {
        MatrixBoard newBoard = this.board.copy();
        newBoard.applyMovement(move.movements.get(0));
        if (isPlayerOnInitialPosition(newBoard, move.playerId))
            return false;

        return move.movements.size() == 1 && move.movements.get(0).isAdjacentInlineMovement(board);
    }

    @Override
    public List<Move> getLegalMoves(int playerId) {
        List<Move> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (this.board.valueAt(x, y) == playerId)
                    for (int[] eightRegion : new int[][]{{0, 1}, {1, 1}, {1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {1, -1}, {-1, 1}})    // Can be reduced to only the diagonals to improve efficiency, but the movement style may be changed to lines eventually
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
