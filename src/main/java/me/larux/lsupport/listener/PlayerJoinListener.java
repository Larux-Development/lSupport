package me.larux.lsupport.listener;

import me.larux.lsupport.PluginCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PluginCore core;

    public PlayerJoinListener(PluginCore core) {
        this.core = core;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        core.getUserStorage().loadAsync(player.getUniqueId().toString(), true);
    }
}
