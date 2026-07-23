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

public class HomeScreen {

    private Main main;
    private StorageManager storageManager = new StorageManager();

    public HomeScreen(Main main) {
        this.main = main;
    }

    public VBox create() {

        VBox root = new VBox();

        root.setSpacing(20);
        root.setPadding(new Insets(25));

        Label title = new Label("OneLaunch");
        title.getStyleClass().add("title-label");

        Button addWorkspaceButton = new Button("+ New Workspace");
        addWorkspaceButton.getStyleClass().add("primary-button");

        addWorkspaceButton.setOnAction(event -> {
            main.showAddWorkspaceScreen();
        });

        Button menuButton = new Button("⋮");
        menuButton.getStyleClass().add("menu-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        header.getChildren().addAll(
                title,
                spacer,
                addWorkspaceButton,
                menuButton
        );

        VBox workspaceContainer = new VBox(15);

        ArrayList<Workspace> workspaces = storageManager.loadWorkspaces();

        for (Workspace workspace : workspaces) {

        workspaceContainer.getChildren().add(
            createWorkspaceCard(workspace)
        );
        }

        root.getChildren().addAll(
                header,
                workspaceContainer
        );

        return root;
    }

    private VBox createWorkspaceCard(Workspace workspace) {
        Label workspaceNameLabel = new Label(workspace.getName());
        workspaceNameLabel.getStyleClass().add("subtitle-label");
        
        Button launchButton = new Button("Launch");
        launchButton.getStyleClass().add("success-button");
        launchButton.setPrefWidth(100);
        launchButton.setOnAction(e -> {
            main.launchWorkspace(workspace);
        });

        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("secondary-button");
        editButton.setOnAction(e ->{
            main.showEditWorkspaceScreen(workspace);
        });
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
        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(
            launchButton,
            editButton,
            deleteButton
        );

        VBox workspaceCard = new VBox(10);
        workspaceCard.getStyleClass().add("workspace-card");

        workspaceCard.getChildren().addAll(
        workspaceNameLabel,
        buttonRow
        );

        return workspaceCard;
    }
}