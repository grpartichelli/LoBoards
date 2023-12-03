package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.move.StandardMove;
import com.marcoantonioaav.lobogames.move.StandardMovement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Game implementation that works for any board by
 * allowing most moves and not having any specific win condition
 */
public class GenericGame extends StandardGame {

    public static final String NAME = "Generic";

    public GenericGame(Board board) {
        super();
        setBoard(board);
        restart();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Board getInitialBoard() {
        if (this.board == null) {
            return null;
        }

        for (Position position : this.board.getPositions()) {
            position.setPlayerId(Player.EMPTY);
        }
        return board;
    }

    @Override
    public String getRules() {
        // NOTE: this game doesn't implement this method because it has no specific rules
        throw new UnsupportedOperationException();
    }

    @Override
    public Game newInstance() {
        return new GenericGame(board);
    }

    @Override
    public boolean isVictory(int playerId) {
        // NOTE: this game doesn't implement this method because the victory is set manually
        return false;
    }

    @Override
    public boolean isDraw() {
        // NOTE: this game doesn't implement this method because the victory is set manually
        return false;
    }

    @Override
    public boolean isLegalMove(Move move) {
        Movement movement = move.getMovements().get(0);
        Position startPosition = this.board.findPositionById(movement.getStartPositionId());
        Position endPosition = this.board.findPositionById(movement.getEndPositionId());

        if (startPosition.equals(Position.instanceOutOfBoard()) || endPosition.equals(Position.instanceOutOfBoard())) {
            return false;
        }

        if (startPosition.equals(endPosition)) {
            return false;
        }

        if (startPosition.getPlayerId() == Player.EMPTY) {
            return false;
        }

        if (endPosition.isOutOfBoard()) {
            return startPosition.getPlayerId() != Player.EMPTY
                    && startPosition.getPlayerId() == endPosition.getPlayerId();
        }
        return endPosition.getPlayerId() == Player.EMPTY;
    }

    @Override
    public List<Move> getLegalMoves(int playerId) {
        // NOTE: this game doesn't implement this method due to not being used by the AI
        throw new UnsupportedOperationException();
    }

    @Override
    public Move getPlayerMove(String startPositionId, String endPositionId, int playerId) {
        Position startPosition = this.board.findPositionById(startPositionId);
        Position endPosition = this.board.findPositionById(endPositionId);

//        if (endPosition.isOutOfBoard() && endPosition.getPlayerId() == startPosition.getPlayerId()) {
//            return new StandardMove(playerId, new ArrayList<>(Arrays.asList(
//                    new StandardMovement(startPosition, endPosition, startPosition.getPlayerId()),
//                    new StandardMovement(endPosition, endPosition, startPosition.getPlayerId())
//            )));
//        }

        // NOTE: Consider the moves player as the start position since we don't know who is acting
        StandardMovement movement = new StandardMovement(startPosition, endPosition, startPosition.getPlayerId());
        return new StandardMove(playerId, new ArrayList<>(Collections.singletonList(movement)));
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        // NOTE: this game doesn't implement this method due to not being used by the AI
        throw new UnsupportedOperationException();
    }
}
