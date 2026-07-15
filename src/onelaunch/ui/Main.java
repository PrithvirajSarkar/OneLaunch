package onelaunch.ui;

import java.util.ArrayList;

import onelaunch.model.Workspace;
import onelaunch.service.StorageManager;

public class Main {

    public static void main(String[] args) {

        StorageManager storageManager = new StorageManager();

        ArrayList<Workspace> workspaces = storageManager.loadWorkspaces();

        System.out.println(workspaces);

    }
}