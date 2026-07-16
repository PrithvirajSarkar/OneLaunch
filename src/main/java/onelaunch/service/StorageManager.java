package onelaunch.service;

import java.util.ArrayList;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import onelaunch.model.LaunchApplication;
import onelaunch.model.Workspace;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
public class StorageManager {

    private static final String FILE_PATH = "C:\\Users\\user\\OneDrive\\Desktop\\FILES\\OneLaunch\\data\\workspaces.json";

public void saveWorkspaces(ArrayList<Workspace> workspaces) {
    Gson gson = new Gson();
    
    String data = gson.toJson(workspaces);
// NOW ACTUALLY WRITE THE DATA TO THE FILE
    try {
        FileWriter writer = new FileWriter(FILE_PATH);

        writer.write(data);

        writer.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}

public ArrayList<Workspace> loadWorkspaces() {

    Gson gson = new Gson();

    try {

        FileReader reader = new FileReader(FILE_PATH);

        Type workspaceListType =
                new TypeToken<ArrayList<Workspace>>() {}.getType();

        ArrayList<Workspace> workspaces =
                gson.fromJson(reader, workspaceListType);

        reader.close();

        if (workspaces == null) {
            return new ArrayList<>();
        }

        return workspaces;

    } catch (IOException e) {

        e.printStackTrace();

        return new ArrayList<>();

    }
}
}