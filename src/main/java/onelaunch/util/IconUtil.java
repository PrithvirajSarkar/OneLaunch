package onelaunch.util;

import javafx.scene.Node;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import onelaunch.model.ItemType;
import javafx.scene.paint.Color;

public final class IconUtil {

    private IconUtil() {
    }

    public static Node getIcon(ItemType type, int size) {

        FontIcon icon;

        switch (type) {

            case WEBSITE:
                icon = new FontIcon(FontAwesomeSolid.GLOBE);
                icon.setIconColor(Color.web("#2563EB"));
                break;

            case APPLICATION:
                icon = new FontIcon(FontAwesomeSolid.DESKTOP);
                icon.setIconColor(Color.web("#16A34A"));
                break;

            case FOLDER:
                icon = new FontIcon(FontAwesomeSolid.FOLDER);
                icon.setIconColor(Color.web("#D97706"));
                break;

            case FILE:
                icon = new FontIcon(FontAwesomeSolid.FILE);
                icon.setIconColor(Color.web("#6B7280"));
                break;

            default:
                icon = new FontIcon(FontAwesomeSolid.FILE);
        }

        icon.setIconSize(size);

        return icon;
    }
}