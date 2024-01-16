package com.gabrielpartichelli.loboards;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.gabrielpartichelli.loboards.game.GameModule;

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
        boards.setOnClickListener(view -> openPreBoardActivity());

        games = findViewById(R.id.buttonGames);
        games.setOnClickListener(view -> openPreGameActivity());

        replays = findViewById(R.id.buttonReplays);
        replays.setOnClickListener(view -> openReplayActivity());

//        howToPlay = findViewById(R.id.buttonHowToPlay);
//        howToPlay.setOnClickListener(view -> openHowToPlayActivity());

        settings = findViewById(R.id.buttonSettings);
        settings.setOnClickListener(view -> openSettingsActivity());

        about = findViewById(R.id.buttonAbout);
        about.setOnClickListener(view -> openAboutActivity());
    }

    private void openReplayActivity() {
        startActivity(new Intent(this, ReplayActivity.class));
    }

    private void openPreBoardActivity() {
        Intent intent  = new Intent(this, PlayOptionsActiviy.class);
        intent.putExtra(PlayOptionsActiviy.GAME_MODULE, GameModule.UNDEFINED);
        startActivity(intent);
    }

    private void openPreGameActivity() {
        startActivity(new Intent(this, ModuleActivity.class));
    }

    private void openHowToPlayActivity() { startActivity(new Intent(this, HowToPlayActivity.class)); }

    private void openSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openAboutActivity() {
        startActivity(new Intent(this, AboutActivity.class));
    }
}