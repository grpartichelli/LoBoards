package com.marcoantonioaav.lobogames.replay;

import android.content.Intent;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.exceptions.FailedToReadFileException;
import com.marcoantonioaav.lobogames.move.MatrixMove;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.move.StandardMove;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ReplayFileService {


    private static final String REPLAYS_DIRECTORY = "replays";

    private ReplayFileService() {
    }

    public static Replay createFromIntent(Intent intent) {
        return null;
    }

    public static void save(Replay replay) {
        try {

            List<String> lines = new ArrayList<>();
            JSONArray movesJsonArray = new JSONArray();

            for (Move move : replay.getMoves()) {
                JSONObject moveJson = new JSONObject();

                if (move instanceof StandardMove) {
                    moveJson.put("type", "standard");
                } else if (move instanceof MatrixMove) {
                    moveJson.put("type", "matrix");
                }

                JSONArray movementsJsonArray = new JSONArray();

                for (Movement movement : move.getMovements()) {
                    JSONObject movementJson = new JSONObject()
                            .put("startPositionId", movement.getStartPositionId())
                            .put("endPositionId", movement.getEndPositionId())
                            .put("playerId", movement.getPlayerId());
                    movementsJsonArray.put(movementJson);

                }
                moveJson.put("movements", movementsJsonArray);
                movesJsonArray.put(moveJson);
            }

            File dir = new File(LoBoGames.getAppContext().getFilesDir(), REPLAYS_DIRECTORY);

            if (!dir.exists()) {
                dir.mkdir();
            }

            File newFile = new File(dir, replay.getFileName());
            FileWriter writer = new FileWriter(newFile);
            writer.append(movesJsonArray.toString());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            throw new FailedToReadFileException();
        }
    }


    public static List<Replay> readAll() {
        return new ArrayList<>();
    }
}
