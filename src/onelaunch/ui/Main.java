package onelaunch.ui;

import java.util.ArrayList;

import onelaunch.model.Application;
import onelaunch.model.Workspace;
import onelaunch.service.StorageManager;

public class Main {

    public static void main(String[] args) {

        ArrayList<Workspace> workspaces = new ArrayList<>();

        Workspace coding = new Workspace("Coding");

        workspaces.add(coding);

        Application chrome = new Application("Chrome","C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        
        coding.addApplication(chrome);
              
        Application vscode = new Application("VSCode","C:\\Program Files\\Google\\VSCode\\Application\\vscode.exe");

        coding.addApplication(vscode);

        Application spotify = new Application("Spotify","C:\\Program Files\\Google\\Spotify\\Application\\spotify.exe");

        coding.addApplication(spotify);

        Workspace gaming = new Workspace("Gaming");

        workspaces.add(gaming);

        Application steam = new Application("Steam","C:\\Program Files\\Google\\Steam\\Application\\steam.exe");
        
        gaming.addApplication(steam);
              
        Application discord = new Application("Discord","C:\\Program Files\\Google\\Discord\\Application\\discord.exe");

        gaming.addApplication(discord);

        StorageManager storageManager = new StorageManager();

        storageManager.saveWorkspaces(workspaces);

        System.out.println("Workspace saved successfully!");
    }
}