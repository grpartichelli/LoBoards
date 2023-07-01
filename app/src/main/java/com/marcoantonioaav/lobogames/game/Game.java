package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;

public abstract class Game {

    protected Board board;

    protected Game() {
        board = getInitialBoard();
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public abstract String getName();

    public abstract Board getInitialBoard();

    public abstract String getRules();

    public abstract boolean isVictory(int playerId);

    public abstract boolean isDraw();

    public abstract boolean isLegalMove(Move move);

    public abstract ArrayList<Move> getLegalMoves(int playerId);

    public abstract Move getPlayerMove(int startX, int startY, int endX, int endY, int playerId);

    /**
     * Método que retorna a avaliação heurística de um estado do jogo,
     * utilizado pelo agente MinimaxAgent.
     *
     * @param playerId id do agente
     * @param turn     id do player da rodada (i.e. que fará a próxima jogada)
     * @return float - Avaliação no intervalo [MinimaxAgent.MIN, MinimaxAgent.MAX].
     * Dica: pode ser utilizado MinimaxAgent.normalizeToEvaluationLimits().
     */
    public abstract float getHeuristicEvaluationOf(int playerId, int turn);

    public Game copy() {
        // TODO: Implement copy
        return new Tapatan();
    }

    public boolean isTerminalState() {
        return isVictory(Player.PLAYER_1) || isVictory(Player.PLAYER_2) || isDraw();
    }
}
