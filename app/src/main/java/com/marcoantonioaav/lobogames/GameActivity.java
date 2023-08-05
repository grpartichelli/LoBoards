package com.marcoantonioaav.lobogames;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.board.MatrixBoard;
import com.marcoantonioaav.lobogames.game.Game;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.player.Human;
import com.marcoantonioaav.lobogames.player.Player;
import com.marcoantonioaav.lobogames.player.agent.MinimaxAgent;
import com.marcoantonioaav.lobogames.position.Position;
import com.marcoantonioaav.lobogames.ui.BoardView;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    private BoardView boardView;
    private Button[][] buttons;
    private TextView gameName, status;
    private Button playAgain, back;

    private Game game;
    private int turn;
    private Player player1;
    private Player player2;
    private boolean isGameRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        game = PreGameActivity.GAMES.get(this.getIntent().getExtras().get(PreGameActivity.GAME_NAME));
        setTitle(game.getName());
        player1 = new Human(Player.PLAYER_1);
        if ((boolean) this.getIntent().getExtras().get(PreGameActivity.IS_MULTIPLAYER)) {
            player2 = new Human(Player.PLAYER_2);
        } else {
            player2 = new MinimaxAgent(Player.PLAYER_2, (String) this.getIntent().getExtras().get(PreGameActivity.DIFFICULTY));
        }

        // board
        boardView = findViewById(R.id.boardView);
        boardView.resizeToScreenSize();
        boardView.setPlayer1Color(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_1_COLOR, Color.GREEN));
        boardView.setPlayer2Color(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_2_COLOR, Color.RED));
        boardView.setCursorColor(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.CURSOR_COLOR, Color.BLUE));
        boardView.setOnClickListener(view -> setCursorByClick());

        // game name
        gameName = findViewById(R.id.gameName);
        gameName.setText(game.getName());

        // status
        status = findViewById(R.id.status);

        // play again
        playAgain = findViewById(R.id.playAgain);
        playAgain.setOnClickListener(view -> initializeGame());

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());

        initializeGame();
        new Thread(GameActivity.this::gameLoop).start();
    }

    private void setCursorByClick() {
        if (isGameRunning) {
            Player player = Player.selectPlayerById(player1, player2, turn);
            if (player instanceof Human) {
                ((Human) player).setCursor(this.boardView.getSelectedPosition());
                if (!player.isReady()) {
                    boardView.drawSelectedPosition();
                }
                // TODO: Acessibility
                // buttons[x][y].announceForAccessibility("Selecionado " + Movement.positionToString(x, y));
            }
        }
    }

    private void gameLoop() {
        while (true) {
            if (isGameRunning) {
                Player player = Player.selectPlayerById(player1, player2, turn);
                if (player.isReady()) {
                    Move move = player.getMove(game);
                    if (game.isLegalMove(move)) {
                        makeMove(move);
                        if (game.isTerminalState())
                            endGame();
                    }
                }
            }
        }
    }

    private void makeMove(Move move) {
        if (move == null) {
            return;
        }
        game.getBoard().applyMove(move);
        runOnUiThread(() -> boardView.announceForAccessibility(move.toString()));
        runOnUiThread(() -> boardView.drawMove(move));
        turn = Player.getOpponentOf(turn);
        showTurn();
        Player currentPlayer = Player.selectPlayerById(player1, player2, turn);
        if (currentPlayer instanceof Human) {
            // runOnUiThread(this::updateButtonsDescription);
        }
    }

    // TODO: Acessibility
//    private void updateButtonsDescription() {
//        for (int x = 0; x < this.game.getBoard().getWidth(); x++)
//            for (int y = 0; y < this.game.getBoard().getHeight(); y++)
//                buttons[x][y].setContentDescription(Movement.positionToString(x, y) + ": " + Player.getName(this.game.getBoard().valueAt(x, y)));
//    }

    private void endGame() {
        isGameRunning = false;
        showResult();
    }

    private void initializeGame() {
        turn = Player.getRandomId();
        showTurn();
        // updateButtonsDescription();
        game.restart();
        boardView.setBoard(game.getBoard());
        boardView.draw();
        isGameRunning = true;
    }

    private void showTurn() {
        String statusMessage = "Vez do " + Player.getName(turn);
        runOnUiThread(() -> {
            status.setText(statusMessage);
            status.announceForAccessibility(statusMessage);
            status.setTextColor(boardView.getPlayerColor(turn));
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
        runOnUiThread(() -> status.announceForAccessibility(resultText));
        SpannableString spanString = new SpannableString(resultText.toUpperCase());
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        runOnUiThread(() -> {
            status.setText(spanString);
            status.setTextColor(resultColor);
        });
    }
}