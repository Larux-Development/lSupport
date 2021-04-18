package me.larux.lsupport.listener;

import me.larux.lsupport.LaruxSupportCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final LaruxSupportCore core;

    public PlayerQuitListener(LaruxSupportCore core) {
        this.core = core;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        core.getUserStorage().saveAsync(player.getUniqueId().toString(), true);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        core.getUserStorage().saveAsync(player.getUniqueId().toString(), true);
    }

}
