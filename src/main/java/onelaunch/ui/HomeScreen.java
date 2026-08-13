package onelaunch.ui;

import java.util.ArrayList;
import onelaunch.service.StorageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import onelaunch.model.Workspace;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import onelaunch.model.LaunchItem;
import onelaunch.util.DisplayNameUtil;
import onelaunch.util.IconUtil;
import onelaunch.util.DialogUtil;
import onelaunch.util.DialogUtil.ConfirmationStyle;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

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

        root.getStyleClass().add("home-root");

        root.setSpacing(18);
        root.setPadding(new Insets(30));

        Label title = new Label("OneLaunch");
        title.getStyleClass().add("title-label");

        Button addWorkspaceButton = new Button("+ New Workspace");
        addWorkspaceButton.getStyleClass().add("primary-button");

        addWorkspaceButton.setOnAction(event -> {
            main.showAddWorkspaceScreen();
        });

        Button aboutButton = new Button();
        aboutButton.setGraphic(IconUtil.getInfoIcon(18));
        aboutButton.getStyleClass().add("menu-button");
        aboutButton.setOnAction(e -> {
            main.showAboutDialog();
        });

        Button themeButton = new Button();

        FontIcon themeIcon = new FontIcon(
            main.isDarkMode() ? FontAwesomeSolid.SUN : FontAwesomeSolid.MOON
        );

        themeIcon.setIconSize(18);

        themeButton.setGraphic(themeIcon);
        themeButton.getStyleClass().add("theme-button");

        themeButton.setOnAction(e -> {
            main.toggleDarkMode();

            themeIcon.setIconCode(
                main.isDarkMode() ? FontAwesomeSolid.SUN : FontAwesomeSolid.MOON
            );
        });

        Region spacer = new Region();
        spacer.setMinWidth(25);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);


        header.getChildren().addAll(
                title,
                spacer,
                addWorkspaceButton,
                themeButton,
                aboutButton
        );

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search workspaces...");
        searchField.getStyleClass().add("search-field");
        searchField.setMaxWidth(520);

        
        VBox workspaceContainer = new VBox(15);
        ArrayList<Workspace> workspaces = storageManager.loadWorkspaces();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            displayWorkspaces(
            workspaceContainer,
            workspaces,
            newValue
        );
    });

        displayWorkspaces(workspaceContainer, workspaces,"");

        root.getChildren().addAll(
                header,
                searchField,
                workspaceContainer
        );

        return root;
    }

    private void displayWorkspaces(VBox workspaceContainer,ArrayList<Workspace> workspaces, String searchText) {

        workspaceContainer.getChildren().clear();

        String search = searchText.toLowerCase();

        boolean displayedWorkspace = false;

        boolean hasPinned = false;

        boolean hasUnpinned = false;

        for (Workspace workspace : workspaces) {
            if(workspace.isPinned() && (search.isBlank() || workspace.getName().toLowerCase().contains(search))){
                hasPinned = true;
                break;
            }
        }

        for (Workspace workspace : workspaces) {
            if(!workspace.isPinned() && (search.isBlank() || workspace.getName().toLowerCase().contains(search))){
                hasUnpinned = true;
                break;
            }
        }

        if (hasPinned) {
            Label pinnedHeading = new Label("PINNED");
            pinnedHeading.getStyleClass().add("section-heading");

            workspaceContainer.getChildren().add(pinnedHeading);

            for (Workspace workspace : workspaces) {
                if(workspace.isPinned() && (search.isBlank() || workspace.getName().toLowerCase().contains(search))){
                    workspaceContainer.getChildren().add(createWorkspaceCard(workspace,workspaces));
                    displayedWorkspace = true;
                }
            }
        }
        if(hasUnpinned){
        Label workspaceHeading = new Label("WORKSPACES");
        workspaceHeading.getStyleClass().add("section-heading");

        workspaceContainer.getChildren().add(workspaceHeading);
        }

        for (Workspace workspace : workspaces) {
            if(!workspace.isPinned() && (search.isBlank() || workspace.getName().toLowerCase().contains(search))){
                workspaceContainer.getChildren().add(createWorkspaceCard(workspace,workspaces));
                displayedWorkspace = true;
            }
        }

        if (!displayedWorkspace) {
            if(workspaces.isEmpty()){
                workspaceContainer.getChildren().add(createWelcomeEmptyState());
            }
            else{
                workspaceContainer.getChildren().add(createSearchEmptyState());
            }
        }
    }

    private VBox createWelcomeEmptyState() {

    Label title = new Label("Welcome to OneLaunch");
    title.getStyleClass().add("subtitle-label");

    Label subtitle = new Label(
            "Create your first workspace to launch everything\n"
          + "                         with a single click.                "
    );
    subtitle.getStyleClass().add("preview-label");
    subtitle.setAlignment(Pos.CENTER);

    VBox box = new VBox(10);
    box.setAlignment(Pos.CENTER);
    box.setMinHeight(260);

    box.getChildren().addAll(
            title,
            subtitle
    );

    return box;
    }

    private VBox createSearchEmptyState() {

    Label title = new Label("No matching workspaces");
    title.getStyleClass().add("subtitle-label");

    Label subtitle = new Label("Try another search.");
    subtitle.getStyleClass().add("preview-label");

    VBox box = new VBox(10);
    box.setAlignment(Pos.CENTER);
    box.setMinHeight(260);

    box.getChildren().addAll(
            title,
            subtitle
    );

    return box;
    }


    private VBox createWorkspaceCard(Workspace workspace,ArrayList<Workspace> workspaces) {

        //Workspace Name
        Label workspaceNameLabel = new Label("📁 "+capitalize(workspace.getName()));
        workspaceNameLabel.getStyleClass().add("subtitle-label");

        //Preview
        Label previewLabel = new Label(getPreviewText(workspace));
        previewLabel.getStyleClass().add("preview-label");

        //Launch
        FontIcon launchIcon = new FontIcon(FontAwesomeSolid.ROCKET);
        launchIcon.setIconSize(14);

        Button launchButton = new Button("Launch");
        launchButton.setGraphic(launchIcon);
        launchButton.setGraphicTextGap(7);

        launchButton.setPrefHeight(38);
        launchButton.getStyleClass().add("success-button");

        launchButton.setOnAction(e -> {
            ScaleTransition buttonPress = new ScaleTransition(Duration.millis(100), launchButton);
            buttonPress.setToX(0.97);
            buttonPress.setToY(0.97);

            TranslateTransition rocketLaunch = new TranslateTransition(Duration.millis(100),launchIcon);
            rocketLaunch.setToX(5);
            rocketLaunch.setToY(-3);

            ParallelTransition launchAnimation = new ParallelTransition(buttonPress, rocketLaunch);
            launchAnimation.setAutoReverse(true);
            launchAnimation.setCycleCount(2);

            launchAnimation.setOnFinished(event -> {
            Platform.runLater(() -> main.launchWorkspace(workspace));
            });

            launchAnimation.play();
        });

        //Edit
        Button editButton = new Button("Edit");
        editButton.setPrefHeight(38);
        editButton.getStyleClass().add("secondary-button");
        editButton.setOnAction(e ->{
            main.showEditWorkspaceScreen(workspace);
        });

        //Delete
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefHeight(38);
        deleteButton.getStyleClass().add("danger-button");
        deleteButton.setOnAction(e ->{
            boolean confirmed = DialogUtil.showConfirmation(
                "Delete Workspace",
                "Delete \"" + workspace.getName() + "\"?",
                "This action cannot be undone.",
                "Delete", "Cancel", ConfirmationStyle.DANGER);
                if(confirmed){
                    //delete workspace 
                    
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
        MenuItem pinItem;
        if(workspace.isPinned()) {
            pinItem = new MenuItem("Unpin Workspace");
        }
        else {
            pinItem = new MenuItem("Pin Workspace");
        }
        pinItem.setOnAction(e -> {

        workspace.setPinned(!workspace.isPinned());

        storageManager.saveWorkspaces(workspaces);

        main.showHomeScreen();
        });


        MenuButton workspaceMenu = new MenuButton("⋮");
        workspaceMenu.setPrefWidth(36);
        workspaceMenu.getStyleClass().add("menu-button");
        workspaceMenu.getItems().add(pinItem);

        //Buttons Row
        HBox buttonRow = new HBox(10);

        Region spacer = new Region();
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

        preview.append(DisplayNameUtil.getDisplayName(items.get(i)));

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