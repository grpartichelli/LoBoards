package com.gabrielpartichelli.loboards;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import com.gabrielpartichelli.loboards.application.LoBoards;
import com.gabrielpartichelli.loboards.board.Board;
import com.gabrielpartichelli.loboards.game.Game;
import com.gabrielpartichelli.loboards.game.GenericGame;
import com.gabrielpartichelli.loboards.move.Move;
import com.gabrielpartichelli.loboards.move.Movement;
import com.gabrielpartichelli.loboards.player.Human;
import com.gabrielpartichelli.loboards.player.Player;
import com.gabrielpartichelli.loboards.player.ReplayPlayer;
import com.gabrielpartichelli.loboards.player.agent.MinimaxAgent;
import com.gabrielpartichelli.loboards.position.Position;
import com.gabrielpartichelli.loboards.replay.Replay;
import com.gabrielpartichelli.loboards.replay.ReplayFileService;
import com.gabrielpartichelli.loboards.ui.BoardButtonDelegate;
import com.gabrielpartichelli.loboards.ui.BoardView;
import com.gabrielpartichelli.loboards.ui.OutOfBoardPositionsView;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.gabrielpartichelli.loboards.ReplayActivity.REPLAYS;

public class GameActivity extends AppCompatActivity {
    public static final String REPLAY_NAME = "REPLAY_NAME";
    public static final String GAME_NAME = "GAME_NAME";
    public static final String IS_FREE_MOVEMENT_MODE = "IS_FREE_MOVEMENT_MODE";

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

    private boolean isFreeMovementMode = false; // when it comes from game selection activity
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
        String replayName = (String) this.getIntent().getExtras().get(REPLAY_NAME);
        isFreeMovementMode = (boolean) this.getIntent().getExtras().get(IS_FREE_MOVEMENT_MODE);

        if (replayName != null) {
            replay = findReplayFromName(replayName);
            gameName = replay.getGameName();
            isReplayMode = true;
            isReplayRunning = true;
        }

        if (isFreeMovementMode) {
            GenericGame genericGame = findGameFromName(gameName);
            if (isReplayMode) {
                genericGame.setMaxPlayerPositionsCount(replay.getMaxPositions());
            }
            game = genericGame;
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
        findViewById(R.id.statusLayout).setVisibility(!isFreeMovementMode ? View.VISIBLE : View.GONE);
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
            if (isFreeMovementMode) {
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
        if (startGame.getVisibility() == View.VISIBLE) {
            runOnUiThread(() -> {
                endGame.setVisibility(View.VISIBLE);
                startGame.setVisibility(View.GONE);
            });
        } else {
            runOnUiThread(() -> {
                startGame.setVisibility(View.VISIBLE);
                startGame.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                endGame.setVisibility(View.GONE);
            });
        }

    }

    private GenericGame findGameFromName(String gameName) {
        for (GenericGame game : GameSelectionActivity.GAMES) {
            if (game.getName().equals(gameName)) {
                return game;
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
                if (isFreeMovementMode) {
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

                if (isFreeMovementMode) {
                    setupOutOfBoardPositionsViews(boardViewSize, outOfBoardHeight, boardView.getBoard());
                }

                // setup buttons
                double buttonSize = boardView.getSelectedPositionBorderRadius() * 2.5;
                Button previousButton = null;

                for (Position position : getPositionsSortedByAccessibility()) {
                    Button button = new Button(LoBoards.getAppContext());
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
                    } else {
                        setupOutOfBoardAccessibilityRelatedButton(button, true);
                    }
                    previousButton = button;
                }

                setupOutOfBoardAccessibilityRelatedButton(previousButton, false);
                updateButtonsDescription();
            }
        });
    }

    private void setupOutOfBoardAccessibilityRelatedButton(Button button, boolean isFirst) {
        if (!isFreeMovementMode) {
            return;
        }

        if (isFirst) {
            topOutOfBoardPositionsView.setFirstAccessibilityButton(button);
        } else {
            bottomOutOfBoardPositionsView.setLastAccessibilityButton(button);
        }
    }

    private void setupOutOfBoardPositionsViews(int width, int height, Board board) {
        setupOutOfBoardPositionsView(topOutOfBoardPositionsView, width, height, board, true);
        setupOutOfBoardPositionsView(bottomOutOfBoardPositionsView, width, height, board, false);
    }

