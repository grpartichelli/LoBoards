package com.example.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView input;
    private Button enter;
    private TextView display;

    private Game game;
    private int[][] board;
    private int player;
    private Agent agent;
    private int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        game = new TicTacToe();
        board = game.getInitialBoard();
        player = Agent.PLAYER_1;
        agent = new RandomAgent(Agent.PLAYER_2);
        turn = player;

        input = findViewById(R.id.input);
        enter = findViewById(R.id.enter);
        display = findViewById(R.id.display);
        enter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(player == turn && !game.isTerminalState(board)) {
                            board = game.enterCommandLine(input.getText().toString(), board, player);
                            turn = Agent.getOpponentOf(turn);
                            display.setText(game.boardToString(board));
                            if(!game.isTerminalState(board)) {
                                board = game.applyMove(agent.selectMove(game, board), board);
                                turn = Agent.getOpponentOf(turn);
                                display.setText(game.boardToString(board));
                            }
                        }
                    }
                }
        );
    }


}