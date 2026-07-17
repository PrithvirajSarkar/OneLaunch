package onelaunch.ui;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        VBox root = createHomeScreen();
        
        Scene scene = new Scene(root, 900, 600);//900 is window width , 600 is window height
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

        root.getChildren().add(title);//get children means give the list of everything inside the vbox

        Button addWorkspaceButton = new Button("Add Workspace");

        addWorkspaceButton.setPrefWidth(300);
        addWorkspaceButton.setPrefHeight(60);

        addWorkspaceButton.setOnAction(event -> {
            System.out.println("Button clicked!");
        });

        root.getChildren().add(addWorkspaceButton);

        return root;
    }
    public static void main(String[] args) {
        launch(args);
    }
}