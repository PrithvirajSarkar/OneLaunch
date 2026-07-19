package onelaunch.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AddWorkspaceScreen {

    private Main main;

    public AddWorkspaceScreen(Main main) {
        this.main = main;
    }

    public VBox create() {

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        Label title = new Label("Add Workspace");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        Label workspaceNameLabel = new Label("Workspace Name");
        workspaceNameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));

        TextField workspaceNameField = new TextField();
        workspaceNameField.setPrefWidth(380);
        workspaceNameField.setPromptText("Enter Workspace Name");

        Label errorLabel = new Label("Please enter a workspace name.");
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);

        Button backButton = new Button("Back");
        backButton.setPrefWidth(120);
        backButton.setPrefHeight(45);

        backButton.setOnAction(event -> {
            main.showHomeScreen();
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

                main.showAddApplicationsScreen(workspaceNameField.getText());

            }

        });

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        buttonBox.getChildren().addAll(
                backButton,
                nextButton
        );

        root.getChildren().addAll(
                title,
                workspaceNameLabel,
                workspaceNameField,
                errorLabel,
                buttonBox
        );

        return root;
    }
}