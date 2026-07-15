package onelaunch.service;

import java.util.ArrayList;

import onelaunch.model.Application;
import onelaunch.model.Workspace;
import java.io.FileWriter;
import java.io.IOException;
public class StorageManager {

    private static final String FILE_PATH = "C:\\Users\\user\\OneDrive\\Desktop\\FILES\\OneLaunch\\data\\workspaces.json";

    public void saveWorkspaces(ArrayList<Workspace> workspaces) {
        String data = "";

for (Workspace work : workspaces) {

    String workspaceName = work.getName();

    ArrayList<Application> apps = work.getApplications();

    data += "Workspace: " + workspaceName + "\n";
    data += "Applications:\n";

    for (Application app : apps) {

        String applicationName = app.getName();
        String applicationPath = app.getPath();

        data += applicationName + "\n";
        data += applicationPath + "\n\n";

    }

    data += "\n";
}
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
        return new ArrayList<>();
    }
}