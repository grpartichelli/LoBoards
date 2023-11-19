package com.marcoantonioaav.lobogames;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.board.GenericGameBoardFactory;
import com.marcoantonioaav.lobogames.board.StandardBoard;
import com.marcoantonioaav.lobogames.exceptions.FailedToReadFileException;
import com.marcoantonioaav.lobogames.game.Game;
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
    private Button play, importReplay;
    private String selectedReplayName;
    private static final int IMPORT_FILE_CODE = 32;

    public static final List<StandardBoard> REPLAYS = GenericGameBoardFactory.createAll();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();


        createReplayList();

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());
        play.setEnabled(false);

        importReplay = findViewById(R.id.importReplay);
        importReplay.setOnClickListener(view -> importFile());
    }

    private void createReplayList() {
        if (replayListView != null) {
            replayListView.setAdapter(null);
        }
        replayListView = findViewById(R.id.replayList);
        replayListView.addHeaderView(new View(getBaseContext()), null, true);
        replayListView.addFooterView(new View(getBaseContext()), null, true);

        List<String> replayNames = new ArrayList<>();
        for(StandardBoard replay : REPLAYS){
            replayNames.add(replay.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.replay_list_item, R.id.replayListTextView, replayNames);
        //adapter.setDropDownViewResource(android.R.layout.activity_list_item);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_FILE_CODE) {
            try (InputStream stream = getContentResolver().openInputStream(data.getData())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = reader.readLine();
                JSONObject object = new JSONObject(line);
                String boardName = object.getString("name");
                File dir = new File(getFilesDir(), "boards");

                if(!dir.exists()){
                    dir.mkdir();
                }

                File newFile = new File(dir, boardName.toLowerCase().replaceAll(" ", "-") + "-lobogames-config.txt");
                FileWriter writer = new FileWriter(newFile);
                writer.append(line);
                writer.flush();
                writer.close();


                for(int i = 0; i < REPLAYS.size(); i++){
                    if(REPLAYS.get(i).getName().equals(boardName)){
                        REPLAYS.remove(i);
                        break;
                    }
                }

                REPLAYS.add(0, GenericGameBoardFactory.fromFilePath(newFile.getPath()));
                selectedReplayName = boardName;
                createReplayList();
            } catch (Exception e) {
                throw new FailedToReadFileException();
            }
        }
    }
}

