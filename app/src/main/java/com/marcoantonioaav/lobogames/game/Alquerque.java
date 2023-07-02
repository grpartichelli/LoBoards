package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.Agent;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Alquerque extends Game {

    public Alquerque() {
        super();
    }

    @Override
    public String getName() {
        return "Alquerque";
    }

    @Override
    public String getRules() {
        return "Cada jogador possui doze peças posicionadas no tabuleiro. O jogador pode capturar peças adversárias saltando sobre elas, respeitando as linhas do tabuleiro. Capturas em sequência são permitidas, sendo que o jogador é obrigado a capturar o máximo de peças que puder em sua jogada. Caso não existam possibilidades de captura, as peças podem se deslocar para posições adjacentes. Vence o jogador que capturar todas as peças do adversário.";
    }

    @Override
    public Board getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2, Player.PLAYER_2}
        };
        int boardImageId = R.drawable._5x5;
        return new Board(matrix, boardImageId);
    }

    @Override
    public boolean isVictory(int playerId) {
        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++)
                if (this.board.getMatrix()[x][y] == Player.getOpponentOf(playerId))
                    return false;
        return true;
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move) {
        if (move == null)
            return false;
        if (move.movements.size() == 1)
            return move.movements.get(0).isAdjacentInlineMovement(this.board);
        Board newBoard = this.board.copy();
        ArrayList<Movement> removals = new ArrayList<>();
        for (Movement movement : move.movements) {
            if (movement.isRemoval(newBoard))
                break;
            if (!movement.isAdjacentInlineOpponentJump(newBoard))
                return false;
            newBoard.applyMovement(movement);
            removals.add(Movement.getRemovalFor(movement));
        }
        int expectedRemovals = 0;
        for (Movement movement : move.movements)
            for (Movement removal : removals)
                if (movement.isRemoval(newBoard) && movement.startX == removal.startX && movement.startY == removal.startY)
                    expectedRemovals++;
        return removals.size() == expectedRemovals;
    }

    @Override
    public ArrayList<Move> getLegalMoves(int playerId) {
        ArrayList<Move> moves = getEliminationMoves(playerId);
        if (moves.isEmpty())
            return getAdjacentInlineMoves(playerId);
        return moves;
    }

    private ArrayList<Move> getAdjacentInlineMoves(int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (this.board.getMatrix()[x][y] == player)
                    for (int[] eightRegion : new int[][]{{0, 1}, {1, 1}, {1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {1, -1}, {-1, 1}})
                        if (this.board.isOnLimits(x + eightRegion[0], y + eightRegion[1])) {
                            Move newMove = new Move(x, y, x + eightRegion[0], y + eightRegion[1], player);
                            if (isLegalMove(newMove))
                                moves.add(newMove);
                        }
        Collections.shuffle(moves);
        return moves;
    }

    private ArrayList<Move> getEliminationMoves(int player) {
        ArrayList<Move> moves = new ArrayList<>();
        int bestEliminationCount = 0;
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (this.getBoard().getMatrix()[x][y] == player)
                    for (ArrayList<Movement> movementSequence : getEliminationMovementSequences(x, y, this.board, player, new ArrayList<>())) {
                        ArrayList<Movement> movementsWithRemovals = new ArrayList<>(movementSequence);
                        for (Movement movement : movementSequence)
                            movementsWithRemovals.add(Movement.getRemovalFor(movement));
                        Move move = new Move(movementsWithRemovals, player);
                        if (move.getRemovalCount(this.board) == bestEliminationCount)
                            moves.add(move);
                        else if (move.getRemovalCount(this.board) > bestEliminationCount) {
                            bestEliminationCount = move.getRemovalCount(this.board);
                            moves.clear();
                            moves.add(move);
                        }
                    }
        Collections.shuffle(moves);
        return moves;
    }

    private ArrayList<ArrayList<Movement>> getEliminationMovementSequences(int lastX, int lastY, Board board, int player, ArrayList<int[]> eliminationSpots) {
        ArrayList<ArrayList<Movement>> movementSequences = new ArrayList<>();
        for (int[] eightRegion : new int[][]{{0, 2}, {2, 2}, {2, 0}, {0, -2}, {-2, -2}, {-2, 0}, {2, -2}, {-2, 2}}) {
            int newX = lastX + eightRegion[0], newY = lastY + eightRegion[1];
            if (board.isOnLimits(newX, newY)) {
                Movement movement = new Movement(lastX, lastY, newX, newY, player);
                int[] newEliminationSpot = new int[]{Movement.getRemovalFor(movement).startX, Movement.getRemovalFor(movement).startY};
                if (movement.isAdjacentInlineOpponentJump(board) && !containsTuple(newEliminationSpot, eliminationSpots)) {
                    ArrayList<int[]> newEliminationSpots = new ArrayList<>(eliminationSpots);
                    newEliminationSpots.add(newEliminationSpot);
                    Board appliedMoveBoard = board.copy();
                    appliedMoveBoard.applyMovement(movement);
                    ArrayList<ArrayList<Movement>> nextEliminationMovementSequences =
                            getEliminationMovementSequences(newX, newY, appliedMoveBoard, player, newEliminationSpots);
                    if (nextEliminationMovementSequences.isEmpty()) {
                        ArrayList<Movement> newMovementSequence = new ArrayList<>();
                        newMovementSequence.add(movement);
                        movementSequences.add(newMovementSequence);
                    } else {
                        for (ArrayList<Movement> nextMovementSequence : nextEliminationMovementSequences) {
                            ArrayList<Movement> newMovementSequence = new ArrayList<>();
                            newMovementSequence.add(movement);
                            newMovementSequence.addAll(nextMovementSequence);
                            movementSequences.add(newMovementSequence);
                        }
                    }
                }
            }
        }
        return movementSequences;
    }

    private boolean containsTuple(int[] tuple, ArrayList<int[]> list) {
        for (int[] element : list)
            if (tuple[0] == element[0] && tuple[1] == element[1])
                return true;
        return false;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int playerId) {
        ArrayList<Move> legalMoves = getLegalMoves(playerId);
        for (Move move : legalMoves) {
            ArrayList<Movement> movementsWithoutRemovals = removeRemovals(move.movements);
            for (Movement movement : movementsWithoutRemovals)
                if (
                        movementsWithoutRemovals.get(0).startX == startX &&
                                movementsWithoutRemovals.get(0).startY == startY &&
                                movement.endX == endX &&
                                movement.endY == endY
                )
                    return move;
        }
        return null;
    }

    private ArrayList<Movement> removeRemovals(List<Movement> movements) {
        ArrayList<Movement> newMovements = new ArrayList<>();
        for (Movement movement : movements)
            if (!movement.isRemoval(this.board))
                newMovements.add(movement);
        return newMovements;
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        int playerPieces = this.board.countPlayerPieces(playerId);
        int opponentPieces = this.board.countPlayerPieces(Agent.getOpponentOf(playerId));
        int maxPieceCount = 12;
        return MinimaxAgent.normalizeToEvaluationLimits(playerPieces - opponentPieces, -maxPieceCount, maxPieceCount);
    }
}
