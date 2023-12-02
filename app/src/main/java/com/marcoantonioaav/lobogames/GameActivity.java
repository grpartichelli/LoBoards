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
import com.marcoantonioaav.lobogames.ui.OutOfBoardPositionsView;

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

    private Button goBackToMenu;
    private Button saveReplay;
    private Button playReplay;
    private Button restartGame;
    private Button goBackToReplay;
    private Button restartReplay;
    private Button startGame;
    private Button endGame;

    RelativeLayout buttonsLayout;
    private BoardView boardView;
    private OutOfBoardPositionsView topOutOfBoardPositionsView;
    private OutOfBoardPositionsView bottomOutOfBoardPositionsView;
    private final Map<Position, Button> positionButtonsMap = new HashMap<>();
    private TextView gameNameTextView, statusTextView;

    private Game game;
    private int turn;
    private Player player1;
    private Player player2;
    private boolean isGameRunning;

    private boolean isBoardMode = false; // when it is a generic game
    private boolean isReplayMode = false; // when it comes from replay activity

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

        if (GenericGame.NAME.equals(gameName)) {
            Board board = findBoardFromName(boardName);
            game = new GenericGame(board);
            game.setBoard(board);
            isBoardMode = true;
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

        // out of board positions
        topOutOfBoardPositionsView = findViewById(R.id.topOutOfBoardPositionsView);
        bottomOutOfBoardPositionsView = findViewById(R.id.bottomOutOfBoardPositionsView);

        // game name
        gameNameTextView = findViewById(R.id.gameName);
        gameNameTextView.setText(game.getName());

        // replay
        replayButtonsLayout = findViewById(R.id.replayLayout);

        // status
        findViewById(R.id.statusLayout).setVisibility(!isBoardMode ? View.VISIBLE : View.GONE);
        statusTextView = findViewById(R.id.status);

        goBackToMenu = findViewById(R.id.goBackToMenu);
        goBackToMenu.setOnClickListener(view -> finish());

        saveReplay = findViewById(R.id.saveReplay);
        saveReplay.setOnClickListener(view -> saveReplay());

        playReplay = findViewById(R.id.playReplay);
        playReplay.setOnClickListener(view -> {
            isReplayRunning = true;
            initializeGame();
        });

        restartGame = findViewById(R.id.restartGame);
        restartGame.setOnClickListener(view -> {
            isReplayRunning = false;
            initializeGame();
        });

        goBackToReplay = findViewById(R.id.goBackToReplay);
        goBackToReplay.setOnClickListener(view -> finish());

        restartReplay = findViewById(R.id.restartReplay);
        restartReplay.setEnabled(false);
        restartReplay.setOnClickListener(view -> {
            restartReplay.setEnabled(false);
            initializeGame();
        });

        startGame = findViewById(R.id.startGame);
        startGame.setOnClickListener(view -> {
            isReplayRunning = false;
            initializeGame();
            toggleStartGameEndGameButtons();
        });
        endGame = findViewById(R.id.endGame);
        endGame.setOnClickListener(view -> {
            endGame();
            toggleStartGameEndGameButtons();
        });

        if (isReplayMode) {
            goBackToReplay.setVisibility(View.VISIBLE);
            restartReplay.setVisibility(View.VISIBLE);
        } else {
            if (isBoardMode) {
                endGame.setVisibility(View.VISIBLE);
            } else {
                restartGame.setVisibility(View.VISIBLE);
            }
            playReplay.setVisibility(View.VISIBLE);
            saveReplay.setVisibility(View.VISIBLE);
            goBackToMenu.setVisibility(View.VISIBLE);
        }

        initializeGame();
        new Thread(GameActivity.this::gameLoop).start();
    }

    private void toggleStartGameEndGameButtons() {
        startGame.setVisibility(startGame.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        endGame.setVisibility(endGame.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private Board findBoardFromName(String boardName) {
        for (Board board : PreBoardActivity.BOARDS) {
            if (board.getName().equals(boardName)) {
                return board;
            }
        }
        return null;
    }

    private Replay findReplayFromName(String replayName) {
        for (Replay replay : REPLAYS) {
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

    private void setupBoardViewAndButtons() {
        buttonsLayout = findViewById(R.id.buttonsLayout);

        for (Button button : positionButtonsMap.values()) {
            buttonsLayout.removeView(button);
        }

        this.boardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                // setup board view size and obtain offset
                int buttonsLayoutWidth = buttonsLayout.getWidth();
                int buttonsLayoutHeight = buttonsLayout.getHeight();
                int outOfBoardHeight = 0;

                int boardViewSize = Math.min(buttonsLayoutWidth, buttonsLayoutHeight);
                if (isBoardMode) {
                    outOfBoardHeight = (int) (boardViewSize * 2.75 * game.getBoard().getPositionRadiusScale());

                    if (buttonsLayoutHeight - boardViewSize < 2 * outOfBoardHeight) {
                        boardViewSize = buttonsLayoutHeight - 2 * outOfBoardHeight;
                    }
                }

                int leftOffset = (buttonsLayoutWidth - boardViewSize) / 2;
                int topOffset = (buttonsLayoutHeight - boardViewSize) / 2;

                boardView.resize(boardViewSize);
                game.getBoard().scaleToLayoutParams(boardView.getLayoutParams());
                boardView.setBoard(game.getBoard().copy());
                boardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setupOutOfBoardPositionsView(boardViewSize, outOfBoardHeight, boardView.getBoard());

                // setup buttons
                double buttonSize = boardView.getSelectedPositionBorderRadius() * 2.5;
                Button previousButton = null;

                for (Position position : getPositionsSortedByAccessibility()) {
                    Button button = new Button(LoBoGames.getAppContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.height = (int) buttonSize;
                    layoutParams.width = (int) buttonSize;
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

    private void setupOutOfBoardPositionsView(int width, int height, Board board) {
        topOutOfBoardPositionsView.setBoard(board);
        topOutOfBoardPositionsView.resize(width, height);
        topOutOfBoardPositionsView.setVisibility(View.VISIBLE);
        topOutOfBoardPositionsView.setPlayerColor(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_2_COLOR, Color.GREEN));
        topOutOfBoardPositionsView.setPlayer(player1);
        topOutOfBoardPositionsView.setIsTop(true);
        topOutOfBoardPositionsView.setButtonsLayout(buttonsLayout);
        topOutOfBoardPositionsView.setBoardView(boardView);

        bottomOutOfBoardPositionsView.setBoard(board);
        bottomOutOfBoardPositionsView.resize(width, height);
        bottomOutOfBoardPositionsView.setVisibility(View.VISIBLE);
        bottomOutOfBoardPositionsView.setPlayerColor(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_1_COLOR, Color.GREEN));
        bottomOutOfBoardPositionsView.setPlayer(player1);
        bottomOutOfBoardPositionsView.setButtonsLayout(buttonsLayout);
        bottomOutOfBoardPositionsView.setBoardView(boardView);
    }

    private void setCursorByClick(Position selectedPosition) {
        if (isGameRunning) {
            Player player = resolvePlayer(turn);
            if (player instanceof Human) {
                ((Human) player).setCursor(selectedPosition);
                boardView.setSelectedPosition(selectedPosition);
                runOnUiThread(() -> boardView.announceForAccessibility("Selecionado " + selectedPosition.getId()));
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
                Player player = resolvePlayer(turn);
                if (player.isReady()) {
                    Move move = player.getMove(game);
                    if (game.isLegalMove(move)) {
                        makeMove(move);
                        if (game.isTerminalState()) {
                            endGame();
                        }
                    } else {
                        boardView.drawSelectedPosition();
                    }
                }
                validateReplayEnd();
            }
        }
    }

    private void validateReplayEnd() {
        boolean player1ReplayedEnded = player1 instanceof ReplayPlayer && ((ReplayPlayer) player1).isReplayFinished();
        boolean player2ReplayedEnded = player2 instanceof ReplayPlayer && ((ReplayPlayer) player2).isReplayFinished();

        if (player1ReplayedEnded && player2ReplayedEnded) {
            runOnUiThread(() -> restartReplay.setEnabled(true));
            endGame();
        } else if (player1ReplayedEnded) {
            turn = Player.PLAYER_2;
        } else if (player2ReplayedEnded) {
            turn = Player.PLAYER_1;
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
        Player currentPlayer = resolvePlayer(turn);
        if (currentPlayer instanceof Human) {
            if (isBoardMode) {
                ((Human) currentPlayer).clearCursor();
            }
            runOnUiThread(this::updateButtonsDescription);
        }
    }

    private Player resolvePlayer(int turn) {
        if (isBoardMode) {
            // in board mode we act as if a single player is playing
            return player1;
        }
        return Player.selectPlayerById(player1, player2, turn);
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
        setReplayButtonsVisibility(true);
        showResult();
    }

    private void initializeGame() {
        handleReplay();
        game.restart();
        boardView.reset();
        boardView.setBoard(game.getBoard().copy());
        setupBoardViewAndButtons();
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