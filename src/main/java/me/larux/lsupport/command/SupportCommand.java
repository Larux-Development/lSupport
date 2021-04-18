package me.larux.lsupport.command;

import me.larux.lsupport.LaruxSupportCore;
import me.larux.lsupport.menu.LaruxSupportMenu;
import me.larux.lsupport.storage.partner.Partner;
import me.raider.plib.commons.cmd.PLibCommand;
import me.raider.plib.commons.cmd.annotated.annotation.Command;
import me.raider.plib.commons.cmd.annotated.annotation.Default;
import me.raider.plib.commons.cmd.annotated.annotation.Injected;
import org.bukkit.Bukkit;
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

    @Default
    public void runSupportCommand(@Injected Player player) {
        new LaruxSupportMenu(core).openMenu(player);
    }

}
