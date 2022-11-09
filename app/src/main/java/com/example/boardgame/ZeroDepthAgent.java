package com.example.boardgame;

import java.util.ArrayList;

public class ZeroDepthAgent extends Agent {
    public ZeroDepthAgent(int player) {
        super(player);
    }

    @Override
    public Move selectMove(Game game, int[][] board) {
        if(game.getLegalMoves(board, getPlayer()).isEmpty())
            return null;
        ArrayList<Move> neutralMoves = new ArrayList<>();
        for(Move move : game.getLegalMoves(board, getPlayer())) {
            int[][] newBoard = game.applyMove(move, board);
            if(game.isVictory(newBoard, getPlayer()))
                return move;
            if(!game.isVictory(newBoard, getOpponent()))
                neutralMoves.add(move);
        }
        if(!neutralMoves.isEmpty())
            return neutralMoves.get(0);
        return game.getLegalMoves(board, getPlayer()).get(0);
    }
}
