package net.reader128k.util;


public class URLHelper {
    public static String changeGravatarSize(String url, int size) {
        return url.contains("?s=18") ? url.replace("?s=18", String.format("?s=%d", size)) : url;
    }

    public static String setDefaultGravatarType(String url, String type) {
        return url.contains("?") ? url + "&d=" + type : url + "?d=" + type;
    }
}
