package me.larux.lsupport.util;

import org.bukkit.ChatColor;

public class Utils {

    private Utils() {}

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
