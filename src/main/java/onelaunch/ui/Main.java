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
import onelaunch.model.LaunchApplication;
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

    public void showAddApplicationsScreen(String workspaceName) {

        scene.setRoot(
                new AddApplicationsScreen(this, workspaceName).create()
        );
    }

    public void showEditWorkspaceScreen(Workspace workspace){
        scene.setRoot(new AddApplicationsScreen(this, workspace,true).create());
    }

    public void launchWorkspace(Workspace workspace) {
        for( LaunchApplication app : workspace.getApplications()) {
            try {
                ProcessBuilder builder = new ProcessBuilder(app.getPath());
                builder.start();
            } catch (IOException e) {
                
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Launch Error");
                alert.setHeaderText("OneLaunch couldn't launch an application.");
                alert.setContentText(
                "The application may have been moved, renamed, deleted, "
                + "or you may not have permission.\n\n"
                + "Continue launching the remaining applications?"
            );
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