package com.marcoantonioaav.lobogames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.marcoantonioaav.lobogames.game.Alquerque;
import com.marcoantonioaav.lobogames.game.FiveFieldKono;
import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.game.Tapatan;
import com.marcoantonioaav.lobogames.game.TicTacToe;
import com.marcoantonioaav.lobogames.player.agent.Agent;

import java.util.HashMap;
import java.util.Objects;

public class PreGameActivity extends AppCompatActivity {
    private Spinner gameSpinner;
    private RadioGroup difficultyChooser;
    private Button play;

    private boolean isMultiplayer;

    public static final String GAME_NAME = "GAME_NAME";
    public static final String IS_MULTIPLAYER = "IS_MULTIPLAYER";
    public static final String DIFFICULTY = "DIFFICULTY";
    public final static HashMap<String, Game> GAMES = new HashMap<String, Game>() {{
        put(new Tapatan().getName(), new Tapatan());
        put(new TicTacToe().getName(), new TicTacToe());
        put(new Alquerque().getName(), new Alquerque());
        put(new FiveFieldKono().getName(), new FiveFieldKono());
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        isMultiplayer = (boolean) this.getIntent().getExtras().get(IS_MULTIPLAYER);

        gameSpinner = findViewById(R.id.gameSpinner);
        ArrayAdapter ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GAMES.keySet().toArray());
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(ad);

        difficultyChooser = findViewById(R.id.difficultyChooser);
        if(isMultiplayer)
            difficultyChooser.setVisibility(View.INVISIBLE);

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_NAME, (String) gameSpinner.getSelectedItem());
        intent.putExtra(IS_MULTIPLAYER, isMultiplayer);
        intent.putExtra(DIFFICULTY, getCheckedDifficulty());

        startActivity(intent);
        finish();
    }

    private String getCheckedDifficulty() {
        int buttonId = difficultyChooser.getCheckedRadioButtonId();
        if(buttonId == R.id.radioMedium)
            return Agent.MEDIUM_DIFFICULTY;
        if(buttonId == R.id.radioHard)
            return Agent.HARD_DIFFICULTY;
        return Agent.EASY_DIFFICULTY;
    }
}