package com.marcoantonioaav.lobogames.replay;

import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.game.GenericGame;
import com.marcoantonioaav.lobogames.move.Move;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Replay {
    private final List<Move> moves = new ArrayList<>();
    private final Date date;
    private final String gameName;
    private final String boardName;
    private int maxPositions = 0;

    public Replay(Game game, Date date) {
        this.gameName = game.getName();
        this.boardName = game.getBoard().getName();
        this.date = date;
    }

    public Replay(String gameName, String boardName,  Date date) {
        this.gameName = gameName;
        this.boardName = boardName;
        this.date = date;
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

    public String getBoardName() {
        return boardName;
    }


    public String getName() {
        String name = GenericGame.NAME.equals(gameName) ? boardName : gameName;
        name += " - " + getDateString();
        return name;
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

    public void setMaxPositions(int maxPositions) {
        this.maxPositions = maxPositions;
    }

    public int getMaxPositions() {
        return maxPositions;
    }
}
