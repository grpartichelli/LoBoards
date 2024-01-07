package com.gabrielpartichelli.loboards.game;

import com.gabrielpartichelli.loboards.board.Board;
import com.gabrielpartichelli.loboards.board.WatermelonChessBoardFactory;
import com.gabrielpartichelli.loboards.move.StandardMove;
import com.gabrielpartichelli.loboards.move.StandardMovement;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.move.Movement;
import com.gabrielpartichelli.loboards.player.Player;
import com.gabrielpartichelli.loboards.player.agent.MinimaxAgent;
import com.gabrielpartichelli.loboards.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WatermelonChess extends StandardGame {
    @Override
    public String getName() {
        return "Watermelon Chess";
    }

    @Override
    public Board getInitialBoard() {
        return WatermelonChessBoardFactory.from(
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
        return "Cada jogador pode mover uma peça para uma posição vazia conectada a ela. Se uma peça estiver cercada por todos os lados por peças inimigas ela é capturada e removida da partida. Ganha o jogador que conseguir capturar todas as peças do inimigo.";
    }

    @Override
    public GameModule getModule() {
        return GameModule.CAPTURE;
    }

    @Override
    public int getMaxPlayerPositionsCount() {
        return 6;
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
                    StandardMovement movement = new StandardMovement(position, emptyConnectedPosition, playerId);
                    moves.add(new StandardMove(playerId, calculateAllMovements(movement)));
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
        return new StandardMove(playerId, calculateAllMovements(movement));
    }

    private List<StandardMovement> calculateAllMovements(StandardMovement movement) {
        Position startPosition = this.board.findPositionById(movement.getStartPositionId());
        Position endPosition = this.board.findPositionById(movement.getEndPositionId());
        List<Position> connectedEnemyPositions = this.board.findConnectedPositionsWithPlayerId(
                endPosition,
                Player.getOpponentOf(movement.getPlayerId())
        );
        List<StandardMovement> movements = new ArrayList<>();
        movements.add(movement);
        for (Position enemyPosition: connectedEnemyPositions) {
            boolean shouldAddMovement = true;
            for (Position connectedPositionToEnemy: this.board.findConnectedPositions(enemyPosition)) {
                if ((connectedPositionToEnemy.getPlayerId() != movement.getPlayerId()
                    && !connectedPositionToEnemy.equals(endPosition))
                    || connectedPositionToEnemy.equals(startPosition)
                ) {
                    shouldAddMovement = false;
                    break;
                }
            }

            if (shouldAddMovement) {
                movements.add(new StandardMovement(enemyPosition, Position.instanceOutOfBoard(), movement.getPlayerId()));
            }
        }
        return movements;
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        int playerPieces = this.board.countPlayerPieces(playerId);
        int opponentPieces = this.board.countPlayerPieces(Player.getOpponentOf(playerId));
        int maxPieceCount = 12;
        return MinimaxAgent.normalizeToEvaluationLimits(playerPieces - opponentPieces, -maxPieceCount, maxPieceCount);

    }
}