    private void setupOutOfBoardPositionsView(OutOfBoardPositionsView positionsView, int width, int height, Board board, boolean isTop) {
        positionsView.setBoard(board);
        positionsView.resize(width, height);
        positionsView.setVisibility(View.VISIBLE);
        int color = isTop
                ? getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_2_COLOR, Color.RED)
                : getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_1_COLOR, Color.GREEN);
        positionsView.setPlayerColor(color);
        positionsView.setIsTop(isTop);
        positionsView.setButtonsLayout(buttonsLayout);
        positionsView.setBoardView(boardView);
        positionsView.setCursorColor(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.CURSOR_COLOR, Color.BLUE));
        positionsView.setOnClickListener(
                () -> {
                    if (player1 instanceof Human) {
                        ((Human) player1).setCursor(Position.instanceOutOfBoardForPlayerId(positionsView.getMovePlayerId()));
                    }

                    positionsView.setSelection(positionsView.isPositionEnabled());
                    runOnUiThread(() -> positionsView.announceForAccessibility("Selecionado Peças do " + Player.getName(positionsView.getMovePlayerId())));

                    if (positionsView.isTop()) {
                        bottomOutOfBoardPositionsView.setSelection(false);
                    } else {
                        topOutOfBoardPositionsView.setSelection(false);
                    }

                    return null;
                }
        );
    }


    private void setCursorByClick(Position selectedPosition) {

        resetOutOfBoardSelection();

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

        int pos = 0;
        if (!move.getMovements().isEmpty()) {
            pos = this.game.getBoard()
                    .findPositionById(move.getMovements().get(0).getStartPositionId()).getPlayerId();
        }
        final int startPositionPlayerId = pos;


        game.getBoard().applyMove(move);
        updatePositionsCount();
        resetOutOfBoardSelection();

        runOnUiThread(() -> boardView.announceForAccessibility(resolveMoveAnnounceMessage(move, startPositionPlayerId)));
        runOnUiThread(() -> boardView.drawMove(move));


        turn = Player.getOpponentOf(turn);
        showTurn();
        Player currentPlayer = resolvePlayer(turn);
        if (currentPlayer instanceof Human) {
            if (isFreeMovementMode) {
                ((Human) currentPlayer).clearCursor();
            }
            runOnUiThread(this::updateButtonsDescription);
        }
    }

    private String resolveMoveAnnounceMessage(Move move, int startPositionPlayerId) {
        if (move.getMovements().isEmpty()) {
            return "";
        }


        int playerId = isFreeMovementMode ? startPositionPlayerId : move.getPlayerId();
        String result = Player.getName(playerId) + ": ";
        for (Movement movement : move.getMovements()) {
            result += " " + movement.toString();
        }

        return result;
    }

    private void resetOutOfBoardSelection() {
        if (isFreeMovementMode) {
            topOutOfBoardPositionsView.setSelection(false);
            bottomOutOfBoardPositionsView.setSelection(false);
        }
    }

    private Player resolvePlayer(int turn) {
        if (isFreeMovementMode) {
            // in free movement mode we act as if a single player is playing
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

    private void updatePositionsCount() {
        if (isFreeMovementMode) {
            runOnUiThread(() -> {
                bottomOutOfBoardPositionsView.setMaxPlayerPositionsCount(game.getMaxPlayerPositionsCount());
                topOutOfBoardPositionsView.setMaxPlayerPositionsCount(game.getMaxPlayerPositionsCount());
                int player1Positions = this.game.getBoard().countPlayerPieces(Player.PLAYER_1);
                int player2Positions = this.game.getBoard().countPlayerPieces(Player.PLAYER_2);
                bottomOutOfBoardPositionsView.updatePositionsCount(player1Positions);
                topOutOfBoardPositionsView.updatePositionsCount(player2Positions);
            });

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
        updatePositionsCount();
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
            replay = new Replay(game, Calendar.getInstance().getTime(), isFreeMovementMode);
            replay.setMaxPositions(game.getMaxPlayerPositionsCount());
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
        if (isFreeMovementMode) {
            return;
        }
        String statusMessage = "Vez do " + Player.getName(turn);
        runOnUiThread(() -> {
            statusTextView.setText(statusMessage);
            statusTextView.announceForAccessibility(statusMessage);
            statusTextView.setTextColor(boardView.getPlayerColor(turn));
        });
    }

    private void showResult() {
        if (isFreeMovementMode) {
            return;
        }

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