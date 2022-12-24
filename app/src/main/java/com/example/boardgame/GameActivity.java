package com.example.boardgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.TextView;

import com.example.boardgame.game.Game;
import com.example.boardgame.move.Move;
import com.example.boardgame.move.Movement;
import com.example.boardgame.player.Human;
import com.example.boardgame.player.agent.MinimaxAgent;
import com.example.boardgame.player.Player;
import com.example.boardgame.ui.BoardView;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    private BoardView boardView;
    private Button[][] buttons;
    private TextView gameName, status;
    private Button playAgain, back;

    private Game game;
    private int[][] board;
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
        if((boolean) this.getIntent().getExtras().get(PreGameActivity.IS_MULTIPLAYER))
            player2 = new Human(Player.PLAYER_2);
        else
            player2 = new MinimaxAgent(Player.PLAYER_2, (String) this.getIntent().getExtras().get(PreGameActivity.DIFFICULTY));

        boardView = findViewById(R.id.boardView);
        boardView.resizeToScreenSize();
        boardView.setPlayer1Color(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_1_COLOR, Color.GREEN));
        boardView.setPlayer2Color(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.PLAYER_2_COLOR, Color.RED));
        boardView.setCursorColor(getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE).getInt(SettingsActivity.CURSOR_COLOR, Color.BLUE));
        setUpButtons(game);
        gameName = findViewById(R.id.gameName);
        gameName.setText(game.getName());
        gameName.setTextColor(Color.BLACK);
        status = findViewById(R.id.status);
        playAgain = findViewById(R.id.playAgain);
        playAgain.setOnClickListener(view -> resetGame());
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());

        resetGame();
        new Thread(GameActivity.this::gameLoop).start();
    }

    private void setUpButtons(Game game) {
        final Button[][] buttons3x3 = new Button[][]{
                {findViewById(R.id.A1_3x3), findViewById(R.id.A2_3x3), findViewById(R.id.A3_3x3)},
                {findViewById(R.id.B1_3x3), findViewById(R.id.B2_3x3), findViewById(R.id.B3_3x3)},
                {findViewById(R.id.C1_3x3), findViewById(R.id.C2_3x3), findViewById(R.id.C3_3x3)}
        };
        final Button[][] buttons5x5 = new Button[][]{
                {findViewById(R.id.A1_5x5), findViewById(R.id.A2_5x5), findViewById(R.id.A3_5x5), findViewById(R.id.A4_5x5), findViewById(R.id.A5_5x5)},
                {findViewById(R.id.B1_5x5), findViewById(R.id.B2_5x5), findViewById(R.id.B3_5x5), findViewById(R.id.B4_5x5), findViewById(R.id.B5_5x5)},
                {findViewById(R.id.C1_5x5), findViewById(R.id.C2_5x5), findViewById(R.id.C3_5x5), findViewById(R.id.C4_5x5), findViewById(R.id.C5_5x5)},
                {findViewById(R.id.D1_5x5), findViewById(R.id.D2_5x5), findViewById(R.id.D3_5x5), findViewById(R.id.D4_5x5), findViewById(R.id.D5_5x5)},
                {findViewById(R.id.E1_5x5), findViewById(R.id.E2_5x5), findViewById(R.id.E3_5x5), findViewById(R.id.E4_5x5), findViewById(R.id.E5_5x5)}
        };

        if(Game.getBoardWidth(game.getInitialBoard()) == 3) {
            buttons = buttons3x3;
            for(final int x : new int[]{0, 1, 2})
                for(final int y : new int[]{0, 1, 2}) {
                    buttons[x][y].setVisibility(View.VISIBLE);
                    buttons[x][y].setOnClickListener(view -> setCursorByClick(x, y));
                }
        }
        else {
            buttons = buttons5x5;
            for(final int x : new int[]{0, 1, 2, 3, 4})
                for(final int y : new int[]{0, 1, 2, 3, 4}) {
                    buttons[x][y].setVisibility(View.VISIBLE);
                    buttons[x][y].setOnClickListener(view -> setCursorByClick(x, y));
                }
        }
    }

    private void setCursorByClick(int x, int y) {
        if(isGameRunning) {
            Player player = Player.selectPlayerById(player1, player2, turn);
            if (player instanceof Human) {
                ((Human) player).setCursor(x, y);
                buttons[x][y].announceForAccessibility("Selecionado " + Movement.positionToString(x, y));
                boardView.drawBoard(board, x, y);
            }
        }
    }

    private void gameLoop() {
        while(true) {
            if(isGameRunning) {
                Player player = Player.selectPlayerById(player1, player2, turn);
                if (player.isReady()) {
                    Move move = player.getMove(game, board);
                    if (game.isLegalMove(move, board)) {
                        makeMove(move);
                        if(game.isTerminalState(board))
                            endGame();
                    }
                }
            }
        }
    }

    private void makeMove(Move move) {
        if(move == null)
            return;
        runOnUiThread(() -> boardView.announceForAccessibility(move.toString()));
        runOnUiThread(() -> boardView.drawBoard(board, move));
        board = Game.applyMove(move, board);
        turn = Player.getOpponentOf(turn);
        showTurn();
        Player currentPlayer = Player.selectPlayerById(player1, player2, turn);
        if(currentPlayer instanceof Human)
            runOnUiThread(this::updateButtonsDescription);
    }

    private void updateButtonsDescription() {
        for(int x = 0; x < Game.getBoardWidth(board); x++)
            for(int y = 0; y < Game.getBoardHeight(board); y++)
                buttons[x][y].setContentDescription(Movement.positionToString(x, y) + ": " + Player.getName(board[x][y]));
    }

    private void endGame() {
        isGameRunning = false;
        showResult();
        runOnUiThread(() -> {
            playAgain.setVisibility(View.VISIBLE);
            //playAgain.requestFocus();//
            //playAgain.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);//
        });
    }

    private void resetGame() {
        turn = Player.getRandomId();
        showTurn();
        board = game.getInitialBoard();
        updateButtonsDescription();
        boardView.drawBoard(board);
        playAgain.setVisibility(View.INVISIBLE);
        //buttons[0][Game.getBoardHeight(board)-1].requestFocus();
        //buttons[0][Game.getBoardHeight(board)-1].sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
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
        if(game.isVictory(board, player1.getId())) {
            resultText = Player.getName(Player.PLAYER_1) +  " venceu!";
            resultColor = boardView.getPlayerColor(Player.PLAYER_1);
        }
        else if(game.isVictory(board, player2.getId())) {
            resultText = Player.getName(Player.PLAYER_2) +  " venceu!";
            resultColor = boardView.getPlayerColor(Player.PLAYER_2);
        }
        else {
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