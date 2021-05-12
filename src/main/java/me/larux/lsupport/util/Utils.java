package me.larux.lsupport.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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

    public static void helpMessage(CommandSender sender) {
        sender.sendMessage(colorize("&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
        sender.sendMessage(colorize("&aRun &c/support admin help &ato see this message."));
        sender.sendMessage(colorize("&a/support &1- &2Opens the supporters GUI."));
        sender.sendMessage(colorize("&a/support add <partner> &1- &2Start supporting a partner."));
        sender.sendMessage(colorize("&a/support remove <partner> &1- &2Stop supporting a partner."));
        sender.sendMessage(colorize("&a/support admin partner add &1- &2Add one player to the partners list."));
        sender.sendMessage(colorize("&a/support admin partner remove &1- &2Remove one player from the partners list."));
        sender.sendMessage(colorize("&a/support view <partner> &1- &2Use this to see the supports a partner has."));
        sender.sendMessage(colorize("&a/support top &1- &2Use this to see the top of partners with more supports."));
        sender.sendMessage(colorize("&a/support reload &1- &2Reload files [config.yml, lang.yml, menu.yml]."));
        sender.sendMessage(colorize("&a/support admin help &1- &2Shows this help message."));
    }

}
