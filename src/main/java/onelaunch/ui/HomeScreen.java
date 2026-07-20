package onelaunch.ui;

import java.util.ArrayList;
import java.util.Optional;
import onelaunch.service.StorageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import onelaunch.model.Workspace;

public class HomeScreen {

    private Main main;
    private StorageManager storageManager = new StorageManager();

    public HomeScreen(Main main) {
        this.main = main;
    }

    public VBox create() {

        VBox root = new VBox();

        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        Label title = new Label("OneLaunch");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        Button addWorkspaceButton = new Button("Add Workspace");
        addWorkspaceButton.setPrefWidth(250);
        addWorkspaceButton.setPrefHeight(60);

        addWorkspaceButton.setOnAction(event -> {
            main.showAddWorkspaceScreen();
        });

        VBox workspaceContainer = new VBox(15);

        ArrayList<Workspace> workspaces = storageManager.loadWorkspaces();

        for (Workspace workspace : workspaces) {

        workspaceContainer.getChildren().add(
            createWorkspaceCard(workspace)
        );
        }

        root.getChildren().addAll(
                title,
                addWorkspaceButton,
                workspaceContainer
        );

        return root;
    }

    private VBox createWorkspaceCard(Workspace workspace) {
        Label workspaceNameLabel = new Label(workspace.getName());
        HBox buttonHBox = new HBox(10);
        Button launchButton = new Button("Launch");
        launchButton.setOnAction(e -> {
            main.launchWorkspace(workspace);
        });
        launchButton.setPrefWidth(100);
        Button editButton = new Button("Edit");
        editButton.setOnAction(e ->{
            main.showEditWorkspaceScreen(workspace);
        });
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Workspace");
                alert.setHeaderText("Confirm Deletion");
                alert.setContentText("Are you sure you want to delete \""+workspace.getName()+"\"?");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.isPresent() && result.get() == ButtonType.OK){
                    //delete workspace 
                    ArrayList<Workspace> workspaces = storageManager.loadWorkspaces();
                    for(int i = 0 ; i < workspaces.size(); i++){
                        if(workspaces.get(i).getName().equals(workspace.getName())){
                            workspaces.remove(i);
                            break;
                        }
                    }
                    storageManager.saveWorkspaces(workspaces);
                    main.showHomeScreen();
                }
        });
        
        buttonHBox.getChildren().addAll(
            launchButton,
            editButton,
            deleteButton
        );

        VBox workspaceCard = new VBox(10);

        workspaceCard.getChildren().addAll(
        workspaceNameLabel,
        buttonHBox
        );

        return workspaceCard;
    }
}