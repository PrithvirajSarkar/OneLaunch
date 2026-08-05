package onelaunch.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

public final class DialogUtil {

    public enum ConfirmationStyle {
    DANGER,
    WARNING
    }

    private DialogUtil() {
    }

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

        DialogPane dialogPane = alert.getDialogPane();

        dialogPane.getStylesheets().add(
                DialogUtil.class.getResource("/style.css").toExternalForm()
        );

        dialogPane.getStyleClass().add("onelaunch-dialog");

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

    DialogPane dialogPane = alert.getDialogPane();

    dialogPane.getStylesheets().add(
            DialogUtil.class.getResource("/style.css").toExternalForm()
    );

    dialogPane.getStyleClass().add("onelaunch-dialog");

    Button gotIt = (Button) dialogPane.lookupButton(gotItButton);
    gotIt.getStyleClass().add("dialog-warning-button");

    alert.showAndWait();
    }

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

    DialogPane dialogPane = alert.getDialogPane();

    dialogPane.getStylesheets().add(
            DialogUtil.class.getResource("/style.css").toExternalForm()
    );

    dialogPane.getStyleClass().add("onelaunch-dialog");

    Button gotIt = (Button) dialogPane.lookupButton(gotItButton);
    gotIt.getStyleClass().add("dialog-primary-button");

    alert.showAndWait();
    }
}