package com.gabrielpartichelli.loboards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {
    private Button link, back;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        findViewById(R.id.linkButton).setOnClickListener(view -> openWebsite("https://www.inf.ufrgs.br/lobogames/"));
        findViewById(R.id.linkButton2).setOnClickListener(view -> openWebsite("https://play.google.com/store/apps/details?id=com.marcoantonioaav.lobogames"));
        version = findViewById(R.id.versionText);
        version.setText("Versão do aplicativo: " + BuildConfig.VERSION_NAME);
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
    }

    private void openWebsite(String link) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }
}