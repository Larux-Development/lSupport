package me.larux.lsupport.command;

import me.larux.lsupport.PluginCore;
import me.larux.lsupport.menu.LaruxSupportMenu;
import me.larux.lsupport.storage.object.Partner;
import me.raider.plib.bukkit.cmd.BukkitSender;
import me.raider.plib.commons.cmd.PLibCommand;
import me.raider.plib.commons.cmd.annotated.annotation.Command;
import me.raider.plib.commons.cmd.annotated.annotation.Default;
import me.raider.plib.commons.cmd.annotated.annotation.Injected;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Optional;

@Command(name = "support")
public class SupportCommand implements PLibCommand {

    private final PluginCore core;

    public SupportCommand(PluginCore core) {
        this.core = core;
    }

    @Command(name = "admin partner add", permission = "lsupport.admin")
    public void runAdminCommand(@Injected BukkitSender sender, String name) {
        CommandSender player = sender.getSender();
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
    public void runDeleteCommand(@Injected BukkitSender sender, String name) {
        CommandSender player = sender.getSender();
        @SuppressWarnings("deprecation")
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Partner partner = core.getStorage().get().get(offlinePlayer.getUniqueId().toString());
        if (partner==null) {
            player.sendMessage(core.getLang().getString("messages.admin.error-deleting-partner"));
            return;
        }
        switch (core.getStorageType()) {
            case YAML:
                File file = new File(core.getPlugin().getDataFolder().getAbsolutePath() + "/data/", partner.getId() + ".yml");
                if (file.delete()) {
                    core.getStorage().get().remove(partner.getId());
                    player.sendMessage(core.getLang().getString("messages.admin.success-deleting-partner"));
                }
                break;
            case MONGODB:
                deleteMongoDocument(partner.getId());
                break;
            default:
        }
    }

    @Command(name = "add")
    public void runAddCommand(@Injected BukkitSender sender, String name) {
        if (!sender.isPlayerSender()) {
            return;
        }
        Player player = sender.getSender(Player.class).get();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Partner partner = core.getStorage().get().get(offlinePlayer.getUniqueId().toString());
        if (partner==null) {
            player.sendMessage(core.getLang().getString("messages.add.cant-find"));
            return;
        }
        core.getPartnerHandler().addSupporterToPartner(partner, player);
    }

    @Command(name = "remove")
    public void runRemoveSupCommand(@Injected BukkitSender sender, String name) {
        if (!sender.isPlayerSender()) {
            return;
        }
        Player player = sender.getSender(Player.class).get();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Partner partner = core.getStorage().get().get(offlinePlayer.getUniqueId().toString());
        if (partner==null) {
            player.sendMessage(core.getLang().getString("messages.remove.cant-find"));
            return;
        }
        core.getPartnerHandler().removeSupportToPartner(partner, player);
    }

    @Command(name = "reload", permission = "lsupport.admin")
    public void runReloadCommand(@Injected BukkitSender sender) {
        CommandSender player = sender.getSender();
    	File lang = new File(core.getPlugin().getDataFolder(), "lang.yml");
    	File menu = new File(core.getPlugin().getDataFolder(), "lang.yml");
    	File config = new File(core.getPlugin().getDataFolder(), "lang.yml");
    	if(lang.exists()) {
    		core.getLang().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload").replaceAll("%file%", "lang.yml"));
    	}
    	if(menu.exists()) {
    		core.getMenu().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload").replaceAll("%file%", "menu.yml"));
    	}
    	if(config.exists()) {
    		core.getConfig().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload").replaceAll("%file%", "config.yml"));
    	}
    }



    @Command(name = "admin help", permission = "lsupport.admin")
    public void runHelpCommand(@Injected BukkitSender sender) {
    	sendHelpMessage(sender.getSender());
    }

    @Default
    public void runSupportCommand(@Injected BukkitSender sender) {
        if (!sender.isPlayerSender()) {
            return;
        }
        Optional<Player> player = sender.getSender(Player.class);
        if (!player.isPresent()) {
            return;
        }
        new LaruxSupportMenu(core).openMenu(player.get());
    }

    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRun &c/support admin help &ato see this message."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin partner add &1- &2Add one player to the partners list."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin partner remove &1- &2Remove one player from the partners list."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support reload &1- &2Reload files [config.yml, lang.yml, menu.yml]."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin help &1- &2Shows this help message."));
    }

    private void deleteMongoDocument(String id) {
        Bukkit.getScheduler().runTaskAsynchronously(core.getPlugin(), () -> {
            Document document = new Document().append("id", id);
            core.getMongoDB().getCollection("partners").deleteOne(document);
        });
    }
}
