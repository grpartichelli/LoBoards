package com.marcoantonioaav.lobogames.move;

import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Move {
    public List<Movement> movements = new ArrayList<>();
    public int playerId;

    public Move(int x, int y, int playerId) {
        movements.add(new Movement(x, y, playerId));
        this.playerId = playerId;
    }

    public Move(int startX, int startY, int endX, int endY, int playerId) {
        movements.add(new Movement(startX, startY, endX, endY, playerId));
        this.playerId = playerId;
    }

    public Move(ArrayList<Movement> movements, int playerId) {
        this.movements = movements;
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        String result = "";
        if(!movements.isEmpty()) {
            result = Player.getName(playerId) + ":";
            for(Movement movement : movements)
                result += " " + movement.toString();
        }
        return result;
    }

    public int getRemovalCount(MatrixBoard board) {
        int count = 0;
        for(Movement movement : movements)
            if(movement.isRemoval(board))
                count++;
        return count;
    }
}
