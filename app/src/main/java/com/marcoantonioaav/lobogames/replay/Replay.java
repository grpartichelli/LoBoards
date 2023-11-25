package com.marcoantonioaav.lobogames.replay;

import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.game.GenericGame;
import com.marcoantonioaav.lobogames.move.Move;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Replay {
    private final List<Move> moves = new ArrayList<>();
    private final Date date;
    private final Game game;

    public Replay(Game game) {
        this.game = game;
        this.date = Calendar.getInstance().getTime();
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public List<Move> findMovesByPlayerId(int playerId) {
        List<Move> playerMoves = new ArrayList<>();
        for (Move move : this.moves) {
            if (move.getPlayerId() == playerId) {
                playerMoves.add(move);
            }
        }
        return playerMoves;
    }

    public Game getGame() {
        return game;
    }

    public String getName() {
        String name = game.equals(new GenericGame())
                ? game.getName()
                : game.getBoard().getName();
        name += " " + date.toString();
        return name;
    }

    public String getFileName() {
        return getName().toLowerCase().replaceAll(" ", "-") + "-lobogames-replay.txt";
    }

    public List<Move> getMoves() {
        return moves;
    }
}
