package onelaunch.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.application.Platform;

public final class DialogUtil {

    public enum ConfirmationStyle {
    DANGER,
    WARNING
    }

    public enum ItemChoice {
    BROWSE,
    WEBSITE,
    CANCEL
    }

    private DialogUtil() {
    }


    //####CONFIRMATIONN
    public static boolean showConfirmation(
            String title,
            String header,
            String message,
            String confirmText,
            String cancelText,
            ConfirmationStyle style) {

        ButtonType confirmButton = new ButtonType(
            confirmText,
            ButtonBar.ButtonData.OK_DONE
        );
        ButtonType cancelButton = new ButtonType(
            cancelText,
            ButtonBar.ButtonData.CANCEL_CLOSE
        );

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                message,
                confirmButton,
                cancelButton
        );

        alert.setTitle(title);
        alert.setHeaderText(header);
        FontIcon icon = new FontIcon(FontAwesomeSolid.EXCLAMATION_TRIANGLE);
        icon.setIconSize(24);
        
        if (style == ConfirmationStyle.DANGER) {
            icon.getStyleClass().add("dialog-danger-icon");
        } else {
            icon.getStyleClass().add("dialog-warning-icon");
        }

        alert.setGraphic(icon);

        setDialogIcon(alert);

        DialogPane dialogPane = alert.getDialogPane();

        applyDialogTheme(dialogPane);

        Button confirm = (Button) dialogPane.lookupButton(confirmButton);
        Button cancel = (Button) dialogPane.lookupButton(cancelButton);

        if (style == ConfirmationStyle.DANGER) {
            confirm.getStyleClass().add("dialog-danger-button");
        } else {
            confirm.getStyleClass().add("dialog-warning-button");
        }

