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
import onelaunch.model.Workspace;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import onelaunch.model.LaunchItem;

public class HomeScreen {

    private Main main;
    private StorageManager storageManager = new StorageManager();

    public HomeScreen(Main main) {
        this.main = main;
    }

    private String capitalize(String text){

    if(text == null || text.isBlank()){
        return text;
    }

    return text.substring(0,1).toUpperCase()
            + text.substring(1);
    }

    public VBox create() {

        VBox root = new VBox();

        root.setSpacing(20);
        root.setPadding(new Insets(30));

        Label title = new Label("OneLaunch");
        title.getStyleClass().add("title-label");

        Button addWorkspaceButton = new Button("+ New Workspace");
        addWorkspaceButton.getStyleClass().add("primary-button");

        addWorkspaceButton.setOnAction(event -> {
            main.showAddWorkspaceScreen();
        });

        Button settingsButton = new Button("⚙");
        settingsButton.getStyleClass().add("menu-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        header.getChildren().addAll(
                title,
                spacer,
                addWorkspaceButton,
                settingsButton
        );

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search workspaces...");
        searchField.getStyleClass().add("search-field");
        searchField.setMaxWidth(480);

        Label workspaceHeading = new Label("WORKSPACES");
        workspaceHeading.getStyleClass().add("section-heading");

        VBox workspaceContainer = new VBox(15);

        ArrayList<Workspace> workspaces = storageManager.loadWorkspaces();

        for (Workspace workspace : workspaces) {

        workspaceContainer.getChildren().add(
            createWorkspaceCard(workspace)
        );
        }

        root.getChildren().addAll(
                header,
                searchField,
                workspaceHeading,
                workspaceContainer
        );

        return root;
    }

    private VBox createWorkspaceCard(Workspace workspace) {

        //Workspace Name
        Label workspaceNameLabel = new Label("💻 " + capitalize(workspace.getName()));
        workspaceNameLabel.getStyleClass().add("subtitle-label");

        //Preview
        Label previewLabel = new Label(getPreviewText(workspace));
        previewLabel.getStyleClass().add("preview-label");

        //Launch
        Button launchButton = new Button("▶ Launch");
        launchButton.getStyleClass().add("success-button");
        launchButton.setOnAction(e -> {
            main.launchWorkspace(workspace);
        });

        //Edit
        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("secondary-button");
        editButton.setOnAction(e ->{
            main.showEditWorkspaceScreen(workspace);
        });

        //Delete
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("danger-button");
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

        //Menu
        MenuItem pinItem = new MenuItem("📌 Pin Workspace");
        MenuButton workspaceMenu = new MenuButton("⋮");
        workspaceMenu.getItems().add(pinItem);

        //Buttons Row
        HBox buttonRow = new HBox(10);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonRow.setAlignment(Pos.CENTER_LEFT);

        buttonRow.getChildren().addAll(
            spacer,
            launchButton,
            editButton,
            deleteButton,
            workspaceMenu
        );

        VBox workspaceCard = new VBox(6);
        workspaceCard.getStyleClass().add("workspace-card");

        workspaceCard.getChildren().addAll(
        workspaceNameLabel,
        previewLabel,
        buttonRow
        );

        return workspaceCard;
    }

    private String getPreviewText(Workspace workspace) {

    ArrayList<LaunchItem> items = workspace.getItems();

    if (items.isEmpty()) {
        return "No items added";
    }

    StringBuilder preview = new StringBuilder();

    int limit = Math.min(3, items.size());

    for (int i = 0; i < limit; i++) {

        preview.append(items.get(i).getName());

        if (i < limit - 1) {
            preview.append(" • ");
        }
    }

    if (items.size() > 3) {

        preview.append(" • +")
               .append(items.size() - 3)
               .append(" more");
    }

    return preview.toString();
    }
}