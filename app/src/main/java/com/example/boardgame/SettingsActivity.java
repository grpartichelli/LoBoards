package com.example.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.boardgame.ui.ColorChooserAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    public static final String SETTINGS = "SETTINGS";
    public static final String PLAYER_1_COLOR = "PLAYER_1_COLOR";
    public static final String PLAYER_2_COLOR = "PLAYER_2_COLOR";
    public static final String CURSOR_COLOR = "CURSOR_COLOR";

    private final Integer[] colors = new Integer[]{Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN};

    private Spinner colorChooser1, colorChooser2, colorChooser3;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).hide();

        colorChooser1 = findViewById(R.id.colorChooser1);
        colorChooser2 = findViewById(R.id.colorChooser2);
        colorChooser3 = findViewById(R.id.colorChooser3);
        save = findViewById(R.id.save);

        save.setOnClickListener(view -> saveAndQuit());

        colorChooser1.setAdapter(new ColorChooserAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(colors))));
        colorChooser1.setSelection(getIndexOf(getSharedPreferences(SETTINGS, MODE_PRIVATE).getInt(PLAYER_1_COLOR, Color.GREEN), colors));
        colorChooser1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int prevSelected = 0;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(colorChooser1.getSelectedItem() == colorChooser2.getSelectedItem())
                    adapterView.setSelection(prevSelected);
                else
                    prevSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        colorChooser2.setAdapter(new ColorChooserAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(colors))));
        colorChooser2.setSelection(getIndexOf(getSharedPreferences(SETTINGS, MODE_PRIVATE).getInt(PLAYER_2_COLOR, Color.RED), colors));
        colorChooser2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int prevSelected = 1;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(colorChooser1.getSelectedItem() == colorChooser2.getSelectedItem())
                    adapterView.setSelection(prevSelected);
                else
                    prevSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        colorChooser3.setAdapter(new ColorChooserAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(colors))));
        colorChooser3.setSelection(getIndexOf(getSharedPreferences(SETTINGS, MODE_PRIVATE).getInt(CURSOR_COLOR, Color.BLUE), colors));
    }

    private void saveAndQuit() {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        sharedPreferences.edit().putInt(PLAYER_1_COLOR, (int) colorChooser1.getSelectedItem()).apply();
        sharedPreferences.edit().putInt(PLAYER_2_COLOR, (int) colorChooser2.getSelectedItem()).apply();
        sharedPreferences.edit().putInt(CURSOR_COLOR, (int) colorChooser3.getSelectedItem()).apply();

        finish();
    }

    private int getIndexOf(int item, Integer[] array) {
        for(int i=0; i < array.length; i++)
            if(array[i] == item)
                return i;
        return 0;
    }
}