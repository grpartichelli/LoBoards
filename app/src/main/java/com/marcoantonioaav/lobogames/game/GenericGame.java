package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;

import static com.marcoantonioaav.lobogames.player.Player.*;

public class GenericGame extends Game {
    @Override
    public String getName() {
        return "Generic";
    }

    @Override
    public int[][] getInitialBoard() {
        return new int[][]{
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
                {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY},
        };
    }

    @Override
    public int getBoardImage() {
        return R.drawable._5x5;
    }

    @Override
    public boolean isVictory(int[][] board, int playerId) {
        return false;
    }

    @Override
    public boolean isDraw(int[][] board) {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        if (move.movements.length != 1) {
            return false;
        }

        Movement movement = move.movements[0];

        if (movement.isInsertion(board) || movement.isRemoval(board)) {
            return true;
        }

        if (movement.startX == -1 ||  movement.startY == -1  || movement.endX == -1 || movement.endY == -1) {
            return false;
        }

        return board[movement.startX][movement.startY] == movement.piece && board[movement.endX][movement.endY] == EMPTY;
    }

    @Override
    public ArrayList<Move> getLegalMoves(int[][] board, int playerId) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                moves.add(new Move(i, j, PLAYER_1));
                moves.add(new Move(i, j, PLAYER_2));

                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        moves.add(new Move(i, j, k, l, PLAYER_1));
                        moves.add(new Move(i, j, k, l, PLAYER_2));
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int playerId) {
        return new Move(startX, startY, endX, endY, playerId);
    }

    @Override
    public String getRules() {
        return "Generic game rules";
    }

    @Override
    public float getHeuristicEvaluationOf(int[][] board, int playerId, int turn) {
        return 0;
    }
}
