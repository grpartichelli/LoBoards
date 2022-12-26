package com.marcoantonioaav.lobogames.move;

import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;

public class Move {
    public Movement[] movements;
    public int playerId;

    public Move(int x, int y, int playerId) {
        movements = new Movement[]{new Movement(x, y, playerId)};
        this.playerId = playerId;
    }

    public Move(int startX, int startY, int endX, int endY, int playerId) {
        movements = new Movement[]{new Movement(startX, startY, endX, endY, playerId)};
        this.playerId = playerId;
    }

    public Move(ArrayList<Movement> movements, int playerId) {
        this.movements = new Movement[movements.size()];
        for(int i=0; i < movements.size(); i++)
            this.movements[i] = movements.get(i);
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        String result = "";
        if(movements.length > 0) {
            result = Player.getName(playerId) + ":";
            for(Movement movement : movements)
                result += " " + movement.toString();
        }
        return result;
    }

    public int getRemovalCount(int[][] board) {
        int count = 0;
        for(Movement movement : movements)
            if(movement.isRemoval(board))
                count++;
        return count;
    }
}
