package me.larux.lsupport.command;

import me.larux.lsupport.PluginCore;
import me.larux.lsupport.menu.LaruxSupportMenu;
import me.larux.lsupport.storage.object.Partner;
import me.larux.lsupport.storage.object.User;
import me.larux.lsupport.util.Utils;
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
                for (User user : core.getAllUsersByYaml()) {
                    if (user.getSupported().containsKey(partner.getId())) {
                        user.getSupported().remove(partner.getId());
                        core.getSerializer().getYamlUserSerializerManager().serialize(user, user.getId());
                    }
                }
                File file = new File(core.getPlugin().getDataFolder().getAbsolutePath() + "/data/", partner.getId() + ".yml");
                if (file.delete()) {
                    core.getStorage().get().remove(partner.getId());
                    player.sendMessage(core.getLang().getString("messages.admin.success-deleting-partner"));
                }
                break;
            case MONGODB:
                for (User user : core.getAllUsersByMongo()) {
                    if (user.getSupported().containsKey(partner.getId())) {
                        user.getSupported().remove(partner.getId());
                        core.getSerializer().getMongoUserSerializerManager().serialize(user, user.getId());
                    }
                }
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
        @SuppressWarnings("deprecation")
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

        for (int m = 0 ; m < messages.size() ; m++) {
            String messageCopy = "";
            for (int i = 0 ; i < partnerList.size() ; i++) {
                if (i == m) {
                    int index = i + 1;
                    messageCopy = messages.get(m).replace("%partner_name_" + index + "%", partnerList.get(i).getPlayerFromId().getName())
                            .replace("%partner_supporters_" + index + "%", String.valueOf(partnerList.get(i).getPartners()));
                }
            }
            player.sendMessage(messageCopy);
        }
    }

    @Command(name = "admin help", permission = "lsupport.admin")
    public void runHelpCommand(@Injected BukkitSender sender) {
        Utils.helpMessage(sender.getSender());
    }

    @Command(name = "help")
    public void runUserHelpCommand(@Injected BukkitSender sender) {
    	if(sender.isPlayerSender()) {
    		if(sender.getSender().hasPermission("lsupport.admin")) {
                Utils.helpMessage(sender.getSender());
    		}else {
    			sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
        		sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support &1- &2Opens the supporters GUI."));
        		sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support add <partner> &1- &2Start supporting a partner."));
        		sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support remove <partner> &1- &2Stop supporting a partner."));
    		}
    	}else {
            Utils.helpMessage(sender.getSender());
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

    private void deleteMongoDocument(String id) {
        Bukkit.getScheduler().runTaskAsynchronously(core.getPlugin(), () -> {
            Document document = new Document().append("id", id);
            core.getMongoDB().getCollection("partners").deleteOne(document);
        });
    }
}
