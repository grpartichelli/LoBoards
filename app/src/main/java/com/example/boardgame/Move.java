package com.example.boardgame;

import java.util.ArrayList;

public class Move {
    public Movement[] movements;
    public int player;

    public Move(int x, int y, int player) {
        movements = new Movement[]{new Movement(x, y, player)};
        this.player = player;
    }

    public Move(int startX, int startY, int endX, int endY, int player) {
        movements = new Movement[]{new Movement(startX, startY, endX, endY, player)};
        this.player = player;
    }

    public Move(ArrayList<Movement> movements, int player) {
        this.movements = new Movement[movements.size()];
        for(int i=0; i < movements.size(); i++)
            this.movements[i] = movements.get(i);
        this.player = player;
    }

    @Override
    public String toString() {
        String result = "";
        if(movements.length > 0) {
            result = Agent.getPlayerName(player) + ": ";
            for(Movement movement : movements)
                result += movement.toString();
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
