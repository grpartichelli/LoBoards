package com.marcoantonioaav.lobogames.replay;

import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.move.Move;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Replay {
    private final List<Move> moves = new ArrayList<>();
    private LocalDateTime date;
    private String name;
    private final Game game;

    public Replay(Game game) {
        this.game = game;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void clearMoves() {
        moves.clear();
    }


    public int countMoves() {
        return moves.size();
    }

    public Move findMove(int moveIndex) {
        return moves.get(moveIndex);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Game getGame() {
        return game;
    }
}
