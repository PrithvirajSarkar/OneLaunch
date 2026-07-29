package onelaunch.util;

import onelaunch.model.LaunchItem;

public final class DisplayNameUtil {

    private DisplayNameUtil() {
    }

    public static String getDisplayName(LaunchItem item) {

        String name = item.getName();

        switch (item.getType()) {

            case WEBSITE:

                String url = item.getPath();

                url = url.replaceFirst("^https?://", "");
                url = url.replaceFirst("^www\\.", "");

                int slash = url.indexOf('/');

                if (slash != -1) {
                    url = url.substring(0, slash);
                }

                return url;

            case APPLICATION:

                if (name.toLowerCase().endsWith(".exe")) {
                    return name.substring(0, name.length() - 4);
                }

                return name;

            default:
                return name;
        }
    }
}