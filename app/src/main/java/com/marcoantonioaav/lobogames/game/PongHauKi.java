package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.GenericPongHauKiBoardFactory;
import com.marcoantonioaav.lobogames.move.GenericMove;
import com.marcoantonioaav.lobogames.move.GenericMovement;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PongHauKi extends GenericGame {
    @Override
    public String getName() {
        return "Pong Hau K'i";
    }

    @Override
    public Board getInitialBoard() {
        return GenericPongHauKiBoardFactory.from(
                Arrays.asList(Player.PLAYER_1, Player.PLAYER_2, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_1)
        );
    }

    @Override
    public String getRules() {
        // TODO: rules
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
            && position.hasAnyEmptyConnectedPositions()
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
        for (Movement movement : move.getMovements()) {
            if (movement.getStartPosition().isOutOfBoard()
                || movement.getEndPosition().isOutOfBoard()
                || !movement.getStartPosition().isConnectedTo(movement.getEndPosition())
                || movement.getStartPosition().getPlayerId() != movement.getPlayerId()
                || movement.getEndPosition().getPlayerId() != Player.EMPTY
            ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Move> getLegalMoves(int playerId) {
        List<Move> moves = new ArrayList<>();
        for (Position position : board.getPositions()) {
            if (position.getPlayerId() == playerId) {
                for (Position emptyConnectedPosition : position.findAllEmptyConnectedPosition()) {
                    GenericMovement movement = new GenericMovement(position, emptyConnectedPosition, playerId);
                    moves.add(new GenericMove(playerId, new ArrayList<>(Collections.singletonList(movement))));
                }
            }
        }
        return moves;
    }

    // TODO: Fix computer

    @Override
    public Move getPlayerMove(Position startPosition, Position endPosition, int playerId) {
        if (!startPosition.isConnectedTo(endPosition)) {
            List<Position> possibleStarts = endPosition.findAllConnectedPositionsForPlayerId(playerId);
            if (possibleStarts.size() == 1) {
                startPosition = possibleStarts.get(0);
            }
        }
        GenericMovement movement = new GenericMovement(startPosition, endPosition, playerId);
        return new GenericMove(playerId, new ArrayList<>(Collections.singletonList(movement)));
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(this, playerId, turn);
    }
}
