package onelaunch.service;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import onelaunch.model.Workspace;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.JsonSyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class StorageManager {

    private static final String FILE_PATH = "data/workspaces.json";

    public void saveWorkspaces(ArrayList<Workspace> workspaces) {

        Gson gson = new Gson();

        String data = gson.toJson(workspaces);

        File file = new File(FILE_PATH);

        // Make sure the data folder exists
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

        File file = new File(FILE_PATH);

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

        } catch(JsonSyntaxException e){
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

    private void createBackupOfCorruptedFile(File file) {

    File backupFile = new File(
        file.getParent(),
        "workspaces.json.backup"
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