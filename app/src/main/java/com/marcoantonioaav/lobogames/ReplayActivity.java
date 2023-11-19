package com.marcoantonioaav.lobogames;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.board.GenericGameFileService;
import com.marcoantonioaav.lobogames.board.StandardBoard;
import com.marcoantonioaav.lobogames.exceptions.FailedToReadFileException;
import com.marcoantonioaav.lobogames.replay.Replay;
import com.marcoantonioaav.lobogames.replay.ReplayFileService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReplayActivity extends AppCompatActivity {
    private ListView replayListView;
    private TextView replayEmptyStateView;
    private Button play;
    private String selectedReplayName;
    private static final int IMPORT_FILE_CODE = 32;

    public static final List<Replay> REPLAYS = ReplayFileService.readAll();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());
        play.setEnabled(false);

        replayEmptyStateView = findViewById(R.id.replayEmptyState);
        replayEmptyStateView.setVisibility(View.INVISIBLE);

        findViewById(R.id.importReplay).setOnClickListener(view -> importFile());
        findViewById(R.id.back).setOnClickListener(view -> finish());

        createReplayList();
    }

    private void createReplayList() {
        if (replayListView != null) {
            replayListView.setAdapter(null);
        }
        replayListView = findViewById(R.id.replayList);
        if (REPLAYS.isEmpty()) {
            replayListView.setVisibility(View.INVISIBLE);
            replayEmptyStateView.setVisibility(View.VISIBLE);
        }
        replayListView.addHeaderView(new View(getBaseContext()), null, true);
        replayListView.addFooterView(new View(getBaseContext()), null, true);

        List<String> replayNames = new ArrayList<>();
        for (Replay replay : REPLAYS) {
            replayNames.add(replay.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.replay_list_item, R.id.replayListTextView, replayNames);
        replayListView.setAdapter(adapter);

        replayListView.setOnItemClickListener(
                (AdapterView<?> ad, View v, int position, long id) -> {
                    selectedReplayName = (String) replayListView.getItemAtPosition(position);
                    play.setEnabled(true);
                }
        );
    }

    private void importFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, IMPORT_FILE_CODE);
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.BOARD_NAME, selectedReplayName);
        intent.putExtra(GameActivity.IS_MULTIPLAYER, true);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_FILE_CODE) {
            Replay replay = ReplayFileService.createFromIntent(data);

            for (int i = 0; i < REPLAYS.size(); i++) {
                if (REPLAYS.get(i).getName().equals(replay.getName())) {
                    REPLAYS.remove(i);
                    break;
                }
            }

            REPLAYS.add(0, replay);
            selectedReplayName = replay.getName();
            createReplayList();
        }
    }
}

