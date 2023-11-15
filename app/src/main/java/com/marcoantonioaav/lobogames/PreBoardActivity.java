package com.marcoantonioaav.lobogames;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.GenericGameBoardFactory;
import com.marcoantonioaav.lobogames.board.StandardBoard;
import com.marcoantonioaav.lobogames.exceptions.FailedToReadFileException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PreBoardActivity extends AppCompatActivity {
    private Spinner boardSpinner;
    private Button play, importBoard;
    private static final int IMPORT_FILE_CODE = 32;

    public static final Map<String, Board> BOARDS = new HashMap<String, Board>() {{
         for (StandardBoard board : GenericGameBoardFactory.createAll()) {
             put(board.getName(), board);
         }
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        boardSpinner = findViewById(R.id.gameSpinner);
        updateBoardSpinner();

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());

        importBoard = findViewById(R.id.importBoard);
        importBoard.setOnClickListener(view -> importFile());
    }

    private void updateBoardSpinner() {
        ArrayAdapter ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BOARDS.keySet().toArray());
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boardSpinner.setAdapter(ad);
    }

    private void importFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, IMPORT_FILE_CODE);
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.BOARD_NAME, (String) boardSpinner.getSelectedItem());
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

                BOARDS.put(boardName, GenericGameBoardFactory.fromFilePath(newFile.getPath()));
                updateBoardSpinner();
                boardSpinner.setSelection(BOARDS.size() - 1);
            } catch (Exception e) {
                throw new FailedToReadFileException();
            }
        }
    }
}
