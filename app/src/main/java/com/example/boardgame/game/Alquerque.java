package com.example.boardgame.game;

import com.example.boardgame.move.Move;
import com.example.boardgame.move.Movement;
import com.example.boardgame.player.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Alquerque extends Game {
    @Override
    public String getName() {
        return "Alquerque";
    }

    @Override
    public int[][] getInitialBoard() {
        return new int[][]{
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2, Player.PLAYER_2},
                {Player.PLAYER_1, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_2, Player.PLAYER_2}
        };
    }

    @Override
    public boolean isVictory(int[][] board, int player) {
        for(int x=0; x < 5; x++)
            for(int y=0; y < 5; y++)
                if(board[x][y] == Player.getOpponentOf(player))
                    return false;
        return true;
    }

    @Override
    public boolean isDraw(int[][] board) {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        if(move == null)
            return false;
        if(move.movements.length == 1)
            return move.movements[0].isAdjacentInlineMovement(board);
        int[][] newBoard = copyBoard(board);
        ArrayList<Movement> removals = new ArrayList<>();
        for(Movement movement : move.movements) {
            if(movement.isRemoval(newBoard))
                break;
            if(!movement.isAdjacentInlineOpponentJump(newBoard))
                return false;
            newBoard = applyMovement(movement, newBoard);
            removals.add(Movement.getRemovalFor(movement));
        }
        int expectedRemovals = 0;
        for(Movement movement : move.movements)
            for(Movement removal : removals)
                if(movement.isRemoval(newBoard) && movement.startX == removal.startX && movement.startY == removal.startY)
                    expectedRemovals++;
        return removals.size() == expectedRemovals;
    }

    @Override
    public ArrayList<Move> getLegalMoves(int[][] board, int player) {
        ArrayList<Move> moves = getEliminationMoves(board, player);
        if(moves.isEmpty())
            return getAdjacentInlineMoves(board, player);
        return moves;
    }

    private ArrayList<Move> getAdjacentInlineMoves(int[][] board, int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
                if(board[x][y] == player)
                    for(int[] eightRegion : new int[][]{{0,1}, {1,1}, {1,0}, {0,-1}, {-1,-1}, {-1, 0}, {1,-1}, {-1,1}})
                        if(isOnBoardLimits(x+eightRegion[0], y+eightRegion[1], board)) {
                            Move newMove = new Move(x, y, x+eightRegion[0], y+eightRegion[1], player);
                            if(isLegalMove(newMove, board))
                                moves.add(newMove);
                        }
        Collections.shuffle(moves);
        return moves;
    }

    private ArrayList<Move> getEliminationMoves(int[][] board, int player) {
        ArrayList<Move> moves = new ArrayList<>();
        int bestEliminationCount = 0;
        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
                if(board[x][y] == player)
                    for(ArrayList<Movement> movementSequence : getEliminationMovementSequences(x, y, board, player, new ArrayList<>())) {
                        ArrayList<Movement> movementsWithRemovals = new ArrayList<>(movementSequence);
                        for(Movement movement : movementSequence)
                            movementsWithRemovals.add(Movement.getRemovalFor(movement));
                        Move move = new Move(movementsWithRemovals, player);
                        if(move.getRemovalCount(board) == bestEliminationCount)
                            moves.add(move);
                        else if(move.getRemovalCount(board) > bestEliminationCount) {
                            bestEliminationCount = move.getRemovalCount(board);
                            moves.clear();
                            moves.add(move);
                        }
                    }
        Collections.shuffle(moves);
        return moves;
    }

    private ArrayList<ArrayList<Movement>> getEliminationMovementSequences(int lastX, int lastY, int[][] board, int player, ArrayList<int[]> eliminationSpots) {
        ArrayList<ArrayList<Movement>> movementSequences = new ArrayList<>();
        for (int[] eightRegion : new int[][]{{0, 2}, {2, 2}, {2, 0}, {0, -2}, {-2, -2}, {-2, 0}, {2, -2}, {-2, 2}}) {
            int newX = lastX + eightRegion[0], newY = lastY + eightRegion[1];
            if (isOnBoardLimits(newX, newY, board)) {
                Movement movement = new Movement(lastX, lastY, newX, newY, player);
                int[] newEliminationSpot = new int[]{Movement.getRemovalFor(movement).startX, Movement.getRemovalFor(movement).startY};
                if(movement.isAdjacentInlineOpponentJump(board) && !containsTuple(newEliminationSpot, eliminationSpots)) {
                    ArrayList<int[]> newEliminationSpots = new ArrayList<>(eliminationSpots);
                    newEliminationSpots.add(newEliminationSpot);
                    ArrayList<ArrayList<Movement>> nextEliminationMovementSequences =
                            getEliminationMovementSequences(newX, newY, applyMovement(movement, board), player, newEliminationSpots);
                    if(nextEliminationMovementSequences.isEmpty()) {
                        ArrayList<Movement> newMovementSequence = new ArrayList<>();
                        newMovementSequence.add(movement);
                        movementSequences.add(newMovementSequence);
                    }
                    else {
                        for(ArrayList<Movement> nextMovementSequence : nextEliminationMovementSequences) {
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
        for(int[] element : list)
            if(tuple[0] == element[0] && tuple[1] == element[1])
                return true;
        return false;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int player) {
        ArrayList<Move> legalMoves = getLegalMoves(board, player);
        for(Move move : legalMoves) {
            ArrayList<Movement> movementsWithoutRemovals = removeRemovals(move.movements, board);
            if(
                    movementsWithoutRemovals.get(0).startX == startX &&
                    movementsWithoutRemovals.get(0).startY == startY &&
                    movementsWithoutRemovals.get(movementsWithoutRemovals.size()-1).endX == endX &&
                    movementsWithoutRemovals.get(movementsWithoutRemovals.size()-1).endY == endY
            )
                return move;
        }
        return null;
    }

    private ArrayList<Movement> removeRemovals(Movement[] movements, int[][] board) {
        ArrayList<Movement> newMovements = new ArrayList<>();
        for(Movement movement : movements)
            if(!movement.isRemoval(board))
                newMovements.add(movement);
        return newMovements;
    }

}
