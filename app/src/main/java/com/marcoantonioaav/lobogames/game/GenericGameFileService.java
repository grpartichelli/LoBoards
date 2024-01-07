package com.marcoantonioaav.lobogames.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import com.marcoantonioaav.lobogames.application.LoBoards;
import com.marcoantonioaav.lobogames.board.StandardBoard;
import com.marcoantonioaav.lobogames.exceptions.FailedToReadFileException;
import com.marcoantonioaav.lobogames.position.Connection;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GenericGameFileService {

    private GenericGameFileService() {
    }

    private static final double PADDING_PERCENTAGE = 0.0;

    public static List<GenericGame> createFromIntent(Intent intent) {
        Context context = LoBoards.getAppContext();
        try (InputStream stream = context.getContentResolver().openInputStream(intent.getData())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = reader.readLine();
            JSONObject object = new JSONObject(line);
            String boardName = object.getString("name");
            File dir = new File(context.getFilesDir(), "boards");

            if (!dir.exists()) {
                dir.mkdir();
            }

            File newFile = new File(dir, boardName.toLowerCase().replaceAll(" ", "-") + "-lobogames-config.txt");
            FileWriter writer = new FileWriter(newFile);
            writer.append(line);
            writer.flush();
            writer.close();

            return fromFilePath(newFile.getPath());
        } catch (Exception e) {
            throw new FailedToReadFileException();
        }
    }

    public static List<GenericGame> readAll() {
        List<GenericGame> games = new ArrayList<>();
        try {
            Context context = LoBoards.getAppContext();

            for (String file : context.getAssets().list("boards")) {
                games.addAll(fromAsset("boards/" + file));
            }

            for (File file : context.getFilesDir().listFiles()) {
                if (!file.getName().equals("boards")) {
                    continue;
                }

                for (File importedFilePaths : file.listFiles()) {
                    games.addAll(fromFilePath(importedFilePaths.getPath()));
                }
            }
        } catch (Exception e) {
            throw new FailedToReadFileException();
        }
        return games;
    }

    private static List<GenericGame> fromAsset(String filePath) {
        try (InputStream stream = LoBoards.getAppContext().getAssets().open(filePath)) {
            return processFileStream(stream);
        } catch (Exception e) {
            throw new FailedToReadFileException();
        }
    }

    private static List<GenericGame> fromFilePath(String filePath) {
        File myFile = new File(filePath);
        try (InputStream stream = new FileInputStream(myFile)) {
            return processFileStream(stream);
        } catch (Exception e) {
            throw new FailedToReadFileException();
        }
    }

    private static List<GenericGame> processFileStream(InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        JSONObject object = new JSONObject(line);

        double positionRadiusScale = readPositionRadiusScale(object);
        BitmapDrawable image = readImage(object);
        List<Position> positions = readPositions(object, image);
        List<Connection> connections = readConnections(positions);

        StandardBoard board = new StandardBoard(image, PADDING_PERCENTAGE, positionRadiusScale, positions, connections);
        List<GenericGame> games = readGames(object, board);
        return games;
    }

    private static double readPositionRadiusScale(JSONObject object) throws JSONException {
        return object.getDouble("positionRadiusScale");
    }

    private static String safeGetString(String value, JSONObject object) {
        try {
            return object.getString(value);
        } catch (JSONException e) {
            return "";
        }
    }

    private static BitmapDrawable readImage(JSONObject object) throws JSONException {
        String encodedImage = object.getString("imageUrl").split(",")[1];
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap imageBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return new BitmapDrawable(LoBoards.getAppContext().getResources(), imageBitMap);
    }

    private static List<GenericGame> readGames(JSONObject object, StandardBoard board) throws JSONException {
        JSONArray jsonArray = safeReadJsonArray("games", object);
        List<GenericGame> games = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject gameObject = jsonArray.getJSONObject(i);
            String name = safeGetString("name", gameObject);
            String videoUrl = safeGetString("videoUrl", gameObject);
            String textUrl = safeGetString("textUrl", gameObject);
            int maxPlayerPositionsCount = gameObject.getInt("maxPlayerPositionsCount");
            GameModule module = GameModule.parse(safeGetString("module", gameObject));
            GenericGame game = new GenericGame(board);
            game.setName(name);
            game.setVideoUrl(videoUrl);
            game.setTextUrl(textUrl);
            game.setMaxPlayerPositionsCount(maxPlayerPositionsCount);
            game.setModule(module);
            games.add(game);
        }

        GenericGame boardNamedGame = new GenericGame(board);
        boardNamedGame.setName(safeGetString("name", object));
        games.add(boardNamedGame);
        return games;
    }

    private static JSONArray safeReadJsonArray(String value, JSONObject object) {
        try {
            return object.getJSONArray(value);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    private static List<Position> readPositions(JSONObject object, BitmapDrawable image) throws JSONException {
        JSONArray jsonArray = object.getJSONArray("positions");
        List<Position> positions = new ArrayList<>();
        int width = image.getIntrinsicWidth();
        int height = image.getIntrinsicHeight();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject positionObject = jsonArray.getJSONObject(i);
            String id = positionObject.getString("id");

            JSONObject lengthPercentages = positionObject.getJSONObject("lengthPercentage");
            double heightPercentage = lengthPercentages.getDouble("height");
            double widthPercentage = lengthPercentages.getDouble("width");

            int x = (int) (width * widthPercentage);
            int y = (int) (height * heightPercentage);

            Coordinate coordinate = new Coordinate(x, y);
            positions.add(new Position(coordinate, id, i));
        }
        return positions;
    }

    private static List<Connection> readConnections(List<Position> positions) {
        List<Connection> connections = new ArrayList<>();
        for (Position position : positions) {
            for (Position otherPosition : positions) {
                if (position.equals(otherPosition)) {
                    continue;
                }
                connections.add(new Connection(position, otherPosition));
            }
        }
        return connections;
    }
}
