package com.gabrielpartichelli.loboards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;

import com.gabrielpartichelli.loboards.game.*;
import com.gabrielpartichelli.loboards.player.agent.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * NOTE: This is currently not being used, it's LoBoGames PreGameActivity
 * // but updated with new UI elements and the new Game class
 */
public class PreGameActivity extends AppCompatActivity {
    private RadioGroup difficultyChooser, numberOfPlayersChooser;
    private Button play;
    private ListView gameListView;
    private String selectedGameName;

    public static final Map<String, Game> GAMES = new HashMap<String, Game>() {{
        put(new Tapatan().getName(), new Tapatan());
        put(new TicTacToe().getName(), new TicTacToe());
        put(new Alquerque().getName(), new Alquerque());
        put(new FiveFieldKono().getName(), new FiveFieldKono());
        put(new TsoroYematatuV2().getName(), new TsoroYematatuV2());
        put(new PongHauKi().getName(), new PongHauKi());
        put(new Shisima().getName(), new Shisima());
        put(new WatermelonChess().getName(), new WatermelonChess());
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        difficultyChooser = findViewById(R.id.difficultyChooser);
        numberOfPlayersChooser = findViewById(R.id.numberOfPlayersChooser);
        numberOfPlayersChooser.setOnCheckedChangeListener((radioGroup, i) -> updateDifficultyChooserVisibility());
        updateDifficultyChooserVisibility();

        createGameList();

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());
        play.setEnabled(false);
    }


    private void createGameList() {
        if (gameListView != null) {
            gameListView.setAdapter(null);
        } else {
            gameListView = findViewById(R.id.gameListView);
            gameListView.addHeaderView(new View(getBaseContext()), null, true);
            gameListView.addFooterView(new View(getBaseContext()), null, true);
        }

        GameListAdapter adapter = new GameListAdapter(this, R.layout.board_list_item, new ArrayList<>(GAMES.values()));

        gameListView.setAdapter(adapter);
        gameListView.setOnItemClickListener(
                (AdapterView<?> ad, View v, int position, long id) -> {
                    selectedGameName = ((Game) gameListView.getItemAtPosition(position)).getName();
                    play.setEnabled(true);
                }
        );
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.GAME_NAME, selectedGameName);
        intent.putExtra(GameActivity.IS_FREE_MOVEMENT_MODE, false);
        intent.putExtra(GameActivity.IS_MULTIPLAYER, getIsMultiplayer());
        intent.putExtra(GameActivity.DIFFICULTY, getDifficulty());

        startActivity(intent);
        finish();
    }

    private String getDifficulty() {
        int buttonId = difficultyChooser.getCheckedRadioButtonId();
        if(buttonId == R.id.radioMedium)
            return Agent.MEDIUM_DIFFICULTY;
        if(buttonId == R.id.radioHard)
            return Agent.HARD_DIFFICULTY;
        return Agent.EASY_DIFFICULTY;
    }

    private boolean getIsMultiplayer() {
        return numberOfPlayersChooser.getCheckedRadioButtonId() == R.id.radioMultiplayer;
    }

    private void updateDifficultyChooserVisibility() {
        if (getIsMultiplayer()) {
            difficultyChooser.setVisibility(View.INVISIBLE);
        } else {
            difficultyChooser.setVisibility(View.VISIBLE);
        }
    }

    private static class GameListAdapter extends ArrayAdapter<Game> {

        private int resourceLayout;
        private Context mContext;

        public GameListAdapter(Context context, int resource, List<Game> games) {
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

            Game game = getItem(position);

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