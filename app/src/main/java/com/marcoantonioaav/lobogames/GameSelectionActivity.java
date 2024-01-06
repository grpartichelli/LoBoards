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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.game.GameModule;
import com.marcoantonioaav.lobogames.game.GenericGame;
import com.marcoantonioaav.lobogames.game.GenericGameFileService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameSelectionActivity extends AppCompatActivity {
    private ListView gameListView;
    private Button playButton, importButton, textButton, videoButton;
    private GenericGame selectedGame;
    private static final int IMPORT_FILE_CODE = 32;
    public EditText maxPlayerPositionsCountSelector;
    public static final String GAME_MODULE = "GAME_MODULE";
    GameModule gameModule;


    public static final List<GenericGame> GAMES = GenericGameFileService.readAll();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        gameModule = (GameModule) this.getIntent().getExtras().get(GAME_MODULE);

        playButton = findViewById(R.id.play);
        playButton.setOnClickListener(view -> openGameActivity());
        playButton.setEnabled(false);

        textButton = findViewById(R.id.textLink);
        textButton.setVisibility(gameModule.isUndefined() ? View.GONE : View.VISIBLE);
        textButton.setEnabled(false);

        videoButton = findViewById(R.id.videoLink);
        videoButton.setVisibility(gameModule.isUndefined() ? View.GONE : View.VISIBLE);
        videoButton.setEnabled(false);

        maxPlayerPositionsCountSelector = findViewById(R.id.maxPiecesInput);
        maxPlayerPositionsCountSelector.setVisibility(gameModule.isUndefined() ? View.VISIBLE : View.GONE);
        runOnUiThread(() -> maxPlayerPositionsCountSelector.setText("10"));

        findViewById(R.id.numberLabel).setVisibility(gameModule.isUndefined() ? View.VISIBLE : View.GONE);

        TextView boardListLabel = findViewById(R.id.boardListLabel);
        boardListLabel.setText(gameModule.isUndefined() ? "Selecione um tabuleiro:" : "Selecione um jogo:");

        importButton = findViewById(R.id.importBoard);
        importButton.setVisibility(View.GONE); // NOTE: Disabled for now
        importButton.setOnClickListener(view -> importFile());

        createBoardList();
    }

    private void createBoardList() {
        if (gameListView != null) {
            gameListView.setAdapter(null);
        } else {
            gameListView = findViewById(R.id.boardList);
            gameListView.addHeaderView(new View(getBaseContext()), null, true);
            gameListView.addFooterView(new View(getBaseContext()), null, true);
        }

        BoardListAdapter adapter = new BoardListAdapter(this, R.layout.board_list_item, resolveGamesFilteredByModule());

        gameListView.setAdapter(adapter);
        gameListView.setOnItemClickListener(
                (AdapterView<?> ad, View v, int position, long id) -> {
                    selectedGame = ((GenericGame) gameListView.getItemAtPosition(position));
                    playButton.setEnabled(true);
                    if (!selectedGame.getVideoUrl().isEmpty()) {
                        videoButton.setEnabled(true);

                    }

                    if (!selectedGame.getTextUrl().isEmpty()) {
                        textButton.setEnabled(true);
                    }
                }
        );
    }

    private List<GenericGame> resolveGamesFilteredByModule() {
        List<GenericGame> filteredGames = new ArrayList<>();
        for (GenericGame game : GAMES) {
            if (game.getModule().equals(gameModule)) {
                filteredGames.add(game);
            }
        }
        return filteredGames;
    }

    private void importFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, IMPORT_FILE_CODE);
    }

    private void openGameActivity() {
        int positionsCount =
                Integer.parseInt(maxPlayerPositionsCountSelector.getText().toString().isEmpty()
                        ? "10"
                        : maxPlayerPositionsCountSelector.getText().toString());

        if (selectedGame.getMaxPlayerPositionsCount() == 0) {
            selectedGame.setMaxPlayerPositionsCount(positionsCount);
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.GAME_NAME, selectedGame.getName());
        intent.putExtra(GameActivity.IS_FREE_MOVEMENT_MODE, true);
        intent.putExtra(GameActivity.IS_MULTIPLAYER, true);

        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_FILE_CODE) {
            List<GenericGame> games = GenericGameFileService.createFromIntent(data);

            for (int i = 0; i < GAMES.size(); i++) {
                for (int j = 0; j < games.size(); j++) {
                    if (GAMES.get(i).getName().equals(games.get(i).getName())) {
                        GAMES.remove(i);
                        break;
                    }
                }
            }

            GAMES.addAll(games);
            selectedGame = games.get(0);
            Toast.makeText(this, "Tabuleiro importado com sucesso", Toast.LENGTH_SHORT).show();
            createBoardList();
        }
    }

    private static class BoardListAdapter extends ArrayAdapter<GenericGame> {

        private int resourceLayout;
        private Context mContext;

        public BoardListAdapter(Context context, int resource, List<GenericGame> games) {
            super(context, resource, games);
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

            GenericGame game = getItem(position);

            if (game != null) {
                TextView textView = view.findViewById(R.id.boardListItemTextView);

                if (textView != null) {
                    textView.setText(game.getName());
                    textView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                }


                ImageView imageView = view.findViewById(R.id.boardListItemImageView);

                if (imageView != null) {
                    // NOTE: Must be a copy to avoid issues with resetting games
                    imageView.setImageDrawable(game.getBoard().getImageCopy());
                    imageView.setContentDescription(game.getName());
                }
            }

            return view;
        }

    }
}

