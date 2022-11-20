package com.example.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

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
    private int selectedX = Move.OUT_OF_BOARD;
    private int selectedY = Move.OUT_OF_BOARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();

        game = MainActivity.games.get(this.getIntent().getExtras().get("gameName"));
        setTitle(game.getName());
        player = Agent.PLAYER_1;
        agent = new ZeroDepthAgent(Agent.PLAYER_2);

        boardView = findViewById(R.id.boardView);
        buttons = new Button[][]{
                {findViewById(R.id.A1), findViewById(R.id.A2), findViewById(R.id.A3)},
                {findViewById(R.id.B1), findViewById(R.id.B2), findViewById(R.id.B3)},
                {findViewById(R.id.C1), findViewById(R.id.C2), findViewById(R.id.C3)}
        };
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

        for(final int x : new int[]{0, 1, 2})
            for(final int y : new int[]{0, 1, 2})
                buttons[x][y].setOnClickListener(view -> makeGameStep(x, y));

        resetGame();
    }

    private void makeGameStep(int x, int y) {
        if(player == turn && !game.isTerminalState(board)) {
            Move playerMove = null;
            if(game.isMovementGame(board)) {
                if(board[x][y] == player) {
                    selectPiece(x, y);
                    return;
                }
                else
                    playerMove = new Move(selectedX, selectedY, x, y, player);
            }
            else
               playerMove = new Move(x, y, player);
            if(game.isLegalMove(playerMove, board)) {
                makeMove(playerMove);
                if(!game.isTerminalState(board)) {
                    Move agentMove = agent.selectMove(game, board);
                    makeMove(agentMove);
                    buttons[x][y].announceForAccessibility(agentMove.toString());
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
        buttons[x][y].announceForAccessibility("Selecionado " + Move.positionToString(x, y));
        boardView.drawBoard(board, selectedX, selectedY);
    }

    private void makeMove(Move move) {
        if(move == null)
            return;
        board = game.applyMove(move, board);
        turn = Agent.getOpponentOf(turn);
        updateButtonsDescription();
        boardView.drawBoard(board);
        info.setContentDescription(info.getContentDescription() + "" + move + "\n");
    }

    private void updateButtonsDescription() {
        for(int x : new int[]{0, 1, 2})
            for(int y : new int[]{0, 1, 2}) {
                if (board[x][y] == 0)
                    buttons[x][y].setContentDescription(Move.positionToString(x, y) + " vazio");
                else
                    buttons[x][y].setContentDescription(Agent.getPlayerName(board[x][y]) + ": " + Move.positionToString(x, y));
            }
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