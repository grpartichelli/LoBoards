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

import com.marcoantonioaav.lobogames.game.*;
import com.marcoantonioaav.lobogames.player.agent.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PreGameActivity extends AppCompatActivity {
    private Spinner gameSpinner;
    private RadioGroup difficultyChooser, numberOfPlayersChooser;
    private Button play;

    public static final String GAME_NAME = "GAME_NAME";
    public static final String IS_MULTIPLAYER = "IS_MULTIPLAYER";
    public static final String DIFFICULTY = "DIFFICULTY";
    public static final Map<String, Game> GAMES = new HashMap<String, Game>() {{
        put(new GenericGame().getName(), new GenericGame());
        put(new Tapatan().getName(), new Tapatan());
        put(new TicTacToe().getName(), new TicTacToe());
        put(new Alquerque().getName(), new Alquerque());
        put(new FiveFieldKono().getName(), new FiveFieldKono());
        put(new TsoroYematatuV2().getName(), new TsoroYematatuV2());
        put(new PongHauKi().getName(), new PongHauKi());
        put(new Shisima().getName(), new Shisima());
        put(new WatermelonChess().getName(), new WatermelonChess());
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        gameSpinner = findViewById(R.id.gameSpinner);
        ArrayAdapter ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GAMES.keySet().toArray());
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(ad);

        difficultyChooser = findViewById(R.id.difficultyChooser);
        numberOfPlayersChooser = findViewById(R.id.numberOfPlayersChooser);
        numberOfPlayersChooser.setOnCheckedChangeListener((radioGroup, i) -> updateDifficultyChooserVisibility());
        updateDifficultyChooserVisibility();

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_NAME, (String) gameSpinner.getSelectedItem());
        intent.putExtra(IS_MULTIPLAYER, getIsMultiplayer());
        intent.putExtra(DIFFICULTY, getDifficulty());

        startActivity(intent);
        finish();
    }

    private String getDifficulty() {
        int buttonId = difficultyChooser.getCheckedRadioButtonId();
        if(buttonId == R.id.radioMedium)
            return Agent.MEDIUM_DIFFICULTY;
        if(buttonId == R.id.radioHard)
            return Agent.HARD_DIFFICULTY;
        return Agent.EASY_DIFFICULTY;
    }

    private boolean getIsMultiplayer() {
        return numberOfPlayersChooser.getCheckedRadioButtonId() == R.id.radioMultiplayer;
    }

    private void updateDifficultyChooserVisibility() {
        if (getIsMultiplayer()) {
            difficultyChooser.setVisibility(View.INVISIBLE);
        } else {
            difficultyChooser.setVisibility(View.VISIBLE);
        }
    }
}