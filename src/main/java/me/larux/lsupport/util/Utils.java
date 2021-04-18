package me.larux.lsupport.util;

import org.bukkit.ChatColor;

public class Utils {

    private Utils() {}

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String removeFileExtension(String fileName) {
        if (fileName == null) return null;
        int pos = fileName.lastIndexOf(".");
        if (pos == -1)
            return fileName;
        return fileName.substring(0, pos);
    }

}
