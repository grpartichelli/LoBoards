package com.marcoantonioaav.lobogames.game;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Player;

import java.util.ArrayList;
import java.util.Collections;

public class FiveFieldKono extends Game {
    @Override
    public String getName() {
        return "Five Field Kono";
    }

    @Override
    public int[][] getInitialBoard() {
        return new int[][]{

                /*|    X - X - X - X - X    |*/
                /*|    |   |   |   |   |    |*/
                /*|    X -   -   -   - X    |*/
                /*|    |   |   |   |   |    |*/
                /*|      -   -   -   -      |*/
                /*|    |   |   |   |   |    |*/
                /*|    O -   -   -   - O    |*/
                /*|    |   |   |   |   |    |*/
                /*|    O - O - O - O - O    |*/

                {Player.PLAYER_1,  Player.PLAYER_1,  Player.EMPTY,   Player.PLAYER_2,  Player.PLAYER_2},
                {Player.PLAYER_1,  Player.EMPTY,     Player.EMPTY,   Player.EMPTY,     Player.PLAYER_2},
                {Player.PLAYER_1,  Player.EMPTY,     Player.EMPTY,   Player.EMPTY,     Player.PLAYER_2},
                {Player.PLAYER_1,  Player.EMPTY,     Player.EMPTY,   Player.EMPTY,     Player.PLAYER_2},
                {Player.PLAYER_1,  Player.PLAYER_1,  Player.EMPTY,   Player.PLAYER_2,  Player.PLAYER_2}
        };
    }

    private boolean isPlayerOnInitialPosition(int playerId, int[][] board) {
        int[][] initialBoard = getInitialBoard();
        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
                if((board[x][y] == playerId && initialBoard[x][y] != playerId) || (initialBoard[x][y] == playerId && board[x][y] != playerId))
                    return false;
        return true;
    }

    @Override
    public boolean isVictory(int[][] board, int playerId) {
        ArrayList<Move> moves = getLegalMoves(board, Player.getOpponentOf(playerId));
        if(moves.isEmpty())
            return true;

        if(isPlayerOnInitialPosition(Player.PLAYER_1, board) || isPlayerOnInitialPosition(Player.PLAYER_2, board))
            return false;

        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
                if(getInitialBoard()[x][y] == Player.getOpponentOf(playerId) && board[x][y] == Player.EMPTY)
                    return false;
        return true;
    }

    @Override
    public boolean isDraw(int[][] board) {
        return false;
    }

    @Override
    public boolean isLegalMove(Move move, int[][] board) {
        int[][] newBoard = copyBoard(board);
        newBoard = applyMovement(move.movements[0], newBoard);
        if(isPlayerOnInitialPosition(move.playerId, newBoard))
            return false;

        return move.movements.length == 1 && move.movements[0].isAdjacentInlineMovement(board);
    }

    @Override
    public ArrayList<Move> getLegalMoves(int[][] board, int playerId) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x=0; x < getBoardWidth(board); x++)
            for(int y=0; y < getBoardHeight(board); y++)
                if(board[x][y] == playerId)
                    for(int[] eightRegion : new int[][]{{0,1}, {1,1}, {1,0}, {0,-1}, {-1,-1}, {-1, 0}, {1,-1}, {-1,1}})    // Can be reduced to only the diagonals to improve efficiency, but the movement style may be changed to lines eventually
                        if(isOnBoardLimits(x+eightRegion[0], y+eightRegion[1], board)) {
                            Move newMove = new Move(x, y, x+eightRegion[0], y+eightRegion[1], playerId);
                            if(isLegalMove(newMove, board))
                                moves.add(newMove);
                        }
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public Move getPlayerMove(int startX, int startY, int endX, int endY, int[][] board, int playerId) {
        return new Move(startX, startY, endX, endY, playerId);
    }

    @Override
    public String getRules() {
        return "Mova suas peças diagonalmente tal que as posições iniciais do inimigo estejam completamente preenchidas por peças suas ou dele.";
    }
}
