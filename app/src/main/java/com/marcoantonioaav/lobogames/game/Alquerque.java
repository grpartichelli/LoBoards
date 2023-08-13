package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Matrix5x5BoardFactory;
import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.move.MatrixMove;
import com.marcoantonioaav.lobogames.move.MatrixMovement;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO: Fix animation
public class Alquerque extends MatrixGame {

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
    public MatrixBoard getInitialBoard() {
        int[][] matrix = new int[][]{
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2, Player.PLAYER_2}
        };
        return Matrix5x5BoardFactory.from(matrix);
    }

    @Override
    public Game newInstance() {
        return new Alquerque();
    }

    @Override
    public boolean isVictory(int playerId) {
        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++)
                if (this.board.valueAt(x, y) == Player.getOpponentOf(playerId))
                    return false;
        return true;
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMatrixMove(MatrixMove move) {
        if (move == null)
            return false;
        if (move.getMatrixMovements().size() == 1)
            return move.getMatrixMovements().get(0).isAdjacentInlineMovement(this.board);
        MatrixBoard newBoard = this.board.copy();
        List<MatrixMovement> removals = new ArrayList<>();
        for (MatrixMovement movement : move.getMatrixMovements()) {
            if (movement.isRemoval(newBoard))
                break;
            if (!movement.isAdjacentInlineOpponentJump(newBoard))
                return false;
            newBoard.applyMovement(movement);
            removals.add(MatrixMovement.getRemovalFor(movement));
        }
        int expectedRemovals = 0;
        for (MatrixMovement movement : move.getMatrixMovements())
            for (MatrixMovement removal : removals)
                if (movement.isRemoval(newBoard) && movement.getStartX() == removal.getStartX() && movement.getStartY() == removal.getStartY())
                    expectedRemovals++;
        return removals.size() == expectedRemovals;
    }

    @Override
    public List<MatrixMove> getLegalMatrixMoves(int playerId) {
        List<MatrixMove> moves = getEliminationMoves(playerId);
        if (moves.isEmpty())
            return getAdjacentInlineMoves(playerId);
        return moves;
    }

    private List<MatrixMove> getAdjacentInlineMoves(int player) {
        List<MatrixMove> moves = new ArrayList<>();
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (this.board.valueAt(x, y) == player)
                    for (int[] eightRegion : new int[][]{{0, 1}, {1, 1}, {1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {1, -1}, {-1, 1}})
                        if (this.board.isOnLimits(x + eightRegion[0], y + eightRegion[1])) {
                            MatrixMove newMove = new MatrixMove(x, y, x + eightRegion[0], y + eightRegion[1], player, this.board.getPositionMapper());
                            if (isLegalMove(newMove))
                                moves.add(newMove);
                        }
        Collections.shuffle(moves);
        return moves;
    }

    private ArrayList<MatrixMove> getEliminationMoves(int player) {
        ArrayList<MatrixMove> moves = new ArrayList<>();
        int bestEliminationCount = 0;
        for (int x = 0; x < this.board.getWidth(); x++)
            for (int y = 0; y < this.board.getHeight(); y++)
                if (this.board.valueAt(x, y) == player)
                    for (ArrayList<MatrixMovement> movementSequence : getEliminationMovementSequences(x, y, this.board, player, new ArrayList<>())) {
                        ArrayList<MatrixMovement> movementsWithRemovals = new ArrayList<>(movementSequence);
                        for (MatrixMovement movement : movementSequence)
                            movementsWithRemovals.add(MatrixMovement.getRemovalFor(movement));
                        MatrixMove move = new MatrixMove(movementsWithRemovals, player, this.board.getPositionMapper());
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

    private ArrayList<ArrayList<MatrixMovement>> getEliminationMovementSequences(int lastX, int lastY, MatrixBoard board, int player, ArrayList<int[]> eliminationSpots) {
        ArrayList<ArrayList<MatrixMovement>> movementSequences = new ArrayList<>();
        for (int[] eightRegion : new int[][]{{0, 2}, {2, 2}, {2, 0}, {0, -2}, {-2, -2}, {-2, 0}, {2, -2}, {-2, 2}}) {
            int newX = lastX + eightRegion[0], newY = lastY + eightRegion[1];
            if (board.isOnLimits(newX, newY)) {
                MatrixMovement movement = new MatrixMovement(lastX, lastY, newX, newY, player, this.board.getPositionMapper());
                int[] newEliminationSpot = new int[]{MatrixMovement.getRemovalFor(movement).getStartX(), MatrixMovement.getRemovalFor(movement).getStartY()};
                if (movement.isAdjacentInlineOpponentJump(board) && !containsTuple(newEliminationSpot, eliminationSpots)) {
                    ArrayList<int[]> newEliminationSpots = new ArrayList<>(eliminationSpots);
                    newEliminationSpots.add(newEliminationSpot);
                    MatrixBoard appliedMoveBoard = board.copy();
                    appliedMoveBoard.applyMovement(movement);
                    ArrayList<ArrayList<MatrixMovement>> nextEliminationMovementSequences =
                            getEliminationMovementSequences(newX, newY, appliedMoveBoard, player, newEliminationSpots);
                    if (nextEliminationMovementSequences.isEmpty()) {
                        ArrayList<MatrixMovement> newMovementSequence = new ArrayList<>();
                        newMovementSequence.add(movement);
                        movementSequences.add(newMovementSequence);
                    } else {
                        for (ArrayList<MatrixMovement> nextMovementSequence : nextEliminationMovementSequences) {
                            ArrayList<MatrixMovement> newMovementSequence = new ArrayList<>();
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
    public MatrixMove getPlayerMatrixMove(int startX, int startY, int endX, int endY, int playerId) {
        List<MatrixMove> legalMoves = getLegalMatrixMoves(playerId);
        for (MatrixMove move : legalMoves) {
            List<MatrixMovement> movementsWithoutRemovals = removeRemovals(move.getMatrixMovements());
            for (MatrixMovement movement : movementsWithoutRemovals)
                if (
                        movementsWithoutRemovals.get(0).getStartX() == startX &&
                                movementsWithoutRemovals.get(0).getStartY() == startY &&
                                movement.getEndX() == endX &&
                                movement.getEndY() == endY
                )
                    return move;
        }
        return null;
    }

    private List<MatrixMovement> removeRemovals(List<MatrixMovement> movements) {
        List<MatrixMovement> newMovements = new ArrayList<>();
        for (MatrixMovement movement : movements)
            if (!movement.isRemoval(this.board))
                newMovements.add(movement);
        return newMovements;
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        int playerPieces = this.board.countPlayerPieces(playerId);
        int opponentPieces = this.board.countPlayerPieces(Player.getOpponentOf(playerId));
        int maxPieceCount = 12;
        return MinimaxAgent.normalizeToEvaluationLimits(playerPieces - opponentPieces, -maxPieceCount, maxPieceCount);
    }
}
