package com.example.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameActivity extends AppCompatActivity {
    private BoardView boardView;
    private Button[][] buttons;
    private TextView gameName, result, info;
    private Button playAgain, back;

    private Game game;
    private int[][] board;
    private int player;
    private Agent agent;
    private int turn;
    private int selectedX = Movement.OUT_OF_BOARD;
    private int selectedY = Movement.OUT_OF_BOARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        game = MainActivity.games.get(this.getIntent().getExtras().get("gameName"));
        setTitle(game.getName());
        player = Agent.PLAYER_1;
        agent = new MinimaxAgent(Agent.PLAYER_2);

        boardView = findViewById(R.id.boardView);
        setUpButtons(game);
        gameName = findViewById(R.id.gameName);
        gameName.setText(game.getName());
        gameName.setTextColor(Color.BLACK);
        result = findViewById(R.id.result);
        info = findViewById(R.id.info);
        info.setTextColor(Color.WHITE);
        info.setText("Histórico");
        playAgain = findViewById(R.id.playAgain);
        playAgain.setOnClickListener(view -> resetGame());
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> openMainActivity());

        resetGame();
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
                    buttons[x][y].setOnClickListener(view -> makeGameStep(x, y));
                }
        }
        else {
            buttons = buttons5x5;
            for(final int x : new int[]{0, 1, 2, 3, 4})
                for(final int y : new int[]{0, 1, 2, 3, 4}) {
                    buttons[x][y].setVisibility(View.VISIBLE);
                    buttons[x][y].setOnClickListener(view -> makeGameStep(x, y));
                }
        }
    }

    private void makeGameStep(int x, int y) {
        if(player == turn && !game.isTerminalState(board)) {
            Move playerMove = null;
            if(!game.isInsertionGame(board)) {
                if(board[x][y] == player) {
                    selectPiece(x, y);
                    return;
                }
                else
                    playerMove = game.getPlayerMove(selectedX, selectedY, x, y, board, player);
            }
            else
                playerMove = game.getPlayerMove(Movement.OUT_OF_BOARD, Movement.OUT_OF_BOARD, x, y, board, player);
            if(game.isLegalMove(playerMove, board)) {
                makeMove(playerMove);
                if(!game.isTerminalState(board)) {
                    makeMove(agent.selectMove(game, board));
                    updateButtonsDescription();
                    if(game.isTerminalState(board))
                        endGame();
                }
                else
                    endGame();
            }
        }
    }

    private void selectPiece(int x, int y) {
        selectedX = x;
        selectedY = y;
        buttons[x][y].announceForAccessibility("Selecionado " + Movement.positionToString(x, y));
        boardView.drawBoard(board, selectedX, selectedY);
    }

    private void makeMove(Move move) {
        if(move == null)
            return;
        board = game.applyMove(move, board);
        turn = Agent.getOpponentOf(turn);
        boardView.announceForAccessibility(move.toString());
        boardView.drawBoard(board);
        info.setContentDescription(info.getContentDescription() + "" + move + "\n");
    }

    private void updateButtonsDescription() {
        for(int x = 0; x < Game.getBoardWidth(board); x++)
            for(int y = 0; y < Game.getBoardHeight(board); y++)
                buttons[x][y].setContentDescription(Movement.positionToString(x, y) + ": " + Agent.getPlayerName(board[x][y]));
    }

    private void endGame() {
        showResult();
        result.setVisibility(View.VISIBLE);
        playAgain.setVisibility(View.VISIBLE);
        //playAgain.requestFocus();
        //playAgain.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }

    private void resetGame() {
        board = game.getInitialBoard();
        updateButtonsDescription();
        turn = player;
        info.setContentDescription("Histórico da partida:\n");
        boardView.drawBoard(board);
        result.setVisibility(View.INVISIBLE);
        result.setText("");
        playAgain.setVisibility(View.INVISIBLE);
        buttons[0][0].requestFocus();
        buttons[0][0].sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }

    private void showResult() {
        int resultColor;
        String resultText;
        if(game.isVictory(board, player)) {
            resultText = "Você venceu!";
            resultColor = Color.BLUE;
        }
        else if(game.isVictory(board, agent.getPlayer())) {
            resultText = "Você perdeu";
            resultColor = Color.RED;
        }
        else {
            resultText = "Empate";
            resultColor = Color.GRAY;
        }
        result.announceForAccessibility(resultText);
        result.setText(resultText);
        result.setTextColor(resultColor);
    }

    private void openMainActivity() {
          startActivity(new Intent(this, MainActivity.class));
    }

}