package com.gabrielpartichelli.loboards.game;

import com.gabrielpartichelli.loboards.board.Board;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.move.Movement;
import com.gabrielpartichelli.loboards.move.StandardMove;
import com.gabrielpartichelli.loboards.move.StandardMovement;
import com.gabrielpartichelli.loboards.player.Player;
import com.gabrielpartichelli.loboards.position.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Game implementation that works for any board by
 * allowing most moves and not having any specific win condition
 */
public class GenericGame extends StandardGame {

    private String name = "";
    private int maxPlayerPositionsCount = 0;
    private String videoUrl = "";
    private String textUrl = "";
    private GameModule module = GameModule.UNDEFINED;

    public GenericGame(Board board) {
        super();
        setBoard(board);
        restart();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public int getMaxPlayerPositionsCount() {
        return maxPlayerPositionsCount;
    }

    @Override
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String getTextUrl() {
        return textUrl;
    }

    public void setTextUrl(String textUrl) {
        this.textUrl = textUrl;
    }

    @Override
    public GameModule getModule() {
        return module;
    }

    public void setModule(GameModule module) {
        this.module = module;
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

        boolean isOutOfMoves = this.board.countPlayerPieces(movement.getPlayerId()) >= maxPlayerPositionsCount;
        if (isOutOfMoves && startPosition.isOutOfBoard()) {
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
        // NOTE: Consider the moves player as the start position since we don't know who is acting
        StandardMovement movement = new StandardMovement(startPosition, endPosition, startPosition.getPlayerId());
        return new StandardMove(playerId, new ArrayList<>(Collections.singletonList(movement)));
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        // NOTE: this game doesn't implement this method due to not being used by the AI
        throw new UnsupportedOperationException();
    }

    public void setMaxPlayerPositionsCount(int maxPlayerPositionsCount) {
        this.maxPlayerPositionsCount = maxPlayerPositionsCount;
    }
}
