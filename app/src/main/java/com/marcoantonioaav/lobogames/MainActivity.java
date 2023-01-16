package com.marcoantonioaav.lobogames;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button singleplayer, multiplayer, howToPlay, settings, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        singleplayer = findViewById(R.id.buttonSingleplayer);
        singleplayer.setOnClickListener(view -> openPreGameActivity(false));
        multiplayer = findViewById(R.id.buttonMultiplayer);
        multiplayer.setOnClickListener(view -> openPreGameActivity(true));
        howToPlay = findViewById(R.id.buttonHowToPlay);
        howToPlay.setOnClickListener(view -> openHowToPlayActivity());
        settings = findViewById(R.id.buttonSettings);
        settings.setOnClickListener(view -> openSettingsActivity());
        about = findViewById(R.id.buttonAbout);
        about.setOnClickListener(view -> openAboutActivity());
    }

    private void openPreGameActivity(boolean isMultiplayer) {
        Intent intent = new Intent(this, PreGameActivity.class);
        intent.putExtra(PreGameActivity.IS_MULTIPLAYER, isMultiplayer);
        startActivity(intent);
    }

    private void openHowToPlayActivity() { startActivity(new Intent(this, HowToPlayActivity.class)); }

    private void openSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openAboutActivity() {
        startActivity(new Intent(this, AboutActivity.class));
    }
}