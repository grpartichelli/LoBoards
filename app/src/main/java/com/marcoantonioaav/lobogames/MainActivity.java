package com.marcoantonioaav.lobogames;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button boards, games, replays, howToPlay, settings, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        boards = findViewById(R.id.buttonBoards);
        boards.setOnClickListener(view -> openPreGameActivity());

        games = findViewById(R.id.buttonGames);
        games.setOnClickListener(view -> openPreGameActivity());

        replays = findViewById(R.id.buttonReplays);
        replays.setOnClickListener(view -> openPreGameActivity());

        howToPlay = findViewById(R.id.buttonHowToPlay);
        howToPlay.setOnClickListener(view -> openHowToPlayActivity());

        settings = findViewById(R.id.buttonSettings);
        settings.setOnClickListener(view -> openSettingsActivity());

        about = findViewById(R.id.buttonAbout);
        about.setOnClickListener(view -> openAboutActivity());
    }

    private void openPreGameActivity() {
        startActivity(new Intent(this, PreGameActivity.class));
    }

    private void openHowToPlayActivity() { startActivity(new Intent(this, HowToPlayActivity.class)); }

    private void openSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openAboutActivity() {
        startActivity(new Intent(this, AboutActivity.class));
    }
}