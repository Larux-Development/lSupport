package me.larux.lsupport.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import me.larux.lsupport.LaruxSupportPlugin;

public class SupportMenu {
	protected final LaruxSupportPlugin pl;
	public SupportMenu(LaruxSupportPlugin pl) {
		this.pl = pl;
	}
	public void openSupportMenu(Player player) {
		FileConfiguration config = pl.getConfig();
		Inventory inv = Bukkit.createInventory(null, 56, ChatColor.translateAlternateColorCodes('&', config.getString("SupportInventoryName")));
	}
}
