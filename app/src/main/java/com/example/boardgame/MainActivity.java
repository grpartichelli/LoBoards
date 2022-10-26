package com.example.boardgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer speechRecognizer;
    private Intent intent;

    private ToggleButton toggleButton;
    private Button[][] buttons;
    private TextView info;

    private Game game;
    private int[][] board;
    private int player;
    private Agent agent;
    private int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        game = new TicTacToe();
        board = game.getInitialBoard();
        player = Agent.PLAYER_1;
        agent = new RandomAgent(Agent.PLAYER_2);
        turn = player;

        toggleButton = findViewById(R.id.toggleButton);
        buttons = new Button[][]{
                {findViewById(R.id.A1), findViewById(R.id.B1), findViewById(R.id.C1)},
                {findViewById(R.id.A2), findViewById(R.id.B2), findViewById(R.id.C2)},
                {findViewById(R.id.A3), findViewById(R.id.B3), findViewById(R.id.C3)},
        };
        info = findViewById(R.id.info);

        for(final int x : new int[]{0, 1, 2})
            for(final int y : new int[]{0, 1, 2})
                buttons[x][y].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeGameStep(x, y);
                    }
                });

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) { }

            @Override
            public void onBeginningOfSpeech() { }

            @Override
            public void onRmsChanged(float v) { }

            @Override
            public void onBufferReceived(byte[] bytes) { }

            @Override
            public void onEndOfSpeech() { }

            @Override
            public void onError(int i) { }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for(String match : matches)
                    for(int x=0; x < 3; x++)
                        for(int y=0; y < 3; y++)
                            if(match.contains(Move.positionToString(x, y)))
                                makeGameStep(x, y);
            }

            @Override
            public void onPartialResults(Bundle bundle) { }

            @Override
            public void onEvent(int i, Bundle bundle) { }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    speechRecognizer.startListening(intent);
                else
                    speechRecognizer.stopListening();
            }
        });
    }

    private void makeGameStep(int x, int y) {
        Move playerMove = new Move(x, y, player);
        if(player == turn && !game.isTerminalState(board) && game.isLegalMove(playerMove, board)) {
            board = game.applyMove(playerMove, board);
            turn = Agent.getOpponentOf(turn);
            buttons[x][y].setText(playerMove.toString());
            info.setText(info.getText() + "" + playerMove + "\n");
            if(!game.isTerminalState(board)) {
                Move agentMove = agent.selectMove(game, board);
                board = game.applyMove(agentMove, board);
                turn = Agent.getOpponentOf(turn);
                buttons[agentMove.x][agentMove.y].setText(agentMove.toString());
                info.setText(info.getText() + "" + agentMove + "\n");
            }
        }
    }

}