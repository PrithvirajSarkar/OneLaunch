package onelaunch.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsManager {

    private static final String FILE_PATH = "data/settings.json";

    public void saveDarkMode(boolean darkMode) {

        Gson gson = new Gson();

        JsonObject settings = new JsonObject();
        settings.addProperty("darkMode", darkMode);

        String data = gson.toJson(settings);

        try {

            FileWriter writer = new FileWriter(FILE_PATH);

            writer.write(data);

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public boolean loadDarkMode() {

        Gson gson = new Gson();

        try {

            FileReader reader = new FileReader(FILE_PATH);

            JsonObject settings = gson.fromJson(reader, JsonObject.class);

            reader.close();

            if (settings == null || !settings.has("darkMode")) {
                return false;
            }

            return settings.get("darkMode").getAsBoolean();

        } catch (IOException e) {

            return false;
        }
    }
}