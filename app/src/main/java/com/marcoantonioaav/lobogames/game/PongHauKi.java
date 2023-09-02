package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.GenericBoard;
import com.marcoantonioaav.lobogames.board.GenericPongHauKiBoardFactory;
import com.marcoantonioaav.lobogames.move.GenericMove;
import com.marcoantonioaav.lobogames.move.GenericMovement;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PongHauKi extends GenericGame {
    @Override
    public String getName() {
        return "Pong Hau K'i";
    }

    @Override
    public Board getInitialBoard() {
        return GenericPongHauKiBoardFactory.from(
                Arrays.asList(Player.PLAYER_1, Player.PLAYER_2, Player.EMPTY, Player.PLAYER_2, Player.PLAYER_1)
        );
    }

    @Override
    public String getRules() {
        // TODO: rules
        return null;
    }

    @Override
    public Game newInstance() {
        return new PongHauKi();
    }

    @Override
    public boolean isVictory(int playerId) {
        return false;
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move) {
        return false;
    }

    @Override
    public List<Move> getLegalMoves(int playerId) {
        return new ArrayList<>();
    }

    @Override
    public Move getPlayerMove(Position startPosition, Position endPosition, int playerId) {
        return null;
    }

    @Override
    public float getHeuristicEvaluationOf(int playerId, int turn) {
        return 0;
    }
}
