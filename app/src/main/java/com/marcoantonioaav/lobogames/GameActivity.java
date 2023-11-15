package com.marcoantonioaav.lobogames;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.game.GenericGame;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Human;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.ReplayPlayer;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;
import com.marcoantonioaav.lobogames.position.Position;
import com.marcoantonioaav.lobogames.ui.BoardButtonDelegate;
import com.marcoantonioaav.lobogames.ui.BoardView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    public static final String GAME_NAME = "GAME_NAME";
    public static final String BOARD_NAME = "BOARD_NAME";
    public static final String IS_MULTIPLAYER = "IS_MULTIPLAYER";
    public static final String DIFFICULTY = "DIFFICULTY";

    private BoardView boardView;
    private final Map<Position, Button> positionButtonsMap = new HashMap<>();
    private TextView gameNameTextView, statusTextView;
    private Button playAgain, back;

    private Game game;
    private int turn;
    private Player player1;
    private Player player2;
    private boolean isGameRunning;

    private boolean isReplay;
    private final ReplayPlayer replayPlayer1 = new ReplayPlayer(Player.PLAYER_1);
    private final ReplayPlayer replayPlayer2= new ReplayPlayer(Player.PLAYER_2);
    private Button replay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        String gameName = (String) this.getIntent().getExtras().get(GAME_NAME);
        if (gameName == null) {
            String boardName = (String) this.getIntent().getExtras().get(BOARD_NAME);
            Board board = PreBoardActivity.BOARDS.get(boardName);
            game = new GenericGame();
            game.setBoard(board);
        } else {
            game = PreGameActivity.GAMES.get(gameName);
        }


        setTitle(game.getName());
        updatePlayers();

        // board
        boardView = findViewById(R.id.boardView);
        boardView.resizeToScreenSize();
        boardView.setPlayer1Color(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_1_COLOR, Color.GREEN));
        boardView.setPlayer2Color(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_2_COLOR, Color.RED));
        boardView.setCursorColor(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.CURSOR_COLOR, Color.BLUE));

        // game name
        gameNameTextView = findViewById(R.id.gameName);
        gameNameTextView.setText(game.getName());

        // status
        statusTextView = findViewById(R.id.status);

        // replay
        replay = findViewById(R.id.replay);
        replay.setOnClickListener(view -> {
            isReplay = true;
            initializeGame();
        });

        // play again
        playAgain = findViewById(R.id.playAgain);
        playAgain.setOnClickListener(view -> {
            isReplay = false;
            initializeGame();
        });

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());

        initializeGame();
        new Thread(GameActivity.this::gameLoop).start();
    }

    private void updatePlayers() {
        player1 = new Human(Player.PLAYER_1);
        if ((boolean) this.getIntent().getExtras().get(IS_MULTIPLAYER)) {
            player2 = new Human(Player.PLAYER_2);
        } else {
            player2 = new MinimaxAgent(Player.PLAYER_2, (String) this.getIntent().getExtras().get(DIFFICULTY));
        }
    }

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("text/plain");
//        startActivityForResult(intent, 32);

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 32){
//            try (InputStream stream = getContentResolver().openInputStream(data.getData())) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//                String line = reader.readLine();
//                log(line);
//            } catch (Exception e) {
//                log(e.getMessage());
//            }
//        }
//    }


    private void setUpButtons() {
        RelativeLayout buttonsLayout = findViewById(R.id.buttonsLayout);
        for (Button button: positionButtonsMap.values()) {
            buttonsLayout.removeView(button);
        }

        this.boardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout buttonsLayout = findViewById(R.id.buttonsLayout);
                double buttonSize = boardView.getSelectedPositionBorderRadius() * 2.5;
                game.getBoard().scaleToLayoutParams(boardView.getLayoutParams());
                boardView.setBoard(game.getBoard().copy());
                Button previousButton = null;

                for (Position position:  getPositionsSortedByAccessibility()) {
                    Button button = new Button(LoBoGames.getAppContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.height = (int) (buttonSize);
                    layoutParams.width = (int) (buttonSize);
                    layoutParams.leftMargin = position.getCoordinate().x() - (int) (buttonSize / 2);
                    layoutParams.topMargin = position.getCoordinate().y() - (int) (buttonSize / 2);
                    button.setOnClickListener(view -> setCursorByClick(position));
                    button.setBackgroundColor(Color.TRANSPARENT);
                    buttonsLayout.addView(button, layoutParams);
                    positionButtonsMap.put(position, button);
                    if (previousButton != null) {
                        ViewCompat.setAccessibilityDelegate(button, new BoardButtonDelegate(previousButton));
                    }
                    previousButton = button;
                }
                updateButtonsDescription();
            }
        });
    }

    private void setCursorByClick(Position selectedPosition) {
        if (isGameRunning) {
            Player player = Player.selectPlayerById(player1, player2, turn);
            if (player instanceof Human) {
                ((Human) player).setCursor(selectedPosition);
                boardView.setSelectedPosition(selectedPosition);
                runOnUiThread(() -> boardView.announceForAccessibility("Selecionado " +  selectedPosition.getId()));
            }
        }
    }

    public List<Position> getPositionsSortedByAccessibility() {
        List<Position> accessibilitySortedPositions = game.getBoard().getPositions();
        Collections.sort(
                accessibilitySortedPositions,
                (p1, p2) -> p1.getAccessibilityOrder() - p2.getAccessibilityOrder());
        return accessibilitySortedPositions;
    }

    private void gameLoop() {
        while (true) {
            if (isGameRunning) {
                Player player = Player.selectPlayerById(player1, player2, turn);
                if (player.isReady()) {
                    Move move = player.getMove(game);
                    if (game.isLegalMove(move)) {
                        makeMove(move);
                        if (game.isTerminalState()) {
                            runOnUiThread(() -> replay.setVisibility(View.VISIBLE));
                            endGame();
                        }
                    } else {
                        boardView.drawSelectedPosition();
                    }
                }
            }
        }
    }

    private void makeMove(Move move) {
        if (move == null) {
            return;
        }
        if (!isReplay) {
            if (move.getPlayerId() == Player.PLAYER_1) {
                replayPlayer1.addMove(move);
            } else {
                replayPlayer2.addMove(move );
            }
        }

        game.getBoard().applyMove(move);
        runOnUiThread(() -> boardView.announceForAccessibility(move.toString()));
        runOnUiThread(() -> boardView.drawMove(move));
        turn = Player.getOpponentOf(turn);
        showTurn();
        Player currentPlayer = Player.selectPlayerById(player1, player2, turn);
        if (currentPlayer instanceof Human) {
             runOnUiThread(this::updateButtonsDescription);
        }
    }

    private void updateButtonsDescription() {
        for (Position position : this.game.getBoard().getPositions()) {
            Button button = positionButtonsMap.get(position);
            if (button == null) {
                throw new IllegalStateException("Non existing button for position: " + position.getId());
            }
            button.setContentDescription(position.getId() + ": " + Player.getName(position.getPlayerId()));
            positionButtonsMap.put(position, button);
        }
    }

    private void endGame() {
        isGameRunning = false;
        showResult();
    }

    private void initializeGame() {
        handleReplay();
        game.restart();
        boardView.reset();
        boardView.setBoard(game.getBoard().copy());
        setUpButtons();
        turn = Player.PLAYER_1;
        showTurn();
        isGameRunning = true;
    }

    private void handleReplay() {
        runOnUiThread(() -> replay.setVisibility(View.INVISIBLE));
        replayPlayer1.reset();
        replayPlayer2.reset();

        if (isReplay) {
            player1 = replayPlayer1;
            player2 = replayPlayer2;
        } else {
            replayPlayer1.clearMoves();
            replayPlayer2.clearMoves();
            updatePlayers();
        }
    }

    private void showTurn() {
        String statusMessage = "Vez do " + Player.getName(turn);
        runOnUiThread(() -> {
            statusTextView.setText(statusMessage);
            statusTextView.announceForAccessibility(statusMessage);
            statusTextView.setTextColor(boardView.getPlayerColor(turn));
        });
    }

    private void showResult() {
        int resultColor;
        String resultText;
        if (game.isVictory(player1.getId())) {
            resultText = Player.getName(Player.PLAYER_1) + " venceu!";
            resultColor = boardView.getPlayerColor(Player.PLAYER_1);
        } else if (game.isVictory(player2.getId())) {
            resultText = Player.getName(Player.PLAYER_2) + " venceu!";
            resultColor = boardView.getPlayerColor(Player.PLAYER_2);
        } else {
            resultText = "Empate";
            resultColor = boardView.getPlayerColor(Player.EMPTY);
        }
        runOnUiThread(() -> statusTextView.announceForAccessibility(resultText));
        SpannableString spanString = new SpannableString(resultText.toUpperCase());
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        runOnUiThread(() -> {
            statusTextView.setText(spanString);
            statusTextView.setTextColor(resultColor);
        });
    }
}