package com.marcoantonioaav.lobogames.player;

import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.game.Game;

import java.util.Random;

public abstract class Player {
    public static final int EMPTY = 0;
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;

    private final int id;

    Player(int id) {
        this.id = id;
    }

    public abstract Move getMove(Game<?> game);

    public abstract boolean isReady();

    public static Player selectPlayerById(Player player1, Player player2, int id) {
        if(player1.getId() == id)
            return player1;
        return player2;
    }

    public static int getRandomId() {
        if(new Random().nextBoolean())
            return PLAYER_1;
        return PLAYER_2;
    }

    public int getId() {
        return id;
    }

    public int getOpponent() {
        return getOpponentOf(this.id);
    }

    public static int getOpponentOf(int playerId) {
        if(playerId == PLAYER_1)
            return PLAYER_2;
        return PLAYER_1;
    }

    public static String getName(int playerId) {
        if(playerId == Player.PLAYER_1)
            return "Jogador 1";
        if(playerId == Player.PLAYER_2)
            return "Jogador 2";
        return "Vazio";
    }
}
