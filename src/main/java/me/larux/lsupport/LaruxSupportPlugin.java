package me.larux.lsupport;

import me.larux.lsupport.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class LaruxSupportPlugin extends JavaPlugin {

	private final PluginDescriptionFile pdfile = getDescription();
	private final String version = pdfile.getVersion();
	private final String name = Utils.colorize("&6[&blSupport&6]");

	private PluginCore core;

	@Override
	public void onLoad() {
		this.core = new LaruxSupportCore(this);
	}

	@Override
	public void onEnable() {
		sendEnableOrDisableMessage(true);
		core.init();
	}

	@Override
	public void onDisable() {
		sendEnableOrDisableMessage(false);
		core.disable();
	}
	
	public String getlSupportVersion() {
		return this.version;
	}

	public String getlSupportName() {
		return this.name;
	}

	public void sendEnableOrDisableMessage(boolean enabling) {
		if (enabling) {
			Bukkit.getConsoleSender().sendMessage(Utils.colorize("&7&m                                                            "));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize("&aEnabling " + getlSupportName()+"&a..."));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize(getlSupportName() + " &aby &6LaruxDevelopment&a."));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize(getlSupportName() + " &aby &cRaider &a& &ctheabdel572&a."));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize(getlSupportName() + " &aenabled! &9Version: &c"+getlSupportVersion()));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize("&7&m                                                            "));
		} else {
			Bukkit.getConsoleSender().sendMessage(Utils.colorize("&7&m                                                            "));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize("&4Disabling "+getlSupportName()+"&4..."));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize(getlSupportName()+" &4by &6LaruxDevelopment&4."));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize(getlSupportName()+" &4by &cRaider &4& &ctheabdel572&a."));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize(getlSupportName()+" &4disabled!"));
			Bukkit.getConsoleSender().sendMessage(Utils.colorize("&7&m                                                            "));
		}
	}
}