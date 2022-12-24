package com.example.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.Objects;

public class CreditsActivity extends AppCompatActivity {
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        Objects.requireNonNull(getSupportActionBar()).hide();

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
    }
}