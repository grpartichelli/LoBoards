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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import com.marcoantonioaav.lobogames.replay.Replay;
import com.marcoantonioaav.lobogames.replay.ReplayFileService;
import com.marcoantonioaav.lobogames.ui.BoardButtonDelegate;
import com.marcoantonioaav.lobogames.ui.BoardView;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.marcoantonioaav.lobogames.ReplayActivity.REPLAYS;

public class GameActivity extends AppCompatActivity {
    public static final String REPLAY_NAME = "REPLAY_NAME";
    public static final String GAME_NAME = "GAME_NAME";
    public static final String BOARD_NAME = "BOARD_NAME";
    public static final String IS_MULTIPLAYER = "IS_MULTIPLAYER";
    public static final String DIFFICULTY = "DIFFICULTY";

    RelativeLayout buttonsLayout;
    private BoardView boardView;
    private final Map<Position, Button> positionButtonsMap = new HashMap<>();
    private TextView gameNameTextView, statusTextView;

    private Game game;
    private int turn;
    private Player player1;
    private Player player2;
    private boolean isGameRunning;

    private boolean isReplayMode; // when it comes from replay activity
    private boolean isReplayRunning;
    private Replay replay;
    private LinearLayout replayButtonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        String gameName = (String) this.getIntent().getExtras().get(GAME_NAME);
        String boardName = (String) this.getIntent().getExtras().get(BOARD_NAME);
        String replayName = (String) this.getIntent().getExtras().get(REPLAY_NAME);

        if (replayName != null) {
            replay = findReplayFromName(replayName);
            gameName = replay.getGameName();
            boardName = replay.getBoardName();
            isReplayRunning = true;
            isReplayMode = true;
        }

        if (gameName == null) {
            Board board = findBoardFromName(boardName);
            game = new GenericGame();
            game.setBoard(board);
        } else {
            game = PreGameActivity.GAMES.get(gameName);
        }


        setTitle(game.getName());
        updatePlayers();

        // buttons
        buttonsLayout = findViewById(R.id.buttonsLayout);

        // board
        boardView = findViewById(R.id.boardView);
        boardView.setPlayer1Color(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_1_COLOR, Color.GREEN));
        boardView.setPlayer2Color(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_2_COLOR, Color.RED));
        boardView.setCursorColor(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.CURSOR_COLOR, Color.BLUE));

        // game name
        gameNameTextView = findViewById(R.id.gameName);
        gameNameTextView.setText(game.getName());

        // status
        statusTextView = findViewById(R.id.status);

        // replay

        replayButtonsLayout = findViewById(R.id.replayLayout);

        // buttons when coming from game/board activity
        Button playReplay = findViewById(R.id.playReplay);
        playReplay.setOnClickListener(view -> {
            isReplayRunning = true;
            initializeGame();
        });
        playReplay.setVisibility(isReplayMode ? View.GONE : View.VISIBLE);

        Button saveReplay = findViewById(R.id.saveReplay);
        saveReplay.setOnClickListener(view -> saveReplay());
        saveReplay.setVisibility(isReplayMode ? View.GONE : View.VISIBLE);

        // buttons when coming from replay activity
        Button goToReplay = findViewById(R.id.goBackToReplay);
        goToReplay.setOnClickListener((view) -> finish());
        goToReplay.setVisibility(isReplayMode ?  View.VISIBLE : View.GONE);

        Button replayAgain = findViewById(R.id.replayAgain);
        replayAgain.setOnClickListener((view) -> initializeGame());
        replayAgain.setVisibility(isReplayMode ?  View.VISIBLE : View.GONE);


        // play again
        Button playAgain = findViewById(R.id.playAgain);
        playAgain.setOnClickListener(view -> {
            isReplayRunning = false;
            initializeGame();
        });
        playAgain.setVisibility(isReplayMode ? View.GONE : View.VISIBLE);

        // back
        Button goBackToMenu = findViewById(R.id.back);
        goBackToMenu.setOnClickListener(view -> finish());
        goBackToMenu.setVisibility(isReplayMode ? View.GONE : View.VISIBLE);


        initializeGame();
        new Thread(GameActivity.this::gameLoop).start();
    }

    private Board findBoardFromName(String boardName) {
        for (Board board: PreBoardActivity.BOARDS) {
            if (board.getName().equals(boardName)) {
                return board;
            }
        }
        return null;
    }

    private Replay findReplayFromName(String replayName) {
        for (Replay replay: REPLAYS) {
            if (replay.getName().equals(replayName)) {
                return replay;
            }
        }
        return null;
    }

    private void saveReplay() {
        for (int i = 0; i < REPLAYS.size(); i++) {
            if (REPLAYS.get(i).getName().equals(replay.getName())) {
                REPLAYS.remove(i);
                break;
            }
        }
        ReplayFileService.save(replay);
        REPLAYS.add(0, replay);
        Toast.makeText(this, "Replay salvo com sucesso", Toast.LENGTH_SHORT).show();
    }

    private void updatePlayers() {
        player1 = new Human(Player.PLAYER_1);
        if ((boolean) this.getIntent().getExtras().get(IS_MULTIPLAYER)) {
            player2 = new Human(Player.PLAYER_2);
        } else {
            player2 = new MinimaxAgent(Player.PLAYER_2, (String) this.getIntent().getExtras().get(DIFFICULTY));
        }
    }

    private void setUpButtons() {
        for (Button button: positionButtonsMap.values()) {
            buttonsLayout.removeView(button);
        }

        this.boardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int leftOffset = 0;
                int topOffset = 0;
                int width = buttonsLayout.getWidth();
                int height = buttonsLayout.getHeight();
                if (width > height) {
                    boardView.resize(height);
                    leftOffset = (width - height) / 2;
                } else {
                    boardView.resize(width);
                    topOffset = (height - width) / 2;
                }

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
                    layoutParams.leftMargin = leftOffset + position.getCoordinate().x() - (int) (buttonSize / 2);
                    layoutParams.topMargin = topOffset + position.getCoordinate().y() - (int) (buttonSize / 2);
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
                            setReplayButtonsVisibility(true);
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
        if (!isReplayRunning) {
            replay.addMove(move);
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
        setReplayButtonsVisibility(false);

        if (isReplayRunning) {
            player1 = new ReplayPlayer(Player.PLAYER_1, replay);
            player2 = new ReplayPlayer(Player.PLAYER_2, replay);
        } else {
            replay = new Replay(game, Calendar.getInstance().getTime());
            updatePlayers();
        }
    }

    private void setReplayButtonsVisibility(boolean areReplayButtonsVisible) {
        runOnUiThread(() -> {
            if (areReplayButtonsVisible) {
                replayButtonsLayout.setVisibility(View.VISIBLE);
            } else {
                replayButtonsLayout.setVisibility(View.INVISIBLE);
            }
        });
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