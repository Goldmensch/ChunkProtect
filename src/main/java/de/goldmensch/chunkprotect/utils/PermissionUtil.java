package de.goldmensch.chunkprotect.utils;

public class PermissionUtil {

    public static String build(String prefix, String... args) {
        StringBuilder builder = new StringBuilder(prefix+".");
        for(String c : args) {
            builder.append(c);
            builder.append(".");
        }
        return builder.substring(0, builder.length()-1);
    }

}
