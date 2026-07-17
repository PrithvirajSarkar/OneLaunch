package onelaunch.ui;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Main extends Application {

    Scene scene;
    @Override
    public void start(Stage stage) {

        VBox root = createHomeScreen();
        scene = new Scene(root, 900, 600);//900 is window width , 600 is window height
        //root is the vbox/ empty room/ layout .
        stage.setTitle("OneLaunch");
        stage.setScene(scene);
        stage.show();
    }


    private VBox createHomeScreen(){

        VBox root = new VBox();

        root.setAlignment(Pos.CENTER);
        //take all children inside you and place them in center
        root.setSpacing(20);
        //sets a spcing of 20 pixels between two objects
        root.setPadding(new Insets(20));//if different then Insets(20,12,34,33);
        //sets padding of 20 from all sides top right bottom left
        

        Label title = new Label("OneLaunch");//title is like furniture

        title.setFont(Font.font("Arial",FontWeight.BOLD,30));

        Button addWorkspaceButton = new Button("Add Workspace");

        addWorkspaceButton.setPrefWidth(250);
        addWorkspaceButton.setPrefHeight(60);

        addWorkspaceButton.setOnAction(event -> {
            showAddWorkspaceScreen();
        });

        root.getChildren().addAll(title, addWorkspaceButton);

        return root;
    }

    private void showAddWorkspaceScreen() {

    VBox root = createAddWorkspaceScreen();

    scene.setRoot(root);
    }

    private VBox createAddWorkspaceScreen() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        Label title = new Label("Add Workspace");

        title.setFont(Font.font("Arial",FontWeight.BOLD,30));

        Label workspaceNameLabel = new Label("Workspace Name");

        workspaceNameLabel.setFont(Font.font("Arial",FontWeight.SEMI_BOLD,16));

        TextField workspaceNameField = new TextField();

        workspaceNameField.setPrefWidth(380);

        workspaceNameField.setPromptText("Enter workspace Name");

        Label  errorLabel = new Label("Please enter a workspace name.");

        errorLabel.setManaged(false);
        errorLabel.setVisible(false);


        Button backButton = new Button("Back");

        backButton.setPrefWidth(120);
        backButton.setPrefHeight(45);

        backButton.setOnAction(event -> {
            showHomeScreen();
        });


        Button nextButton = new Button("Next");
        nextButton.setPrefWidth(120);
        nextButton.setPrefHeight(45);

        nextButton.setOnAction(event -> {

            String workspaceName = workspaceNameField.getText().trim();

            if (workspaceName.isEmpty()) {

                errorLabel.setManaged(true);
                errorLabel.setVisible(true);

            } else {

                errorLabel.setManaged(false);
                errorLabel.setVisible(false);

                showAddApplicationsScreen();

            }
        });
         root.getChildren().addAll(
                title,
                workspaceNameLabel,
                workspaceNameField,
                errorLabel,
                backButton,
                nextButton);
        return root;
    }

    private void showHomeScreen() {
        VBox root = createHomeScreen();
        scene.setRoot(root);
    }

    // Placeholder for the next screen (we'll build this next)
    private void showAddApplicationsScreen() {
        System.out.println("Add Applications Screen Coming Soon...");
    }
    public static void main(String[] args) {
        launch(args);
    }
}