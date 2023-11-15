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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PreBoardActivity extends AppCompatActivity {
    private Spinner boardSpinner;
    private Button play;

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
        ArrayAdapter ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BOARDS.keySet().toArray());
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boardSpinner.setAdapter(ad);

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.BOARD_NAME, (String) boardSpinner.getSelectedItem());
        intent.putExtra(GameActivity.IS_MULTIPLAYER, true);

        startActivity(intent);
        finish();
    }
}
