package com.marcoantonioaav.lobogames;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.game.GameModule;

import java.util.Objects;

public class ModuleSelectionActivity extends AppCompatActivity {
    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_selection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        findViewById(R.id.blockAlignmentButton).setOnClickListener(v -> {
            openGameSelectionActivity(GameModule.ALIGNMENT_OR_BLOCK);
        });

        findViewById(R.id.dislocationButton).setOnClickListener(v -> {
            openGameSelectionActivity(GameModule.DISLOCATION);
        });

        findViewById(R.id.positionButton).setOnClickListener(v -> {
            openGameSelectionActivity(GameModule.POSITION);
        });

        findViewById(R.id.captureButton).setOnClickListener(v -> {
            openGameSelectionActivity(GameModule.CAPTURE);
        });

        findViewById(R.id.huntButton).setOnClickListener(v -> {
            openGameSelectionActivity(GameModule.HUNT);
        });
    }


    private void openGameSelectionActivity(GameModule gameModule) {
        Intent intent = new Intent(this, GameSelectionActivity.class);
        intent.putExtra("gameModule", gameModule);
        startActivity(intent);
        finish();
    }

}