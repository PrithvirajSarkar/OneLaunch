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
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import onelaunch.util.DisplayNameUtil;

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
        root.setSpacing(26);
        root.setPadding(new Insets(20));

        // Title
        Label title = new Label("Add Items");
        title.getStyleClass().add("page-title");

        VBox content = new VBox(24);
        content.getStyleClass().add("form-card");

        content.setMaxWidth(780);
        content.setPrefWidth(780);
        content.setFillWidth(true);


        // Workspace header
        HBox workspaceBox = new HBox();
        workspaceBox.setAlignment(Pos.CENTER_LEFT);
        workspaceBox.setMaxWidth(Double.MAX_VALUE);


        Label workspaceHeading = new Label("Workspace");
        workspaceHeading.getStyleClass().add("section-heading");
        Label workspaceLabel = new Label(capitalize(workspaceName));
        workspaceLabel.getStyleClass().add("subtitle-label");

        HBox workspaceRow = new HBox();
        workspaceRow.setAlignment(Pos.CENTER_LEFT);
        workspaceRow.setSpacing(10);
        workspaceRow.setMaxWidth(Double.MAX_VALUE);

        Pane spacer1 = new Pane();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Button editButton = new Button("Rename");
        editButton.getStyleClass().add("rename-button");
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

        workspaceRow.getChildren().addAll(
        workspaceLabel,
        spacer1,
        editButton
        );

        VBox workspaceSection = new VBox(6);
        workspaceSection.getChildren().addAll(
        workspaceHeading,
        workspaceRow
        );

        workspaceBox.getChildren().add(workspaceSection);

        
        VBox.setMargin(workspaceBox, new Insets(8, 0, 0, 0));

        // Items Heading
        HBox itemsHeader = new HBox();
        itemsHeader.setAlignment(Pos.CENTER_LEFT);
        itemsHeader.setPadding(new Insets(0, 0, 6, 0));
        itemsHeader.setMaxWidth(Double.MAX_VALUE);


        Label itemsLabel = new Label("Items");
        itemsLabel.getStyleClass().add("subtitle-label");
        
        Pane spacer2 = new Pane();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Button addItemButton = new Button("+ Add Item");
        addItemButton.getStyleClass().add("add-item-button");
        //it will open file chooser 
        addItemButton.setOnAction(e -> {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Add Item");
    alert.setHeaderText("Choose what you want to add.");

    ButtonType browseButton = new ButtonType("Browse");
    ButtonType websiteButton = new ButtonType("Website");

    alert.getButtonTypes().setAll(
            browseButton,
            websiteButton,
            ButtonType.CANCEL
    );

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
        return;
    }

    LaunchItem item = null;

    // Browse
    if (result.get() == browseButton) {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(main.getStage());

        if (selectedFile == null) {
            return;
        }

        ItemType type = detectItemType(selectedFile);

        item = new LaunchItem(
                selectedFile.getName(),
                selectedFile.getAbsolutePath(),
                type
        );
    }

    // Website
    else if (result.get() == websiteButton) {

        TextInputDialog dialog = new TextInputDialog("https://");

        dialog.setTitle("Add Website");
        dialog.setHeaderText("Enter Website URL");
        dialog.setContentText("URL:");

        Optional<String> websiteResult = dialog.showAndWait();

        if (websiteResult.isEmpty()) {
            return;
        }

        String url = websiteResult.get().trim();

        if (url.isEmpty()) {
            return;
        }

        if (!url.startsWith("http://") &&
            !url.startsWith("https://")) {

            url = "https://" + url;
        }

        item = new LaunchItem(
                url,
                url,
                ItemType.WEBSITE
        );
    }

    // Duplicate Check
    for (LaunchItem existingItem : items) {

        if (existingItem.getPath().equalsIgnoreCase(item.getPath())) {

            Alert duplicateAlert = new Alert(Alert.AlertType.WARNING);

            duplicateAlert.setTitle("Duplicate Item");
            duplicateAlert.setHeaderText("This item has already been added.");
            duplicateAlert.setContentText(
                    "This item already exists in this workspace."
            );

            duplicateAlert.showAndWait();
            return;
        }
    }

    
    // Add Item
    items.add(item);
    refreshItems();
    hasUnsavedChanges = true;});


        itemsHeader.getChildren().addAll(
        itemsLabel,
        spacer2,
        addItemButton
        );

        itemsContainer = new VBox();
        itemsContainer.setSpacing(10);

        //Load all existing items
        refreshItems();


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("items-scroll-pane");
        scrollPane.setPrefHeight(320);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(itemsContainer);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        HBox bottomButtons = new HBox();
        bottomButtons.setAlignment(Pos.CENTER_LEFT);
        bottomButtons.setMaxWidth(Double.MAX_VALUE);


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
        saveWorkspaceButton.getStyleClass().add("primary-button");
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

        content.getChildren().addAll(
                workspaceBox,
                itemsHeader,
                scrollPane,
                bottomButtons
        );

        root.getChildren().addAll(
            title,
            content
        );

        Platform.runLater(() -> addItemButton.requestFocus());

        return root;
    }


    private void refreshItems() {

    itemsContainer.getChildren().clear();

    if(items.isEmpty()){

        itemsContainer.getChildren().add(
            createEmptyState()
        );

        return;
    }

    for(LaunchItem item : items){

        itemsContainer.getChildren().add(
            createItemRow(item)
        );
    }
    }


    private VBox createEmptyState() {

    Label title = new Label("No items added yet");
    title.getStyleClass().add("subtitle-label");

    Label subtitle = new Label(
            "Click \"+ Add Item\"\n"
          + "to add applications,\n"
          + "folders, files or websites."
    );

    subtitle.getStyleClass().add("preview-label");
    subtitle.setAlignment(Pos.CENTER);

    VBox box = new VBox(8);
    box.setAlignment(Pos.CENTER);
    box.setMinHeight(140);

    box.getChildren().addAll(
            title,
            subtitle
    );

    return box;
    }

    private VBox createItemRow(LaunchItem item) {

        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(12);
        row.setPadding(new Insets(12));

        Label iconLabel = new Label(getItemIcon(item));
        iconLabel.getStyleClass().add("item-icon");

        Label nameLabel = new Label(DisplayNameUtil.getDisplayName(item));
        nameLabel.getStyleClass().add("item-name");


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteButton = new Button("ⓧ");
        deleteButton.getStyleClass().add("delete-item-button");

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
            refreshItems();
            hasUnsavedChanges = true;
            }
        });
        
        row.getChildren().addAll(
                iconLabel,
                nameLabel,
                spacer,
                deleteButton
        );

        VBox itemBox = new VBox();
        itemBox.getStyleClass().add("item-row");
        itemBox.getChildren().add(row);

    return itemBox;
    }

    private String getItemIcon(LaunchItem item){
        switch (item.getType()) {

        case APPLICATION:
            return "🖥";

        case WEBSITE:
            return "🌐";

        case FOLDER:
            return "📁";

        case FILE:
            return "📄";

        default:
            return "❓";
        }
    }

    private ItemType detectItemType(File file){
        String path = file.getAbsolutePath().toLowerCase();

        if (file.isDirectory()) {
        return ItemType.FOLDER;
    }

    else if (path.endsWith(".exe") || path.endsWith(".lnk")) {
        return ItemType.APPLICATION;
    }

    else {
        return ItemType.FILE;
    } 
    }



    private String capitalize(String text) {

    if (text == null || text.isBlank()) {
        return text;
    }

    return text.substring(0, 1).toUpperCase()
            + text.substring(1);
}

}