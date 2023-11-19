package com.marcoantonioaav.lobogames.player;

import android.os.SystemClock;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.replay.Replay;

import java.util.ArrayList;
import java.util.List;

public class ReplayPlayer extends Player {
    private Replay replay;
    private int currentMoveIndex = 0;

    private long lastTime = 0;
    private static final double MOVE_DELAY_IN_SECONDS = 1.5;

    public ReplayPlayer(int id) {
        super(id);
    }

    public void addMove(Move move) {
        replay.addMove(move);
    }

    @Override
    public Move getMove(Game game) {
        if (replay.countMoves() <= currentMoveIndex) {
            throw new IllegalStateException("Replay player ran out of moves");
        }
        Move move = replay.findMove(currentMoveIndex);
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
        replay.clearMoves();
    }

    public Replay getReplay() {
        return replay;
    }

    public void setReplay(Replay replay) {
        this.replay = replay;
    }

    public void reset() {
        currentMoveIndex = 0;
        lastTime = 0;
    }
}
