package com.marcoantonioaav.lobogames.replay;

import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.move.Move;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Replay {
    private final List<Move> moves = new ArrayList<>();
    private final Date date;
    private final String gameName;
    private int maxPositions = 0;
    private boolean isFreeMovementMode = false;

    public Replay(Game game, Date date, boolean isFreeMovementMode) {
        this.gameName = game.getName();
        this.date = date;
        this.isFreeMovementMode = isFreeMovementMode;
    }

    public Replay(String gameName, Date date, boolean isFreeMovementMode){
        this.gameName = gameName;
        this.date = date;
        this.isFreeMovementMode = isFreeMovementMode;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void addAllMoves(List<Move> moves) {
        this.moves.addAll(moves);
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

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

    public Date getDate() {
        return date;
    }

    public String getGameName() {
        return gameName;
    }



    public String getName() {
        return gameName + " - " + getDateString();
    }


    public String getFileName() {
        return getName().toLowerCase()
                .replace(" - ", "-")
                .replaceAll(" ", "-")
                .replaceAll("/", "-")
                .replaceAll(":", "-")
                + "-lobogames-replay.txt";
    }

    public List<Move> getMoves() {
        return moves;
    }

    public boolean isFreeMovementMode() {
        return isFreeMovementMode;
    }

    public void setMaxPositions(int maxPositions) {
        this.maxPositions = maxPositions;
    }

    public int getMaxPositions() {
        return maxPositions;
    }
}
