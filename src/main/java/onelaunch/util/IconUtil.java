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
    public static Node getSearchIcon(int size) {

    FontIcon icon = new FontIcon(FontAwesomeSolid.SEARCH);
    icon.setIconSize(size);
    icon.setIconColor(Color.web("#9CA3AF"));

    return icon;
}

public static Node getFolderIcon(int size) {

    FontIcon icon = new FontIcon(FontAwesomeSolid.FOLDER);

    icon.setIconSize(size);
    icon.setIconColor(Color.web("#D97706"));

    return icon;
}

public static Node getPlayIcon(int size) {

    FontIcon icon = new FontIcon(FontAwesomeSolid.PLAY);

    icon.setIconSize(size);
    icon.setIconColor(Color.WHITE);

    return icon;
}

public static Node getSettingsIcon(int size) {

    FontIcon icon = new FontIcon(FontAwesomeSolid.COG);

    icon.setIconSize(size);
    icon.setIconColor(Color.web("#4B5563"));

    return icon;
}

public static Node getPlusIcon(int size) {

    FontIcon icon = new FontIcon(FontAwesomeSolid.PLUS);

    icon.setIconSize(size);
    icon.setIconColor(Color.WHITE);

    return icon;
}

public static Node getMoreIcon(int size) {

    FontIcon icon = new FontIcon(FontAwesomeSolid.ELLIPSIS_V);

    icon.setIconSize(size);
    icon.setIconColor(Color.web("#4B5563"));

    return icon;
}
public static Node getInfoIcon(int size) {

    FontIcon icon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);

    icon.setIconSize(size);
    icon.setIconColor(Color.web("#4B5563"));

    return icon;
}
}