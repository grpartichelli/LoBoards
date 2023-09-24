package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.GenericBoard;
import com.marcoantonioaav.lobogames.board.GenericWatermelonChessBoardFactory;
import com.marcoantonioaav.lobogames.move.GenericMove;
import com.marcoantonioaav.lobogames.move.GenericMovement;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WatermelonChess extends GenericGame {
    @Override
    public String getName() {
        return "Watermelon Chess";
    }

    @Override
    public Board getInitialBoard() {
        return GenericWatermelonChessBoardFactory.from(
                Arrays.asList(
                        Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_1,
                        Player.PLAYER_1, Player.EMPTY, Player.PLAYER_1,
                        Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY,
                        Player.PLAYER_2, Player.EMPTY, Player.PLAYER_2,
                        Player.PLAYER_2, Player.PLAYER_2, Player.PLAYER_2, Player.PLAYER_2
                        )
        );
    }

    @Override
    public String getRules() {
        // TODO: rules
        return "";
    }

    @Override
    public Game newInstance() {
        return new WatermelonChess();
    }

    @Override
    public boolean isVictory(int playerId) {
        return !this.board.hasAnyPositionsWithPlayerId(Player.getOpponentOf(playerId));
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
                    GenericMovement movement = new GenericMovement(position, emptyConnectedPosition, playerId);
                    moves.add(new GenericMove(playerId, calculateAllMovements(movement)));
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

        GenericMovement movement = new GenericMovement(startPosition, endPosition, playerId);
        return new GenericMove(playerId, calculateAllMovements(movement));
    }

    private List<GenericMovement> calculateAllMovements(GenericMovement movement) {
        Position endPosition = this.board.findPositionById(movement.getEndPositionId());
        List<Position> connectedEnemyPositions = this.board.findConnectedPositionsWithPlayerId(
                endPosition,
                Player.getOpponentOf(movement.getPlayerId())
        );
        List<GenericMovement> movements = new ArrayList<>();
        movements.add(movement);
        for (Position enemyPosition: connectedEnemyPositions) {
            boolean shouldAddMovement = true;
            for (Position connectedPositionToEnemy: this.board.findConnectedPositions(enemyPosition)) {
                if (connectedPositionToEnemy.getPlayerId() != movement.getPlayerId() && !connectedPositionToEnemy.equals(endPosition)) {
                    shouldAddMovement = false;
                    break;
                }
            }

            if (shouldAddMovement) {
                movements.add(new GenericMovement(enemyPosition, Position.instanceOutOfBoard(), movement.getPlayerId()));
            }
        }
        return movements;
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        return MinimaxAgent.evaluateWithPlayouts(this, playerId, turn);
    }
}
