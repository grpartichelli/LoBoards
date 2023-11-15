package com.marcoantonioaav.lobogames.board;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.board.StandardBoard;
import com.marcoantonioaav.lobogames.exceptions.FailedToReadFileException;
import com.marcoantonioaav.lobogames.game.GenericGame;
import com.marcoantonioaav.lobogames.position.Connection;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GenericGameBoardFactory {

    private GenericGameBoardFactory() {}

    private static final double PADDING_PERCENTAGE = 0.0;

    public static List<StandardBoard> createAll() {
        List<StandardBoard> boards = new ArrayList<>();
        try {
            String[] files = LoBoGames.getAppContext().getAssets().list("boards");
            for (String file: files) {
                boards.add(fromConfigFile("boards/" + file));
            }
        } catch (Exception e) {
            throw new FailedToReadFileException();
        }
        return boards;
    }

    private static StandardBoard fromConfigFile(String filePath) {

        try (InputStream stream = LoBoGames.getAppContext().getAssets().open(filePath)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = reader.readLine();
            JSONObject object = new JSONObject(line);

            double positionRadiusScale = readPositionRadiusScale(object);
            String name = readName(object);
            BitmapDrawable image = readImage(object);
            List<Position> positions = readPositions(object, image);
            List<Connection> connections = readConnections(positions);

            StandardBoard board = new StandardBoard(image, PADDING_PERCENTAGE, positionRadiusScale, positions, connections);
            board.setName(name);
            return board;
        } catch (Exception e) {
            throw new FailedToReadFileException();
        }
    }

    private static double readPositionRadiusScale(JSONObject object) throws JSONException {
        return object.getDouble("positionRadiusScale");
    }

    private static String readName(JSONObject object) throws JSONException {
        return object.getString("name");
    }

    private static BitmapDrawable readImage(JSONObject object) throws JSONException {
        String encodedImage = object.getString("imageUrl").split(",")[1];
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap imageBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return new BitmapDrawable(LoBoGames.getAppContext().getResources(), imageBitMap);
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
        for (Position position: positions) {
            for (Position otherPosition: positions) {
                if (position.equals(otherPosition)) {
                    continue;
                }
                connections.add(new Connection(position, otherPosition));
            }
        }
        return connections;
    }
}
