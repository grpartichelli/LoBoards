package com.marcoantonioaav.lobogames.game;

import androidx.annotation.Nullable;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.List;

public abstract class Game {

    protected Game() {
        restart();
    }

    public abstract Board getBoard();
    public abstract void setBoard(Board board);

    public abstract String getName();

    public abstract Board getInitialBoard();

    public abstract String getRules();

    public abstract Game newInstance();

    public Game copy() {
        Game newGame = this.newInstance();
        Board newBoard = this.getBoard().copy();
        newGame.setBoard(newBoard);
        return newGame;
    }

    public abstract boolean isVictory(int playerId);

    public abstract boolean isDraw();

    public abstract boolean isLegalMove(Move move);

    public abstract List<Move> getLegalMoves(int playerId);

    public abstract Move getPlayerMove(String startPositionId, String endPositionId, int playerId);

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

    public boolean isTerminalState() {
        return isVictory(Player.PLAYER_1) || isVictory(Player.PLAYER_2) || isDraw();
    }

    public void restart() {
        setBoard(getInitialBoard());
    }
}
