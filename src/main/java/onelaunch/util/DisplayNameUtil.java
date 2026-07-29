package onelaunch.util;

import onelaunch.model.LaunchItem;

public final class DisplayNameUtil {

    private static final int MAX_DISPLAY_LENGTH = 24;

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

                return truncate(url);

            case APPLICATION:

                if (name.toLowerCase().endsWith(".exe")) {
                    return truncate(name.substring(0, name.length() - 4));
                }

                return truncate(name);

            default:

                return truncate(name);
        }
    }

    private static String truncate(String text) {

        if (text == null) {
            return "";
        }

        if (text.length() <= MAX_DISPLAY_LENGTH) {
            return text;
        }

        return text.substring(0, MAX_DISPLAY_LENGTH - 1) + "…";
    }
}