package me.larux.lsupport.command;

import me.larux.lsupport.LaruxSupportCore;
import me.larux.lsupport.menu.LaruxSupportMenu;
import me.larux.lsupport.storage.object.Partner;
import me.raider.plib.commons.cmd.PLibCommand;
import me.raider.plib.commons.cmd.annotated.annotation.Command;
import me.raider.plib.commons.cmd.annotated.annotation.Default;
import me.raider.plib.commons.cmd.annotated.annotation.Injected;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;

@Command(name = "support")
public class SupportCommand implements PLibCommand {

    private final LaruxSupportCore core;
    
    public SupportCommand(LaruxSupportCore core) {
        this.core = core;
    }

    @Command(name = "admin partner add", permission = "lsupport.admin")
    public void runAdminCommand(@Injected Player player, String name) {
        @SuppressWarnings("deprecation")
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (!offlinePlayer.hasPlayedBefore()) {
            player.sendMessage(core.getLang().getString("messages.admin.error-player-never-played"));
            return;
        }
        Partner partner = core.getStorage().load(offlinePlayer.getUniqueId().toString(), true, false);
        if (partner!=null) {
            player.sendMessage(core.getLang().getString("messages.admin.error-partner-exist"));
            return;
        }
        player.sendMessage(core.getLang().getString("messages.admin.success-creating-partner"));
    }

    @Command(name = "admin partner remove", permission = "lsupport.admin")
    public void runDeleteCommand(@Injected Player player, String name) {
        @SuppressWarnings("deprecation")
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Partner partner = core.getStorage().get().get(offlinePlayer.getUniqueId().toString());
        if (partner==null) {
            player.sendMessage(core.getLang().getString("messages.admin.error-deleting-partner"));
            return;
        }
        File file = new File(core.getPlugin().getDataFolder().getAbsolutePath() + "/data/", partner.getId() + ".yml");
        if (file.delete()) {
            core.getStorage().get().remove(partner.getId());
            player.sendMessage(core.getLang().getString("messages.admin.success-deleting-partner"));
        }
    }
    @Command(name = "reload", permission = "lsupport.admin")
    public void runReloadCommand(@Injected Player player) {
    	File lang = new File(core.getPlugin().getDataFolder(), "lang.yml");
    	File menu = new File(core.getPlugin().getDataFolder(), "lang.yml");
    	File config = new File(core.getPlugin().getDataFolder(), "lang.yml");
    	if(lang.exists()) {
    		core.getLang().save();
    		core.getLang().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload").replaceAll("%file%", "lang.yml"));
    	}
    	if(menu.exists()) {
    		core.getMenu().save();
    		core.getMenu().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload").replaceAll("%file%", "menu.yml"));
    	}
    	if(config.exists()) {
    		core.getConfig().save();
    		core.getConfig().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload").replaceAll("%file%", "config.yml"));
    	}
    }
    @Command(name = "admin help", permission = "lsupport.admin")
    public void runHelpCommand(@Injected Player player) {
    	sendHelpMessage(player);
    }
    @Default
    public void runSupportCommand(@Injected Player player) {
        new LaruxSupportMenu(core).openMenu(player);
    }

    public void sendHelpMessage(Player player) {
    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRun &c/support admin help &ato see this message."));
    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin partner add &1- &2Add one player to the partners list."));
    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin partner remove &1- &2Remove one player from the partners list."));
    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support reload &1- &2Reload files [config.yml, lang.yml, menu.yml]."));
    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin help &1- &2Shows this help message."));
    }
}
