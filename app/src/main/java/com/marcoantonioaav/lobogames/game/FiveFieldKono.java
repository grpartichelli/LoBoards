package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;

import java.util.ArrayList;
import java.util.Collections;

public class FiveFieldKono extends Game {

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
    public Board getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.PLAYER_1, Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.PLAYER_2},
                {Player.PLAYER_1, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.PLAYER_2},
                {Player.PLAYER_1, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_2}
        };
        int boardImageId = R.drawable._5x5;
        return new Board(matrix, boardImageId);
    }

    @Override
    public boolean isVictory(int playerId) {
        ArrayList<Move> moves = getLegalMoves(Player.getOpponentOf(playerId));
        if (moves.isEmpty())
            return true;

        if (isPlayerOnInitialPosition(this.board, Player.PLAYER_1) || isPlayerOnInitialPosition(this.board, Player.PLAYER_2))
            return false;

        int[][] initialBoardMatrix = getInitialBoard().getMatrix();
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (initialBoardMatrix[x][y] == Player.getOpponentOf(playerId) && board.getMatrix()[x][y] == Player.EMPTY)
                    return false;
        return true;
    }

    private boolean isPlayerOnInitialPosition(Board otherBoard, int playerId) {
        int[][] initialBoardMatrix = getInitialBoard().getMatrix();
        int[][] otherBoardMatrix = otherBoard.getMatrix();
        for (int x = 0; x < otherBoard.getWidth(); x++)
            for (int y = 0; y < otherBoard.getHeight(); y++)
                if ((otherBoardMatrix [x][y] == playerId && initialBoardMatrix[x][y] != playerId)
                        || (initialBoardMatrix[x][y] == playerId && otherBoardMatrix[x][y] != playerId))
                    return false;
        return true;
    }


    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move) {
        Board newBoard = this.board.copy();
        newBoard.applyMovement(move.movements[0]);
        if (isPlayerOnInitialPosition(newBoard, move.playerId))
            return false;

        return move.movements.length == 1 && move.movements[0].isAdjacentInlineMovement(board);
    }

    @Override
    public ArrayList<Move> getLegalMoves(int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (this.board.getMatrix()[x][y] == playerId)
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
