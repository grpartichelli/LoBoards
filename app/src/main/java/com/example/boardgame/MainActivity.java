package com.example.boardgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button buttonTicTacToe, buttonTapatan, buttonAlquerque;

    public final static HashMap<String, Game> games = new HashMap<String, Game>() {{
        put(new Tapatan().getName(), new Tapatan());
        put(new TicTacToe().getName(), new TicTacToe());
        put(new Alquerque().getName(), new Alquerque());
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        buttonTapatan = findViewById(R.id.buttonTapatan);
        buttonTapatan.setOnClickListener(view -> openGameActivity(new Tapatan().getName()));
        buttonTicTacToe = findViewById(R.id.buttonTicTacToe);
        buttonTicTacToe.setOnClickListener(view -> openGameActivity(new TicTacToe().getName()));
        buttonAlquerque = findViewById(R.id.buttonAlquerque);
        buttonAlquerque.setOnClickListener(view -> openGameActivity(new Alquerque().getName()));
    }

    private void openGameActivity(String gameName) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("gameName", gameName);
        startActivity(intent);
    }
}