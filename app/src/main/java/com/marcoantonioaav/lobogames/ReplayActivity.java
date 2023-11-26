package com.marcoantonioaav.lobogames;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.replay.Replay;
import com.marcoantonioaav.lobogames.replay.ReplayFileService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReplayActivity extends AppCompatActivity {
    private ListView replayListView;
    private TextView replayEmptyStateView;
    private Button play, delete,exportFile;
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

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(view -> deleteReplay());
        delete.setEnabled(false);

        exportFile = findViewById(R.id.export);
        exportFile.setOnClickListener(view -> exportReplay());
        exportFile.setEnabled(false);

        replayEmptyStateView = findViewById(R.id.replayEmptyState);
        replayEmptyStateView.setVisibility(View.GONE);

        findViewById(R.id.importReplay).setOnClickListener(view -> importFile());
        findViewById(R.id.back).setOnClickListener(view -> finish());

        createReplayList();
    }

    private void createReplayList() {
        if (replayListView != null) {
            replayListView.setAdapter(null);
        } else {
            replayListView = findViewById(R.id.replayList);
            replayListView.addHeaderView(new View(getBaseContext()), null, true);
            replayListView.addFooterView(new View(getBaseContext()), null, true);
        }

        if (REPLAYS.isEmpty()) {
            replayListView.setVisibility(View.GONE);
            replayEmptyStateView.setVisibility(View.VISIBLE);
        } else {
            replayListView.setVisibility(View.VISIBLE);
            replayEmptyStateView.setVisibility(View.GONE);
        }


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
                    delete.setEnabled(true);
                    exportFile.setEnabled(true);
                }
        );
    }

    private void deleteReplay() {
        for (int i = 0; i < REPLAYS.size(); i++) {
            Replay replay = REPLAYS.get(i);
            if (replay.getName().equals(selectedReplayName)) {

                int selectedReplayIndex = i;
                new AlertDialog.Builder(this)
                        .setTitle("Deleção do replay " + selectedReplayName)
                        .setMessage("Você tem certeza que deseja continuar? Não será possível recuperá-lo.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Continuar", (dialog, whichButton) -> {
                            REPLAYS.remove(selectedReplayIndex);
                            ReplayFileService.delete(replay);
                            Toast.makeText(this, "Deleção realizada com sucesso", Toast.LENGTH_SHORT).show();
                            play.setEnabled(false);
                            delete.setEnabled(false);
                            exportFile.setEnabled(false);
                            createReplayList();
                        })
                        .setNegativeButton("Cancelar", null).show();
                break;
            }
        }
    }

    private void exportReplay() {
        Replay replay = null;
        for (Replay r : REPLAYS) {
            if (r.getName().equals(selectedReplayName)) {
                replay = r;
                break;
            }
        }
        if (replay != null) {
            ReplayFileService.export(replay);
            Toast.makeText(this, "Replay baixado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }

    private void importFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, IMPORT_FILE_CODE);
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.REPLAY_NAME, selectedReplayName);
        intent.putExtra(GameActivity.IS_MULTIPLAYER, true);

        startActivity(intent);
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
            Toast.makeText(this, "Replay importado com sucesso", Toast.LENGTH_SHORT).show();
            createReplayList();
        }
    }
}