        cancel.getStyleClass().add("dialog-secondary-button");

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == confirmButton;
    }



    //##WARNINGGGG
    public static void showWarning(
        String title,
        String header,
        String message) {

    ButtonType gotItButton = new ButtonType(
            "Got It",
            ButtonBar.ButtonData.OK_DONE
    );

    Alert alert = new Alert(
            Alert.AlertType.WARNING,
            message,
            gotItButton
    );

    alert.setTitle(title);
    alert.setHeaderText(header);

    FontIcon icon = new FontIcon(FontAwesomeSolid.EXCLAMATION_TRIANGLE);
    icon.setIconSize(28);
    icon.getStyleClass().add("dialog-warning-icon");

    alert.setGraphic(icon);

    setDialogIcon(alert);

    DialogPane dialogPane = alert.getDialogPane();

    applyDialogTheme(dialogPane);

    Button gotIt = (Button) dialogPane.lookupButton(gotItButton);
    gotIt.getStyleClass().add("dialog-warning-button");

    alert.showAndWait();
    }



    //##INFORMATIONNNNNNN
    public static void showInfo(
        String title,
        String header,
        String message) {

    ButtonType gotItButton = new ButtonType(
            "Got It",
            ButtonBar.ButtonData.OK_DONE
    );

    Alert alert = new Alert(
            Alert.AlertType.INFORMATION,
            message,
            gotItButton
    );

    alert.setTitle(title);
    alert.setHeaderText(header);

    FontIcon icon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
    icon.setIconSize(24);
    icon.getStyleClass().add("dialog-info-icon");

    alert.setGraphic(icon);

    setDialogIcon(alert);

    DialogPane dialogPane = alert.getDialogPane();

    applyDialogTheme(dialogPane);

    Button gotIt = (Button) dialogPane.lookupButton(gotItButton);
    gotIt.getStyleClass().add("dialog-primary-button");

    alert.showAndWait();
    }


    public static ItemChoice showItemChoice() {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    alert.setTitle("Add Item");
    alert.setHeaderText("Choose what you want to add.");
    alert.setContentText("Add an application, file, folder, or website.");

    FontIcon icon = new FontIcon(FontAwesomeSolid.PLUS_CIRCLE);
    icon.setIconSize(24);
    icon.getStyleClass().add("dialog-info-icon");

    alert.setGraphic(icon);

    setDialogIcon(alert);

    ButtonType browseButton = new ButtonType(
        "Browse",
        ButtonBar.ButtonData.LEFT
    );

    ButtonType websiteButton = new ButtonType(
        "Website",
        ButtonBar.ButtonData.LEFT
    );

    ButtonType cancelButton = new ButtonType(
        "Cancel",
        ButtonBar.ButtonData.CANCEL_CLOSE
    );

    alert.getButtonTypes().setAll(
        browseButton,
        websiteButton,
        cancelButton
    );

    DialogPane dialogPane = alert.getDialogPane();

    applyDialogTheme(dialogPane);

    Button browse = (Button) dialogPane.lookupButton(browseButton);
    Button website = (Button) dialogPane.lookupButton(websiteButton);
    Button cancel = (Button) dialogPane.lookupButton(cancelButton);

    browse.getStyleClass().add("dialog-primary-button");
    website.getStyleClass().add("dialog-website-button");
    cancel.getStyleClass().add("dialog-secondary-button");

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isEmpty()) {
        return ItemChoice.CANCEL;
    }

    if (result.get() == browseButton) {
        return ItemChoice.BROWSE;
    }

    if (result.get() == websiteButton) {
        return ItemChoice.WEBSITE;
    }

    return ItemChoice.CANCEL;
    }



    
    public static Optional<String> showWebsiteInput() {

    TextInputDialog dialog = new TextInputDialog("https://");

    dialog.setTitle("Add Website");
    dialog.setHeaderText("Enter Website URL");
    dialog.setContentText("Website:");

    FontIcon icon = new FontIcon(FontAwesomeSolid.GLOBE);
    icon.setIconSize(24);
    icon.getStyleClass().add("dialog-info-icon");

    dialog.setGraphic(icon);

    setDialogIcon(dialog);

    DialogPane dialogPane = dialog.getDialogPane();

    applyDialogTheme(dialogPane);

    Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
    Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);

    okButton.setText("Add");
    okButton.getStyleClass().add("dialog-primary-button");

    cancelButton.getStyleClass().add("dialog-secondary-button");

    TextField editor = dialog.getEditor();
    editor.setPromptText("https://example.com");

    dialog.setOnShown(e -> editor.selectAll());

    Optional<String> result = dialog.showAndWait();

    if (result.isEmpty()) {
        return Optional.empty();
    }

    return Optional.of(result.get().trim());
    }




    private static void setDialogIcon(Alert alert) {

    alert.setOnShown( e -> {

        Platform.runLater(() -> {

        Stage dialodStage =(Stage) alert.getDialogPane()
                        .getScene()
                        .getWindow();

        Image appIcon = new Image(
                DialogUtil.class
                        .getResourceAsStream("/onelaunch-icon.png")
        );

        dialodStage.getIcons().setAll(appIcon);
    });
    });
    }




    public static void setDialogIcon(TextInputDialog dialog) {
        dialog.addEventHandler(
        javafx.scene.control.DialogEvent.DIALOG_SHOWN,
        e -> {

            Platform.runLater(() -> {

                Stage dialStage = (Stage) dialog.getDialogPane()
                        .getScene()
                        .getWindow();

                Image appIcon = new Image(
                        DialogUtil.class
                                .getResourceAsStream("/onelaunch-icon.png")
                );

                dialStage.getIcons().setAll(appIcon);
            });
        }
    );
    }


    
    public static void applyDialogTheme(DialogPane dialogPane) {

    dialogPane.getStylesheets().add(
        DialogUtil.class.getResource("/style.css").toExternalForm()
    );

    String darkStylesheet =
        DialogUtil.class.getResource("/dark.css").toExternalForm();

    if ("true".equals(System.getProperty("onelaunch.darkMode"))) {
        dialogPane.getStylesheets().add(darkStylesheet);
    }

    dialogPane.getStyleClass().add("onelaunch-dialog");
    }
}