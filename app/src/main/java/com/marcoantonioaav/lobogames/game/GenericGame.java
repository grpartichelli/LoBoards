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
    private final Board initalBoard;

    public GenericGame(Board board) {
        this.initalBoard = board;
        restart();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Board getInitialBoard() {
        if (initalBoard == null) {
            return null;
        }
        return initalBoard.copy();
    }

    @Override
    public String getRules() {
        // NOTE: this game doesn't implement this method because it has no specific rules
        throw new UnsupportedOperationException();
    }

    @Override
    public Game newInstance() {
        return new GenericGame(initalBoard);
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

        // TODO: Consider out of board
        //      !startPosition.isOutOfBoard()
        //      && !endPosition.isOutOfBoard())
        //      && startPosition.getPlayerId() == movement.getPlayerId()
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
        StandardMovement movement = new StandardMovement(startPosition, endPosition, playerId);
        return new StandardMove(playerId, new ArrayList<>(Collections.singletonList(movement)));
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        // NOTE: this game doesn't implement this method due to not being used by the AI
        throw new UnsupportedOperationException();
    }
}
