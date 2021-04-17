package me.larux.lsupport.command;

import me.larux.lsupport.LaruxSupportCore;
import me.larux.lsupport.storage.partner.Partner;
import me.raider.plib.commons.cmd.PLibCommand;
import me.raider.plib.commons.cmd.annotated.annotation.Command;
import me.raider.plib.commons.cmd.annotated.annotation.Default;
import me.raider.plib.commons.cmd.annotated.annotation.Injected;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Command(name = "support")
public class SupportCommand implements PLibCommand {

    private final LaruxSupportCore core;

    public SupportCommand(LaruxSupportCore core) {
        this.core = core;
    }

    @Command(name = "admin partners")
    public void runAdminCommand(@Injected Player player, String name) {
        if (!player.hasPermission("lsupport.admin")) {
            player.sendMessage(core.getLang().getString("messages.no-perm"));
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Partner partner = core.getStorage().load(offlinePlayer.getUniqueId().toString(), true, false);

        if (partner!=null) {
            player.sendMessage(core.getLang().getString("error-partner-exist"));
            return;
        }
        player.sendMessage(core.getLang().getString("messages.admin.success-creating-partner"));
    }

    @Default
    public void runSupportCommand(@Injected Player player) {
    }

}
