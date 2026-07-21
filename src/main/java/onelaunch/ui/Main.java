package onelaunch.ui;

import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import onelaunch.model.LaunchItem;
import onelaunch.model.Workspace;

public class Main extends Application {

    private Scene scene;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        VBox root = new HomeScreen(this).create();

        scene = new Scene(root, 900, 600);

        stage.setTitle("OneLaunch");
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
    return stage;
    }

    
    public void showHomeScreen() {

        scene.setRoot(
                new HomeScreen(this).create()
        );
    }

    public void showAddWorkspaceScreen() {

        scene.setRoot(
                new AddWorkspaceScreen(this).create()
        );
    }

    public void showAddItemsScreen(String workspaceName) {

        scene.setRoot(
                new AddItemsScreen(this, workspaceName).create()
        );
    }

    public void showEditWorkspaceScreen(Workspace workspace){
        System.out.println("Editing: " + workspace.getName());
        System.out.println(workspace.getItems());
        scene.setRoot(new AddItemsScreen(this, workspace,true).create());
    }

    public void launchWorkspace(Workspace workspace) {
        System.out.println("Launching: " + workspace.getName());
        System.out.println(workspace.getItems());
        if (workspace.getItems().isEmpty()) {
            // Show alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Empty Workspace");

            alert.setHeaderText("No items to launch.");

            alert.setContentText("This workspace doesn't contain any items.");
            alert.showAndWait();
            return;
        }
        for( LaunchItem item : workspace.getItems()) {
            try {
                ProcessBuilder builder = new ProcessBuilder(item.getPath());
                builder.start();
            } catch (IOException e) {
                
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Launch Error");
                alert.setHeaderText("Couldn't launch \"" + item.getName() + "\"");
                alert.setContentText("Continue launching the remaining items?");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.orElse(ButtonType.CANCEL) == ButtonType.CANCEL){
                return;
                }
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}