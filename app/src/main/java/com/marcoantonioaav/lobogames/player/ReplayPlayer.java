package com.marcoantonioaav.lobogames.player;

import android.os.SystemClock;
import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.replay.Replay;

import java.util.List;

public class ReplayPlayer extends Player {
    private final List<Move> moves;
    private int currentMoveIndex = 0;

    private long lastTime = 0;
    private static final double MOVE_DELAY_IN_SECONDS = 1;

    public ReplayPlayer(int id, Replay replay) {
        super(id);
        this.moves = replay.findMovesByPlayerId(id);
    }

    @Override
    public Move getMove(Game game) {
        if (isReplayFinished()) {
            throw new IllegalStateException("Replay player ran out of moves");
        }
        Move move = moves.get(currentMoveIndex);
        currentMoveIndex++;
        lastTime = 0;
        return move;
    }

    @Override
    public boolean isReady() {
        if (isReplayFinished()) {
            return false;
        }

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

    public boolean isReplayFinished() {
        return moves.size() <= currentMoveIndex;
    }
}
