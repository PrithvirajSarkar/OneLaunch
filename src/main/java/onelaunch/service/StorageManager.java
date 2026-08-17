package onelaunch.service;

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import onelaunch.model.Workspace;

public class StorageManager {

    private static final String APP_NAME = "OneLaunch";
    private static final String FILE_NAME = "workspaces.json";
    private static final String BACKUP_FILE_NAME = "workspaces.json.backup";

    public void saveWorkspaces(ArrayList<Workspace> workspaces) {

        Gson gson = new Gson();

        String data = gson.toJson(workspaces);

        File file = getWorkspaceFile();

        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        File tempFile = new File(
            file.getParent(),
            "workspaces.json.tmp"
        );

        try (FileWriter writer = new FileWriter(tempFile)) {

            // First write the complete data to the temporary file
            writer.write(data);

        } catch (IOException e) {

            System.out.println("Could not save workspaces.");
            e.printStackTrace();
            return;
        }

        try {

            // Replace the real file only after the temporary write succeeded
            Files.move(
                tempFile.toPath(),
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            System.out.println("Could not replace workspace file.");
            e.printStackTrace();

            // Clean up temporary file if replacement failed
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public void saveWorkspace(Workspace workspace) {

        ArrayList<Workspace> workspaces = loadWorkspaces();

        workspaces.add(workspace);

        saveWorkspaces(workspaces);
    }

    public ArrayList<Workspace> loadWorkspaces() {

        Gson gson = new Gson();

        File file = getWorkspaceFile();

        migrateOldWorkspaceFiles(file);

        // No file yet = no saved workspaces
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {

            Type workspaceListType =
                new TypeToken<ArrayList<Workspace>>() {}.getType();

            ArrayList<Workspace> workspaces =
                gson.fromJson(reader, workspaceListType);

            if (workspaces == null) {
                return new ArrayList<>();
            }

            return workspaces;

        } catch (JsonSyntaxException e) {

            System.out.println("Workspace data is corrupted.");

            createBackupOfCorruptedFile(file);

            return new ArrayList<>();

        } catch (IOException e) {

            System.out.println("Could not load workspaces.");

            return new ArrayList<>();

        } catch (Exception e) {

            System.out.println("Workspace data is invalid.");

            return new ArrayList<>();
        }
    }

    private File getWorkspaceFile() {

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

    private void migrateOldWorkspaceFiles(File newFile) {

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
                "Existing workspace data was migrated to the OneLaunch user data folder."
            );

            File oldBackupFile = new File(
                "data",
                BACKUP_FILE_NAME
            );

            if (oldBackupFile.exists()) {

                File newBackupFile = new File(
                    parentFolder,
                    BACKUP_FILE_NAME
                );

                Files.copy(
                    oldBackupFile.toPath(),
                    newBackupFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                );
            }

        } catch (IOException e) {

            System.out.println(
                "Could not migrate existing workspace data."
            );
        }
    }

    private void createBackupOfCorruptedFile(File file) {

        File backupFile = new File(
            file.getParent(),
            BACKUP_FILE_NAME
        );

        try {

            Files.copy(
                file.toPath(),
                backupFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                "A backup of the corrupted workspace file was created."
            );

        } catch (IOException e) {

            System.out.println(
                "Could not create backup of corrupted workspace file."
            );
        }
    }
}