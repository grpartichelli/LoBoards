package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.PongHauKiBoardFactory;
import com.marcoantonioaav.lobogames.move.StandardMove;
import com.marcoantonioaav.lobogames.move.StandardMovement;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PongHauKi extends StandardGame {
    @Override
    public String getName() {
        return "Pong Hau K'i";
    }

    @Override
    public Board getInitialBoard() {
        return PongHauKiBoardFactory.from(
                Arrays.asList(Player.PLAYER_1, Player.PLAYER_2, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_1)
        );
    }

    @Override
    public String getRules() {
        // TODO: rules
        return "Cada jogador inicia com duas peças posicionadas em lados opostos de um tabuleiro. Cada jogador pode mover uma peça para uma posição vazia conectada a ela. O jogador que conseguir impedir que o inimigo mova suas peças bloqueando todas as posições adjacentes ganha.";
    }

    @Override
    public int getMaxPlayerPositionsCount() {
        return 2;
    }

    @Override
    public String getTextUrl() {
        return "";
    }

    @Override
    public String getVideoUrl() {
        return "";
    }

    @Override
    public Game newInstance() {
        return new PongHauKi();
    }

    @Override
    public boolean isVictory(int playerId) {
        int opponentPlayerId = Player.getOpponentOf(playerId);

        for (Position position : board.getPositions()) {
            if (position.getPlayerId() == opponentPlayerId
            && this.board.hasAnyEmptyConnectedPositions(position)
            ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move) {
        Movement movement = move.getMovements().get(0);
        Position startPosition = this.board.findPositionById(movement.getStartPositionId());
        Position endPosition = this.board.findPositionById(movement.getEndPositionId());

        return !startPosition.isOutOfBoard()
                && !endPosition.isOutOfBoard()
                && this.board.areConnected(startPosition, endPosition)
                && startPosition.getPlayerId() == movement.getPlayerId()
                && endPosition.getPlayerId() == Player.EMPTY;
    }

    @Override
    public List<Move> getLegalMoves(int playerId) {
        List<Move> moves = new ArrayList<>();
        for (Position position : board.getPositions()) {
            if (position.getPlayerId() == playerId) {
                for (Position emptyConnectedPosition : this.board.findEmptyConnectedPositions(position)) {
                    StandardMovement movement = new StandardMovement(position, emptyConnectedPosition, playerId);
                    moves.add(new StandardMove(playerId, new ArrayList<>(Collections.singletonList(movement))));
                }
            }
        }
        return moves;
    }

    @Override
    public Move getPlayerMove(String startPositionId, String endPositionId, int playerId) {
        Position startPosition = this.board.findPositionById(startPositionId);
        Position endPosition = this.board.findPositionById(endPositionId);

        if (!this.board.areConnected(startPosition, endPosition)) {
            List<Position> possibleStarts = this.board.findConnectedPositionsWithPlayerId(endPosition, playerId);
            if (possibleStarts.size() == 1) {
                startPosition = possibleStarts.get(0);
            }
        }
        StandardMovement movement = new StandardMovement(startPosition, endPosition, playerId);
        return new StandardMove(playerId, new ArrayList<>(Collections.singletonList(movement)));
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(this, playerId, turn);
    }
}
