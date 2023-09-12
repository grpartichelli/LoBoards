package com.marcoantonioaav.lobogames.player;

import android.os.SystemClock;
import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.move.Move;

import java.util.ArrayList;
import java.util.List;

public class ReplayPlayer extends Player{
    private final List<Move> moves = new ArrayList<>();
    private int currentMoveIndex = 0;

    private long lastTime = 0;
    private static final double MOVE_DELAY_IN_SECONDS = 1.5;

    public ReplayPlayer(int id) {
        super(id);
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    @Override
    public Move getMove(Game game) {
        if (moves.size() <= currentMoveIndex) {
            throw new IllegalStateException("Replay player ran out of moves");
        }
        Move move = moves.get(currentMoveIndex);
        currentMoveIndex++;
        lastTime = 0;
        return move;
    }

    @Override
    public boolean isReady() {
        if (lastTime == 0) {
            lastTime = SystemClock.uptimeMillis();
            return false;
        }
        if ((lastTime + (MOVE_DELAY_IN_SECONDS * 1000)) <= SystemClock.uptimeMillis()) {
            lastTime = SystemClock.uptimeMillis();
            return true;
        }
        return false;
    }

    public void clearMoves() {
        moves.clear();
    }

    public void reset() {
        currentMoveIndex = 0;
        lastTime = 0;
    }
}
