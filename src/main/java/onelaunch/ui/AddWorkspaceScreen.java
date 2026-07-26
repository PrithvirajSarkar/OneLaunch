package onelaunch.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.application.Platform;

public class AddWorkspaceScreen {

    private Main main;

    public AddWorkspaceScreen(Main main) {
        this.main = main;
    }

    public BorderPane create() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));

        Button backButton = new Button("← Back");
        backButton.getStyleClass().add("secondary-button");

        backButton.setOnAction(event -> {
            main.showHomeScreen();
        });

        HBox topBar = new HBox(backButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        root.setTop(topBar);

        Label title = new Label("New Workspace");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Give your workspace a meaningful name.");
        subtitle.getStyleClass().add("helper-text");

        VBox heading = new VBox(8);
        heading.setAlignment(Pos.CENTER);
        heading.getChildren().addAll(
            title,
            subtitle
        );
        

        Label workspaceNameLabel = new Label("Workspace Name");
        workspaceNameLabel.getStyleClass().add("form-label");

        HBox labelBox = new HBox(workspaceNameLabel);
        // Shift it right so it lines up with the text field
        labelBox.setPadding(new Insets(0, 0, 0, 36));

        TextField workspaceNameField = new TextField();
        workspaceNameField.setPrefWidth(430);
        workspaceNameField.setMaxWidth(430);
        workspaceNameField.setPrefHeight(42);
        HBox.setHgrow(workspaceNameField, Priority.ALWAYS);


        Label folderIcon = new Label("📁");
        folderIcon.setStyle("-fx-font-size:20px; -fx-text-fill:#6B7280;");

        HBox fieldBox = new HBox(12);

        fieldBox.setAlignment(Pos.CENTER_LEFT);

        fieldBox.getChildren().addAll(
                folderIcon,
                workspaceNameField
        );


        Label errorLabel = new Label("Please enter a workspace name.");
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);


        Label examplesTitle = new Label("Examples");
        examplesTitle.getStyleClass().add("form-text");

        Label examples = new Label("Coding • College • Gaming • Work");
        examples.getStyleClass().add("helper-text");

        VBox examplesBox = new VBox(4);
        examplesBox.getChildren().addAll(examplesTitle, examples);

    
        Button continueButton = new Button("Continue →");
        continueButton.getStyleClass().add("primary-button");
        continueButton.setPrefWidth(170);
        continueButton.setPrefHeight(44);

        continueButton.setOnAction(event -> {

            String workspaceName = workspaceNameField.getText().trim();

            if (workspaceName.isEmpty()) {

                errorLabel.setManaged(true);
                errorLabel.setVisible(true);

            } else {

                errorLabel.setManaged(false);
                errorLabel.setVisible(false);

                main.showAddItemsScreen(workspaceName);

            }

        });

        workspaceNameField.setOnAction(e -> continueButton.fire());

        HBox continueBox = new HBox(continueButton);
        continueBox.setAlignment(Pos.CENTER_RIGHT);
        continueBox.setPadding(new Insets(0, 15, 0, 0));
        continueBox.setMaxWidth(520);

        VBox content = new VBox(22);
        content.setMaxWidth(520);
        content.setPrefWidth(520);
        content.setAlignment(Pos.TOP_LEFT);


        content.getChildren().addAll(
            heading,
            labelBox,
            fieldBox,
            errorLabel,
            examplesBox,
            continueBox
        );
        
        VBox wrapper = new VBox(content);
        wrapper.setTranslateY(-10);
        wrapper.setAlignment(Pos.CENTER);

        root.setCenter(wrapper);

        Platform.runLater(workspaceNameField::requestFocus);

        return root;
    }
}