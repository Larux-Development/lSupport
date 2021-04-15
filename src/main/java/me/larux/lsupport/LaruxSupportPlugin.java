package me.larux.lsupport;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class LaruxSupportPlugin extends JavaPlugin{
	private final PluginDescriptionFile pdfile = getDescription();
	private final String version = pdfile.getVersion();
	private final String name = ChatColor.translateAlternateColorCodes('&', "&6[&blSupport&6]");
	protected String rutaConfig;
	
	public void onEnable() {
		registerConfig();
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m                                                            "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEnabling "+getlSupportName()+"&a..."));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getlSupportName()+" &aby &6LaruxDevelopment&a."));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getlSupportName()+" &aby &cRaider &a& &ctheabdel572&a."));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getlSupportName()+" &aenabled! &9Version: &c"+getlSupportVersion()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m                                                            "));
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m                                                            "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Disabling "+getlSupportName()+"&4..."));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getlSupportName()+" &4by &6LaruxDevelopment&4."));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getlSupportName()+" &4by &cRaider &4& &ctheabdel572&a."));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getlSupportName()+" &4disabled!"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m                                                            "));
	}
	
	public String getlSupportVersion() {
		return this.version;
	}
	public String getlSupportName() {
		return this.name;
	}
	
	public void registerConfig() {
		File config = new File (this.getDataFolder(), ("config.yml"));
		rutaConfig = config.getPath();
		if(!config.exists()) {
			this.getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}
}