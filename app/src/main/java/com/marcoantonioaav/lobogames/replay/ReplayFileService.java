package com.marcoantonioaav.lobogames.replay;

import android.content.Context;
import android.content.Intent;
import android.text.style.TabStopSpan;
import android.util.Log;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.exceptions.FailedToReadFileException;
import com.marcoantonioaav.lobogames.move.MatrixMove;
import com.marcoantonioaav.lobogames.move.MatrixMovement;
import com.marcoantonioaav.lobogames.move.Move;
import com.marcoantonioaav.lobogames.move.Movement;
import com.marcoantonioaav.lobogames.move.StandardMove;
import com.marcoantonioaav.lobogames.move.StandardMovement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

            JSONObject replayJson = new JSONObject();
            replayJson.put("gameName", replay.getGameName());
            replayJson.put("boardName", replay.getBoardName());
            replayJson.put("date", replay.getDateString());

            JSONArray movesJsonArray = new JSONArray();

            for (Move move : replay.getMoves()) {
                JSONObject moveJson = new JSONObject();
                moveJson.put("playerId", move.getPlayerId());

                JSONArray movementsJsonArray = new JSONArray();

                for (Movement movement : move.getMovements()) {

                    if (movement instanceof StandardMovement) {
                        JSONObject movementJson = new JSONObject()
                                .put("type", "standard")
                                .put("startPositionId", movement.getStartPositionId())
                                .put("endPositionId", movement.getEndPositionId())
                                .put("playerId", movement.getPlayerId());
                        movementsJsonArray.put(movementJson);
                    }

                    if (movement instanceof MatrixMovement) {
                        MatrixMovement matrixMovement = (MatrixMovement) movement;
                        JSONObject movementJson = new JSONObject()
                                .put("type", "matrix")
                                .put("startX", matrixMovement.getStartX())
                                .put("startY", matrixMovement.getStartY())
                                .put("endX", matrixMovement.getEndX())
                                .put("endY", matrixMovement.getEndY())
                                .put("playerId", movement.getPlayerId());
                        movementsJsonArray.put(movementJson);
                    }
                }
                moveJson.put("movements", movementsJsonArray);
                movesJsonArray.put(moveJson);
            }

            replayJson.put("moves", movesJsonArray);

            File dir = new File(LoBoGames.getAppContext().getFilesDir(), REPLAYS_DIRECTORY);

            if (!dir.exists()) {
                dir.mkdir();
            }

            File newFile = new File(dir, replay.getFileName());
            FileWriter writer = new FileWriter(newFile);
            writer.append(replayJson.toString());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            throw new FailedToReadFileException();
        }
    }

    public static List<Replay> readAll() {
        List<Replay> replays = new ArrayList<>();
        try {
            Context context = LoBoGames.getAppContext();

            for (File file : context.getFilesDir().listFiles()) {
                if (!file.getName().equals("replays")) {
                    continue;
                }

                for (File importedFilePaths : file.listFiles()) {
                    replays.add(fromFilePath(importedFilePaths.getPath()));
                }
            }
            Collections.sort(replays, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            return replays;
        } catch (Exception e) {
            Log.e("ReplayFileService", e.getMessage());
            throw new FailedToReadFileException();
        }
    }

    private static Replay fromFilePath(String filePath) throws Exception {
        File myFile = new File(filePath);
        try (InputStream stream = new FileInputStream(myFile)) {
            return processFileStream(stream);
        }
    }

    private static Replay processFileStream(InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        JSONObject object = new JSONObject(line);

        Date date = getDate(object);
        String gameName = object.getString("gameName");
        String boardName = object.getString("boardName");
        Replay replay = new Replay(gameName, boardName, date);
        List<Move> moves = getMoves(object);
        replay.addAllMoves(moves);

        return replay;
    }

    private static Date getDate(JSONObject object) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.parse(object.getString("date"));
    }

    private static List<Move> getMoves(JSONObject object) throws Exception {
        JSONArray movesJsonArray = object.getJSONArray("moves");
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < movesJsonArray.length(); i++) {
            JSONObject moveJson = movesJsonArray.getJSONObject(i);
            JSONArray movementsJsonArray = moveJson.getJSONArray("movements");

            List<StandardMovement> standardMovements = new ArrayList<>();
            List<MatrixMovement> matrixMovements = new ArrayList<>();

            for (int j = 0; j < movementsJsonArray.length(); j++) {
                JSONObject movementJson = movementsJsonArray.getJSONObject(j);
                String type = movementJson.getString("type");
                if (type.equals("standard")) {
                    String startPositionId = movementJson.getString("startPositionId");
                    String endPositionId = movementJson.getString("endPositionId");
                    int movementPlayerId = movementJson.getInt("playerId");
                    standardMovements.add(new StandardMovement(startPositionId, endPositionId, movementPlayerId));
                } else if (type.equals("matrix")) {
                    int startX = movementJson.getInt("startX");
                    int startY = movementJson.getInt("startY");
                    int endX = movementJson.getInt("endX");
                    int endY = movementJson.getInt("endY");
                    int movementPlayerId = movementJson.getInt("playerId");
                    matrixMovements.add(new MatrixMovement(startX, startY, endX, endY, movementPlayerId));
                }
            }
            int movePlayerId = moveJson.getInt("playerId");

            if (!standardMovements.isEmpty()) {
                moves.add(new StandardMove(movePlayerId, standardMovements));
                continue;
            }

            if (!matrixMovements.isEmpty()) {
                moves.add(new MatrixMove(matrixMovements, movePlayerId));
            }
        }
        return moves;
    }
}
