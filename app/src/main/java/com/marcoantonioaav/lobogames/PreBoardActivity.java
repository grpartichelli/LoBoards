package com.marcoantonioaav.lobogames;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.marcoantonioaav.lobogames.board.Board;
import com.marcoantonioaav.lobogames.board.GenericGameBoardFactory;
import com.marcoantonioaav.lobogames.board.StandardBoard;
import com.marcoantonioaav.lobogames.exceptions.FailedToReadFileException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PreBoardActivity extends AppCompatActivity {
    private ListView listView;
    private Button play, importBoard;
    private String selectedBoardName;
    private static final int IMPORT_FILE_CODE = 32;

    public static final List<StandardBoard> BOARDS = GenericGameBoardFactory.createAll();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).hide();


        createBoardList();

        play = findViewById(R.id.play);
        play.setOnClickListener(view -> openGameActivity());
        play.setEnabled(false);

        importBoard = findViewById(R.id.importBoard);
        importBoard.setOnClickListener(view -> importFile());
    }

    private void createBoardList() {
        if (listView != null) {
            listView.setAdapter(null);
        }
        listView = findViewById(R.id.listView);
        listView.addHeaderView(new View(getBaseContext()), null, true);
        listView.addFooterView(new View(getBaseContext()), null, true);
        BoardListAdapter adapter = new BoardListAdapter(this, R.layout.board_list_item, BOARDS);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                (AdapterView<?> ad, View v, int position, long id) -> {
                    selectedBoardName = ((StandardBoard) listView.getItemAtPosition(position)).getName();
                    play.setEnabled(true);
                }
        );
    }

    private void importFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, IMPORT_FILE_CODE);
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.BOARD_NAME, selectedBoardName);
        intent.putExtra(GameActivity.IS_MULTIPLAYER, true);

        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_FILE_CODE) {
            try (InputStream stream = getContentResolver().openInputStream(data.getData())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = reader.readLine();
                JSONObject object = new JSONObject(line);
                String boardName = object.getString("name");
                File dir = new File(getFilesDir(), "boards");

                if(!dir.exists()){
                    dir.mkdir();
                }

                File newFile = new File(dir, boardName.toLowerCase().replaceAll(" ", "-") + "-lobogames-config.txt");
                FileWriter writer = new FileWriter(newFile);
                writer.append(line);
                writer.flush();
                writer.close();


                for(int i=0; i < BOARDS.size(); i++){
                    if(BOARDS.get(i).getName().equals(boardName)){
                        BOARDS.remove(i);
                        break;
                    }
                }

                BOARDS.add(0, GenericGameBoardFactory.fromFilePath(newFile.getPath()));
                selectedBoardName = boardName;
                createBoardList();
            } catch (Exception e) {
                throw new FailedToReadFileException();
            }
        }
    }

    static class BoardListAdapter extends ArrayAdapter<StandardBoard> {

        private int resourceLayout;
        private Context mContext;

        public BoardListAdapter(Context context, int resource, List<StandardBoard> boards) {
            super(context, resource, boards);
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

            Board board = getItem(position);

            if (board != null) {
                TextView textView = view.findViewById(R.id.boardListItemTextView);

                if (textView != null) {
                    textView.setText(board.getName());
                }


                ImageView imageView = view.findViewById(R.id.boardListItemImageView);

                if (imageView != null) {
                    imageView.setImageDrawable(board.getImage());
                }
            }

            return view;
        }

    }
}

