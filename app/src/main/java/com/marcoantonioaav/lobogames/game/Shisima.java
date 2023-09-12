package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.GenericCircularBoardFactory;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Shisima extends GenericGame {
    @Override
    public String getName() {
        return "Shisima";
    }

    @Override
    public Board getInitialBoard() {
        return GenericCircularBoardFactory.from(
                Arrays.asList(
                        Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2,
                        Player.PLAYER_2, Player.EMPTY, Player.PLAYER_1,
                        Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2
                ));
    }

    @Override
    public String getRules() {
        // TODO: Rules
        return null;
    }

    @Override
    public Game newInstance() {
        return new Shisima();
    }

    @Override
    public boolean isVictory(int playerId) {
        Set<String> positionIds = new HashSet<>(this.board.findAllPositionsIdsForPlayerId(playerId));

        return positionIds.contains(GenericCircularBoardFactory.CENTER)
                && (positionIds.containsAll(
                        Arrays.asList(GenericCircularBoardFactory.TOP, GenericCircularBoardFactory.BOTTOM))
                || positionIds.containsAll(
                        Arrays.asList(GenericCircularBoardFactory.TOP_LEFT, GenericCircularBoardFactory.BOTTOM_RIGHT))
                || positionIds.containsAll(
                        Arrays.asList(GenericCircularBoardFactory.TOP_RIGHT, GenericCircularBoardFactory.BOTTOM_LEFT))
                || positionIds.containsAll(
                Arrays.asList(GenericCircularBoardFactory.LEFT, GenericCircularBoardFactory.RIGHT)
        ));

    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move) {
        for (Movement movement : move.getMovements()) {
            Position startPosition = this.board.findPositionById(movement.getStartPositionId());
            Position endPosition = this.board.findPositionById(movement.getEndPositionId());

            if (startPosition.isOutOfBoard()
                    || endPosition.isOutOfBoard()
                    || !startPosition.isConnectedTo(endPosition)
                    || startPosition.getPlayerId() != movement.getPlayerId()
                    || endPosition.getPlayerId() != Player.EMPTY
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
                for (Position emptyConnectedPosition : this.board.findEmptyConnectedPositions(position)) {
                    GenericMovement movement = new GenericMovement(position, emptyConnectedPosition, playerId);
                    moves.add(new GenericMove(playerId, new ArrayList<>(Collections.singletonList(movement))));
                }
            }
        }
        return moves;
    }

    @Override
    public Move getPlayerMove(String startPositionId, String endPositionId, int playerId) {
        Position startPosition = this.board.findPositionById(startPositionId);
        Position endPosition = this.board.findPositionById(endPositionId);

        if (!startPosition.isConnectedTo(endPosition)) {
            List<Position> possibleStarts = this.board.findConnectedPositionsForPlayerId(endPosition, playerId);
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
