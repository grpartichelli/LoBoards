package com.example.boardgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button singleplayer, multiplayer, settings, credits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        singleplayer = findViewById(R.id.buttonSingleplayer);
        singleplayer.setOnClickListener(view -> openPreGameActivity(false));
        multiplayer = findViewById(R.id.buttonMultiplayer);
        multiplayer.setOnClickListener(view -> openPreGameActivity(true));
        settings = findViewById(R.id.buttonSettings);
        settings.setOnClickListener(view -> openSettingsActivity());
        credits = findViewById(R.id.buttonCredits);
        credits.setOnClickListener(view -> openCreditsActivity());
    }

    private void openPreGameActivity(boolean isMultiplayer) {
        Intent intent = new Intent(this, PreGameActivity.class);
        intent.putExtra(PreGameActivity.IS_MULTIPLAYER, isMultiplayer);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openCreditsActivity() {
        startActivity(new Intent(this, CreditsActivity.class));
    }
}