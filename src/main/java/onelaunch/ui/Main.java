package onelaunch.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import onelaunch.model.LaunchItem;
import onelaunch.model.Workspace;
import onelaunch.util.DialogUtil;

import java.awt.Desktop;

import java.net.URI;
import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.scene.Parent;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

import javafx.scene.image.Image;


public class Main extends Application {

    private Scene scene;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        VBox root = new HomeScreen(this).create();

        scene = new Scene(wrapInScrollPane(root),900,600);


        scene.getStylesheets().add(
            getClass().getResource("/style.css").toExternalForm()
        );

        Image appIcon = new Image(
            getClass().getResourceAsStream("/onelaunch-icon.png")
        );

        stage.getIcons().add(appIcon);

        stage.setTitle("OneLaunch");
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
    return stage;
    }


    private ScrollPane wrapInScrollPane(Parent content) {

    ScrollPane scrollPane = new ScrollPane(content);

    scrollPane.setFitToWidth(true);

    scrollPane.setFitToHeight(true);

    scrollPane.setPannable(true);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    return scrollPane;
    }

    
    public void showHomeScreen() {

        scene.setRoot(wrapInScrollPane(new HomeScreen(this).create()));
    }

    public void showAddWorkspaceScreen() {

        scene.setRoot(new AddWorkspaceScreen(this).create());
    }

    public void showAddItemsScreen(String workspaceName) {

        scene.setRoot(new AddItemsScreen(this, workspaceName).create());
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
            DialogUtil.showInfo(
                "Empty Workspace", 
                "No items to launch.", 
                "This workspace doesn't contain any items."
            );
            return;
        }
        for( LaunchItem item : workspace.getItems()) {

        try {
                
        switch (item.getType()) {

        case APPLICATION:
        new ProcessBuilder(item.getPath()).start();
        break;

        case FILE:
        Desktop.getDesktop().open(new File(item.getPath()));
        break;

        case FOLDER:
        Desktop.getDesktop().open(new File(item.getPath()));
        break;

        case WEBSITE:
        Desktop.getDesktop().browse(new URI(item.getPath()));
        break;
}
            } catch (Exception e) {
                
                boolean continueLaunching = DialogUtil.showConfirmation(
                    "Launch Error", 
                    "Couldn't launch \"" + item.getName() + "\"", 
                    "Continue launching the remaining items?", 
                    "Continue", 
                    "Stop", 
                    DialogUtil.ConfirmationStyle.WARNING
                );

                if(!continueLaunching){
                return;
                }
            }
        }
    }
    public void showAboutDialog() {

    Dialog<Void> dialog = new Dialog<>();

    dialog.setTitle("About OneLaunch");
    dialog.initOwner(stage);

    DialogPane dialogPane = dialog.getDialogPane();

    dialogPane.getStylesheets().add(
        getClass().getResource("/style.css").toExternalForm()
    );

    dialogPane.getStyleClass().add("about-dialog");

    // OneLaunch icon
    FontIcon appIcon = new FontIcon(FontAwesomeSolid.ROCKET);
    appIcon.setIconSize(32);
    appIcon.getStyleClass().add("about-app-icon");

    // App name
    Label appName = new Label("OneLaunch");
    appName.getStyleClass().add("about-app-name");

    // Tagline
    Label tagline = new Label("Launch your workspace with one click.");
    tagline.getStyleClass().add("about-tagline");

    VBox titleBox = new VBox(3, appName, tagline);

    HBox header = new HBox(14, appIcon, titleBox);
    header.setAlignment(Pos.CENTER_LEFT);

    // Version badge
    Label versionLabel = new Label("Version");
    versionLabel.getStyleClass().add("about-detail-label");

    Label versionValue = new Label("1.0");

    // Technology
    Label builtLabel = new Label("Built with");
    builtLabel.getStyleClass().add("about-detail");

    Label builtValue = new Label("Java & JavaFX");

    GridPane details = new GridPane();
    details.setHgap(35);
    details.setVgap(10);

    details.add(versionLabel, 0, 0);
    details.add(versionValue, 1, 0);
    details.add(builtLabel, 0, 1);
    details.add(builtValue, 1, 1);

    // Copyright
    Label copyright = new Label("© 2026 Prithviraj Sarkar");
    copyright.getStyleClass().add("about-copyright");

    VBox content = new VBox(
        22,
        header,
        details,
        copyright
    );

    content.getStyleClass().add("about-content");

    dialogPane.setContent(content);

    ButtonType closeButton = new ButtonType(
        "Close",
        ButtonBar.ButtonData.CANCEL_CLOSE
    );

    dialogPane.getButtonTypes().add(closeButton);

    Button close = (Button) dialogPane.lookupButton(closeButton);
    close.getStyleClass().add("dialog-primary-button");

    dialog.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }
}