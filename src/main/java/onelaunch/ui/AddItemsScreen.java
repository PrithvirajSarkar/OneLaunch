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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.util.Optional;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.TextInputDialog;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import onelaunch.util.DisplayNameUtil;
import onelaunch.util.IconUtil;
import javafx.scene.Node;
import onelaunch.util.DialogUtil;
import onelaunch.util.DialogUtil.ConfirmationStyle;


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

            DialogUtil.setDialogIcon(dialog);
            DialogUtil.applyDialogTheme(dialog.getDialogPane());

            Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

            okButton.getStyleClass().add("dialog-primary-button");
            cancelButton.getStyleClass().add("dialog-secondary-button");

            dialog.setOnShown(e2 -> {
                dialog.getEditor().selectAll();
            });

            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                String newName = result.get().trim();
                if (newName.isEmpty()) {
                    DialogUtil.showWarning(
                        "Invalid Workspace Name", 
                        "Workspace name cannot be empty.", 
                        "Please enter a workspace name."
                    );
                    return;
                }
                workspaceName = newName;
                workspaceLabel.setText(newName);
                hasUnsavedChanges = true;
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
            DialogUtil.ItemChoice choice = DialogUtil.showItemChoice();
            if (choice == DialogUtil.ItemChoice.CANCEL) {
                return;
            }

    LaunchItem item = null;

    // Browse
    if (choice == DialogUtil.ItemChoice.BROWSE) {

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
    else if (choice == DialogUtil.ItemChoice.WEBSITE) {

        Optional<String> websiteResult = DialogUtil.showWebsiteInput();

        if (websiteResult.isEmpty()) {
            return;
        }

        String url = websiteResult.get().trim();

        if (url.isEmpty()) {
            return;
        }

        if(!url.contains(".") && !url.equalsIgnoreCase("localhost")) {
            DialogUtil.showWarning(
                "Invalid Website",
                "Please enter a valid website URL.",
                "Examples:\n• youtube.com\n• https://github.com"
            );

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
            DialogUtil.showWarning(
                "Duplicate Item", 
                "This item has already been added.", 
                "This item already exists in this workspace."
            );
            return;
        }
    }

    
    // Add Item
    items.add(item);
    refreshItems();
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
            if (!hasUnsavedChanges) {
                main.showHomeScreen();
                return;
            }
            boolean discard = DialogUtil.showConfirmation(
                "Unsaved Changes",
                "You have unsaved changes.",
                "Discard your changes and return to the home screen?",
                "Discard",
                "Keep Editing",
                ConfirmationStyle.WARNING
            );

            if(discard){
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

        DialogUtil.showWarning(
            "Empty Workspace Name", 
            "Workspace name cannot be empty.", 
            "Please enter a workspace name."
        );
        return;
    }

    ArrayList<Workspace> workspaces = storageManager.loadWorkspaces();

    for (Workspace existingWorkspace : workspaces) {

        if (isEditMode &&
                existingWorkspace.getName().equalsIgnoreCase(originalWorkspace.getName())) {
                continue;
        }

        if (existingWorkspace.getName().equalsIgnoreCase(workspaceName)) {

            DialogUtil.showWarning(
                "Duplicate Workspace",
                "A workspace with this name already exists.",
                "Please choose a different workspace name."
            );
            return;
        }
    }
        Workspace workspace = new Workspace(workspaceName);

        // Preserve the existing pin state when editing a workspace.
        if (isEditMode && originalWorkspace != null) {
            workspace.setPinned(originalWorkspace.isPinned());
        }

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
          + "files or websites."
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

        Node icon = IconUtil.getIcon(item.getType(), 18);
        icon.getStyleClass().add("item-icon");

        Label nameLabel = new Label(DisplayNameUtil.getDisplayName(item));
        nameLabel.getStyleClass().add("item-name");


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button editButton = new Button();

        FontIcon editIcon = new FontIcon(FontAwesomeSolid.PEN);
        editIcon.setIconSize(14);

        editButton.setGraphic(editIcon);
        editButton.getStyleClass().add("edit-item-button");

        editButton.setOnAction(e -> {
            editItem(item);
        });
        Button deleteButton = new Button();
        FontIcon deleteIcon = new FontIcon(FontAwesomeSolid.TIMES);
        deleteIcon.setIconSize(14);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().add("delete-item-button");

        deleteButton.setOnAction(e -> {
            boolean confirmed = DialogUtil.showConfirmation(
                "Remove Item",
                "Remove \"" + DisplayNameUtil.getDisplayName(item) + "\"?",
                "This item will be removed from the workspace.",
                "Remove",
                "Cancel",
                ConfirmationStyle.DANGER);
            if(confirmed){
            items.remove(item);
            refreshItems();
            hasUnsavedChanges = true;
            }
        });
        
        row.getChildren().addAll(
                icon,
                nameLabel,
                spacer,
                editButton,
                deleteButton
        );

        VBox itemBox = new VBox();
        itemBox.getStyleClass().add("item-row");
        itemBox.getChildren().add(row);

    return itemBox;
    }

    private void editItem(LaunchItem originalItem){
        if(originalItem.getType() == ItemType.WEBSITE){
            Optional<String> result = DialogUtil.showWebsiteInput();

            if(result.isEmpty()){
                return;
            }

            String url = result.get().trim();

            if(url.isEmpty()){
                return;
            }

            if (!url.contains(".") &&
            !url.equalsIgnoreCase("localhost")){
                DialogUtil.showWarning(
                "Invalid Website",
                "Please enter a valid website URL.",
                "Examples:\n• youtube.com\n• https://github.com"
            );

            return;
            }

            if(!url.startsWith("http://") && !url.startsWith("https://")){
                url = "https://" + url;
            } 

        // Check for duplicate URL
        for (LaunchItem existingItem : items) {

            if (existingItem != originalItem &&
                existingItem.getPath().equalsIgnoreCase(url)) {

                DialogUtil.showWarning(
                    "Duplicate Item",
                    "This item has already been added.",
                    "This item already exists in this workspace."
                );

                return;
                }
            }

            int index = items.indexOf(originalItem);

            LaunchItem editedItem = new LaunchItem(url, url, ItemType.WEBSITE);

            items.set(index, editedItem);

            refreshItems();;
            hasUnsavedChanges = true;

            return;
        }



    // FILE / APPLICATION
    FileChooser fileChooser = new FileChooser();

    File selectedFile =
        fileChooser.showOpenDialog(main.getStage());

    if (selectedFile == null) {
        return;
    }

    ItemType type = detectItemType(selectedFile);

    String path = selectedFile.getAbsolutePath();


    // Check for duplicate path
    for (LaunchItem existingItem : items) {

        if (existingItem != originalItem &&
            existingItem.getPath().equalsIgnoreCase(path)) {

            DialogUtil.showWarning(
                "Duplicate Item",
                "This item has already been added.",
                "This item already exists in this workspace."
            );

            return;
        }
        int index = items.indexOf(originalItem);
        LaunchItem editedItem = new LaunchItem(
            selectedFile.getName(),
            path,
            type
        );      
        items.set(index, editedItem);
        
        refreshItems();
        hasUnsavedChanges = true;
        }
    }





    //DETECTING TYPE OF SELECTION 
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