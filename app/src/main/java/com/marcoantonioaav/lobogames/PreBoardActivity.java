package com.marcoantonioaav.lobogames;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.board.Board;

import com.marcoantonioaav.lobogames.board.StandardBoard;
import com.marcoantonioaav.lobogames.game.GenericGame;
import com.marcoantonioaav.lobogames.game.GenericGameFileService;

import java.util.List;
import java.util.Objects;

public class PreBoardActivity extends AppCompatActivity {
    private ListView boardListView;
    private Button play;
    private String selectedBoardName;
    private static final int IMPORT_FILE_CODE = 32;
    public EditText maxPiecesInput;

    public static final List<StandardBoard> BOARDS = GenericGameFileService.readAll();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        createBoardList();

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());
        play.setEnabled(false);

        maxPiecesInput = findViewById(R.id.maxPiecesInput);
        runOnUiThread(() -> {
            maxPiecesInput.setText("10");
        });


        findViewById(R.id.importBoard).setOnClickListener(view -> importFile());
    }

    private void createBoardList() {
        if (boardListView != null) {
            boardListView.setAdapter(null);
        }  else {
            boardListView = findViewById(R.id.boardList);
            boardListView.addHeaderView(new View(getBaseContext()), null, true);
            boardListView.addFooterView(new View(getBaseContext()), null, true);
        }

        BoardListAdapter adapter = new BoardListAdapter(this, R.layout.board_list_item, BOARDS);

        boardListView.setAdapter(adapter);
        boardListView.setOnItemClickListener(
                (AdapterView<?> ad, View v, int position, long id) -> {
                    selectedBoardName = ((StandardBoard) boardListView.getItemAtPosition(position)).getName();
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
        intent.putExtra(GameActivity.GAME_NAME, GenericGame.NAME);
        intent.putExtra(GameActivity.BOARD_NAME, selectedBoardName);
        intent.putExtra(GameActivity.MAX_POSITIONS, Integer.parseInt(maxPiecesInput.getText().toString()));
        intent.putExtra(GameActivity.IS_MULTIPLAYER, true);

        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_FILE_CODE) {
            StandardBoard board = GenericGameFileService.createFromIntent(data);

            for(int i=0; i < BOARDS.size(); i++){
                if(BOARDS.get(i).getName().equals(board.getName())){
                    BOARDS.remove(i);
                    break;
                }
            }

            BOARDS.add(0, board);
            selectedBoardName = board.getName();
            Toast.makeText(this, "Tabuleiro importado com sucesso", Toast.LENGTH_SHORT).show();
            createBoardList();
        }
    }

    private static class BoardListAdapter extends ArrayAdapter<StandardBoard> {

        private int resourceLayout;
        private Context mContext;

        public BoardListAdapter(Context context, int resource, List<StandardBoard> boards) {
            super(context, resource, boards);
            this.resourceLayout = resource;
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                view = layoutInflater.inflate(resourceLayout, null);
            }

            Board board = getItem(position);

            if (board != null) {
                TextView textView = view.findViewById(R.id.boardListItemTextView);

                if (textView != null) {
                    textView.setText(board.getName());
                    textView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                }


                ImageView imageView = view.findViewById(R.id.boardListItemImageView);

                if (imageView != null) {
                    // NOTE: Must be a copy to avoid issues with resetting games
                    imageView.setImageDrawable(board.getImageCopy());
                    imageView.setContentDescription(board.getName());
                }
            }

            return view;
        }

    }
}

