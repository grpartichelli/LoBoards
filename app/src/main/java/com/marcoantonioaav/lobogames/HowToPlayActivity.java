package com.marcoantonioaav.lobogames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.marcoantonioaav.lobogames.game.Game;

import java.util.Objects;

public class HowToPlayActivity extends AppCompatActivity {
    private Spinner gameSpinner;
    private TextView gameRules;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        gameRules = findViewById(R.id.gameRules);

        gameSpinner = findViewById(R.id.gameSpinner);
        ArrayAdapter ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreGameActivity.GAMES.keySet().toArray());
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(ad);
        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Game selectedGame = PreGameActivity.GAMES.get(gameSpinner.getSelectedItem());
                gameRules.setText(selectedGame.getRules());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
    }
}