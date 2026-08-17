package onelaunch.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SettingsManager {

    private static final String APP_NAME = "OneLaunch";
    private static final String FILE_NAME = "settings.json";

    public void saveDarkMode(boolean darkMode) {

        Gson gson = new Gson();

        JsonObject settings = new JsonObject();
        settings.addProperty("darkMode", darkMode);

        String data = gson.toJson(settings);

        File file = getSettingsFile();

        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(data);

        } catch (IOException e) {

            System.out.println("Could not save settings.");
            e.printStackTrace();
        }
    }

    public boolean loadDarkMode() {

        Gson gson = new Gson();

        File file = getSettingsFile();

        migrateOldSettingsFile(file);

        if (!file.exists()) {
            return false;
        }

        try (FileReader reader = new FileReader(file)) {

            JsonObject settings = gson.fromJson(reader, JsonObject.class);

            if (settings == null || !settings.has("darkMode")) {
                return false;
            }

            return settings.get("darkMode").getAsBoolean();

        } catch (IOException e) {

            return false;

        } catch (Exception e) {

            System.out.println("Settings data is invalid.");
            return false;
        }
    }

    private File getSettingsFile() {

        return new File(
            getDataDirectory(),
            FILE_NAME
        );
    }

    private File getDataDirectory() {

        String localAppData = System.getenv("LOCALAPPDATA");

        if (localAppData != null && !localAppData.isBlank()) {

            return new File(
                localAppData,
                APP_NAME
            );
        }

        // Fallback for environments where LOCALAPPDATA is unavailable
        return new File(
            System.getProperty("user.home"),
            "AppData" + File.separator + "Local" + File.separator + APP_NAME
        );
    }

    private void migrateOldSettingsFile(File newFile) {

        if (newFile.exists()) {
            return;
        }

        File oldFile = new File(
            "data",
            FILE_NAME
        );

        if (!oldFile.exists()) {
            return;
        }

        File parentFolder = newFile.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try {

            Files.copy(
                oldFile.toPath(),
                newFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                "Existing settings were migrated to the OneLaunch user data folder."
            );

        } catch (IOException e) {

            System.out.println(
                "Could not migrate existing settings."
            );
        }
    }
}