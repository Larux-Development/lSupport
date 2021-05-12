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
import java.util.*;

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
        Optional<Player> pSender = sender.getSender(Player.class);
        if (!pSender.isPresent()) {
            return;
        }
        Player player = pSender.get();
        @SuppressWarnings("deprecation")
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Partner partner = core.getStorage().get().get(offlinePlayer.getUniqueId().toString());
        if (partner==null) {
            player.sendMessage(core.getLang().getString("messages.cant-find"));
            return;
        }
        core.getPartnerHandler().addSupporterToPartner(partner, player);
    }

    @Command(name = "remove")
    public void runRemoveSupCommand(@Injected BukkitSender sender, String name) {
        if (!sender.isPlayerSender()) {
            return;
        }
        Optional<Player> pSender = sender.getSender(Player.class);
        if (!pSender.isPresent()) {
            return;
        }
        Player player = pSender.get();
        @SuppressWarnings("deprecation")
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
    	File menu = new File(core.getPlugin().getDataFolder(), "menu.yml");
    	File config = new File(core.getPlugin().getDataFolder(), "config.yml");
    	if(lang.exists()) {
    		core.getLang().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload")
                    .replaceAll("%file%", "lang.yml"));
    	}
    	if(menu.exists()) {
    		core.getMenu().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload")
                    .replaceAll("%file%", "menu.yml"));
    	}
    	if(config.exists()) {
    		core.getConfig().reload();
    		player.sendMessage(core.getLang().getString("messages.admin.success-reload")
                    .replaceAll("%file%", "config.yml"));
    	}
    }

    @Command(name = "view")
    public void runSeeCommand(@Injected BukkitSender sender, String name) {
        if (!sender.isPlayerSender()) {
            return;
        }
        Optional<Player> pSender = sender.getSender(Player.class);
        if (!pSender.isPresent()) {
            return;
        }
        Player player = pSender.get();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Partner partner = core.getStorage().get().get(offlinePlayer.getUniqueId().toString());
        if (partner==null) {
            player.sendMessage(core.getLang().getString("messages.cant-find"));
            return;
        }
        player.sendMessage(core.getLang().getString("messages.view.cmd")
                .replace("%name%", partner.getPlayerFromId().getName())
                .replace("%supporters%", String.valueOf(partner.getPartners())));
    }

    @Command(name = "top")
    public void runTopCommand(@Injected BukkitSender sender) {
        if (!sender.isPlayerSender()) {
            return;
        }
        Optional<Player> pSender = sender.getSender(Player.class);
        if (!pSender.isPresent()) {
            return;
        }
        Player player = pSender.get();
        Collection<Partner> partners = core.getStorage().get().values();
        if (partners.isEmpty()) {
            player.sendMessage(core.getLang().getString("messages.cant-find"));
            return;
        }
        List<Partner> partnerList = new ArrayList<>(partners);

        partnerList.sort(Comparator.comparingInt(Partner::getPartners));
        Collections.reverse(partnerList);

        List<String> messages = core.getLang().getStringList("messages.view.top");

        for (int i = 0 ; i < partnerList.size() ; i++) {
            String m = "";
            for (String message : messages) {
                m = message.replace("%partner_name_" + i + 1 + "%", partnerList.get(i).getPlayerFromId().getName())
                        .replace("%partner_supporters_" + i + 1 + "%", String.valueOf(partnerList.get(i).getPartners()));
            }
            player.sendMessage(m);
        }
    }


    @Command(name = "admin help", permission = "lsupport.admin")
    public void runHelpCommand(@Injected BukkitSender sender) {
    	sendHelpMessage(sender.getSender());
    }
    @Command(name = "help")
    public void runUserHelpCommand(@Injected BukkitSender sender) {
    	if(sender.isPlayerSender()) {
    		if(sender.getSender().hasPermission("lsupport.admin")) {
    			sendHelpMessage(sender.getSender());
    		}else {
    			sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
        		sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support &1- &2Opens the supporters GUI."));
        		sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support add <partner> &1- &2Start supporting a partner."));
        		sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support remove <partner> &1- &2Stop supporting a partner."));
    		}
    	}else {
    		sendHelpMessage(sender.getSender());
    	}
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
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support &1- &2Opens the supporters GUI."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support add <partner> &1- &2Start supporting a partner."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support remove <partner> &1- &2Stop supporting a partner."));
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
