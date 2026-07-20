package onelaunch.ui;

import java.io.File;
import java.util.ArrayList;
import onelaunch.model.LaunchApplication;
import onelaunch.model.Workspace;
import onelaunch.service.StorageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;

public class AddApplicationsScreen {

    private Main main;
    private String workspaceName;
    private boolean isEditMode = false;
    private Workspace originalWorkspace;
    private ArrayList<LaunchApplication> applications = new ArrayList<>();
    private StorageManager storageManager = new StorageManager();
    private VBox applicationsContainer;
    private boolean hasUnsavedChanges = false;


    public AddApplicationsScreen(Main main, String workspaceName) {
        this.main = main;
        this.workspaceName = workspaceName;
    }

    public AddApplicationsScreen(Main main, Workspace workspace, boolean isEditMode) {
    this.main = main;
    this.workspaceName = workspace.getName();
    this.applications = new ArrayList<>(workspace.getApplications());
    this.originalWorkspace = workspace;
    this.isEditMode = isEditMode;
    }

    public VBox create() {

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        // Title
        Label title = new Label("Add Applications");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Workspace header
        HBox workspaceBox = new HBox();
        workspaceBox.setAlignment(Pos.CENTER_LEFT);

        Label workspaceLabel = new Label(workspaceName);

        Pane spacer1 = new Pane();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(workspaceName);
            dialog.setTitle("Rename Workspace");
            dialog.setHeaderText("Workspace Name");
            dialog.setOnShown(e2 -> dialog.getEditor().selectAll());            
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                String newName = result.get().trim();
                if (!newName.isEmpty()) {
                    workspaceName = newName;
                    workspaceLabel.setText(newName);
                    hasUnsavedChanges = true;
                }
            }

        });

        workspaceBox.getChildren().addAll(
                workspaceLabel,
                spacer1,   
                editButton
        );

        // Applications Heading
        HBox applicationsHeader = new HBox();
        applicationsHeader.setAlignment(Pos.CENTER_LEFT);

        Label applicationsLabel = new Label("Applications");
        Pane spacer2 = new Pane();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Button addApplicationButton = new Button("+ Add Application");
        //it will open file chooser 
        addApplicationButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(main.getStage());
            
            if(selectedFile != null){
                LaunchApplication application = new LaunchApplication(
                selectedFile.getName(),
                selectedFile.getAbsolutePath()
            );
            applications.add(application);
            applicationsContainer.getChildren().add(createApplicationRow(application));
            hasUnsavedChanges = true;
            }
        });
        applicationsHeader.getChildren().addAll(
        applicationsLabel,
        spacer2,
        addApplicationButton
        );

        applicationsContainer = new VBox();
        applicationsContainer.setSpacing(10);

        //Load all existing applications
        for (LaunchApplication app : applications) {

            applicationsContainer.getChildren().add(
            createApplicationRow(app)
            );
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(250);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(applicationsContainer);

        HBox bottomButtons = new HBox();
        bottomButtons.setAlignment(Pos.CENTER_LEFT);


        Button backButton = new Button("← Back");
        //will check if any unsaved changes and then show previous screen
        backButton.setOnAction(e -> {
            if (hasUnsavedChanges) {
                // show confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Unsaved Changes");
                alert.setHeaderText("You have unsaved changes.");
                alert.setContentText("Do you want to discard them?");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.orElse(ButtonType.CANCEL)== ButtonType.OK){
                    main.showHomeScreen();
                }
                } else {
                    // go home immediately
                    main.showHomeScreen();
                    }

});

        Pane spacer3 = new Pane();
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        Button saveWorkspaceButton = new Button("Save Workspace");
        //saving the details

    saveWorkspaceButton.setOnAction(e -> {
        workspaceName = workspaceName.trim();

    if (workspaceName.isEmpty()) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Empty Workspace Name");
        alert.setHeaderText("Workspace name cannot be empty.");
        alert.setContentText("Please enter a workspace name.");
        alert.showAndWait();
        return;
    }

    ArrayList<Workspace> workspaces = storageManager.loadWorkspaces();

    for (Workspace existingWorkspace : workspaces) {

        if (isEditMode &&
                existingWorkspace.getName().equalsIgnoreCase(originalWorkspace.getName())) {
                continue;
        }

        if (existingWorkspace.getName().equalsIgnoreCase(workspaceName)) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Workspace");
            alert.setHeaderText("A workspace with this name already exists.");
            alert.setContentText("Please choose a different workspace name.");
            alert.showAndWait();
            return;
        }
    }
        Workspace workspace = new Workspace(workspaceName);

        for (LaunchApplication app : applications) {

            workspace.addApplication(app);
        }
        if(isEditMode){

            for (int i = 0; i < workspaces.size(); i++) {
                if(workspaces.get(i).getName().equalsIgnoreCase(originalWorkspace.getName())){
                    workspaces.set(i, workspace);
                    break;
                }
            }
            
            storageManager.saveWorkspaces(workspaces);
        }
        else{
            storageManager.saveWorkspace(workspace);
        }
        hasUnsavedChanges = false;
        main.showHomeScreen();
        });


        bottomButtons.getChildren().addAll(
        backButton,
        spacer3,
        saveWorkspaceButton
        );

        root.getChildren().addAll(
                title,
                workspaceBox,
                applicationsHeader,
                scrollPane,
                bottomButtons
        );

        return root;
    }

    private HBox createApplicationRow(LaunchApplication application) {

        HBox appRow = new HBox();
        appRow.setSpacing(10);
        appRow.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label("O");
        Label nameLabel = new Label(application.getName());

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteButton = new Button("X");
        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Delete Application");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText(
            "Are you sure you want to remove \"" +
            application.getName() +
            "\"?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK){
            applications.remove(application);
            applicationsContainer.getChildren().remove((appRow));
            hasUnsavedChanges = true;
            }
        });

        
        appRow.getChildren().addAll(
                iconLabel,
                nameLabel,
                spacer,
                deleteButton
        );

        return appRow;
    }

}

//COMPLETED ALL FEATURES !!!!!!!!