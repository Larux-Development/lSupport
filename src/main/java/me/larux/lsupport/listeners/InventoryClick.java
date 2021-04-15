package me.larux.lsupport.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.larux.lsupport.LaruxSupportPlugin;

public class InventoryClick implements Listener{
	private final LaruxSupportPlugin plugin;
	public InventoryClick(LaruxSupportPlugin plugin) {
		this.plugin = plugin;
	}
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory().getName().equalsIgnoreCase("Partner Menu")) {
			
			e.setCancelled(true);
		}
	}
}
