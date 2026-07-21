package onelaunch.ui;

import java.io.File;
import java.util.ArrayList;

import onelaunch.model.ItemType;
import onelaunch.model.LaunchItem;
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

public class AddItemsScreen {

    private Main main;
    private String workspaceName;
    private boolean isEditMode = false;
    private Workspace originalWorkspace;
    private ArrayList<LaunchItem> items = new ArrayList<>();
    private StorageManager storageManager = new StorageManager();
    private VBox itemsContainer;
    private boolean hasUnsavedChanges = false;


    public AddItemsScreen(Main main, String workspaceName) {
        this.main = main;
        this.workspaceName = workspaceName;
    }

    public AddItemsScreen(Main main, Workspace workspace, boolean isEditMode) {
    this.main = main;
    this.workspaceName = workspace.getName();
    this.items = new ArrayList<>(workspace.getItems());
    this.originalWorkspace = workspace;
    this.isEditMode = isEditMode;
    }

    public VBox create() {

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        // Title
        Label title = new Label("Add Items");
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

        // Items Heading
        HBox itemsHeader = new HBox();
        itemsHeader.setAlignment(Pos.CENTER_LEFT);

        Label itemsLabel = new Label("Items");
        Pane spacer2 = new Pane();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Button addItemButton = new Button("+ Add Item");
        //it will open file chooser 
        addItemButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(main.getStage());
            
            ItemType type = detectItemType(selectedFile);
                LaunchItem item = new LaunchItem(
                selectedFile.getName(),
                selectedFile.getAbsolutePath(),
                type
            );
            for (LaunchItem existingItem : items) {
                if (selectedFile.getAbsolutePath().equalsIgnoreCase(existingItem.getPath())) {

                Alert alert = new Alert(Alert.AlertType.WARNING);

                alert.setTitle("Duplicate Item");
                alert.setHeaderText("This Item has already been added.");
                alert.setContentText(
                    "This item already exists in this workspace."
                );

                alert.showAndWait();
                return;
            }
        }

            items.add(item);
            itemsContainer.getChildren().add(createItemRow(item));
            hasUnsavedChanges = true;
        });
        itemsHeader.getChildren().addAll(
        itemsLabel,
        spacer2,
        addItemButton
        );

        itemsContainer = new VBox();
        itemsContainer.setSpacing(10);

        //Load all existing items
        for (LaunchItem item : items) {

            itemsContainer.getChildren().add(
            createItemRow(item)
            );
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(250);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(itemsContainer);

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

        for (LaunchItem item : items) {

            workspace.addItem(item);
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
                itemsHeader,
                scrollPane,
                bottomButtons
        );

        return root;
    }

    private HBox createItemRow(LaunchItem item) {

        HBox itemRow = new HBox();
        itemRow.setSpacing(10);
        itemRow.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label("O");
        Label nameLabel = new Label(item.getName());

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteButton = new Button("X");
        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Delete Item");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText(
            "Are you sure you want to remove \"" +
            item.getName() +
            "\"?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK){
            items.remove(item);
            itemsContainer.getChildren().remove((itemRow));
            hasUnsavedChanges = true;
            }
        });

        
        itemRow.getChildren().addAll(
                iconLabel,
                nameLabel,
                spacer,
                deleteButton
        );

        return itemRow;
    }



    private ItemType detectItemType(File file){
        if (file.isDirectory()) {
        return ItemType.FOLDER;
    }

    else if (file.getAbsolutePath().toLowerCase().endsWith(".exe")) {
        return ItemType.APPLICATION;
    }

    else {
        return ItemType.FILE;
    } 
    }


}

//COMPLETED ALL FEATURES !!!!!!!!