package onelaunch.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    public static void main(String[] args) {
        launch(args);
    }
}