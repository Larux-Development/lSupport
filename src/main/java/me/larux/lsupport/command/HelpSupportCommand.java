package me.larux.lsupport.command;

import org.bukkit.ChatColor;

import me.larux.lsupport.PluginCore;
import me.raider.plib.bukkit.cmd.BukkitSender;
import me.raider.plib.commons.cmd.PLibCommand;
import me.raider.plib.commons.cmd.annotated.annotation.Command;
import me.raider.plib.commons.cmd.annotated.annotation.Default;
import me.raider.plib.commons.cmd.annotated.annotation.Injected;

@Command(name = "help lsupport")
public class HelpSupportCommand implements PLibCommand{
	@SuppressWarnings("unused")
	private final PluginCore core;
	public HelpSupportCommand(PluginCore core) {
		this.core = core;
	}
	@Default
	public void runDefaultClassCommand(@Injected BukkitSender sender) {
		if(sender.isPlayerSender()) {
			if(sender.getSender().hasPermission("lsupport.admin")) {
				sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRun &c/support admin help &ato see this message."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support &1- &2Opens the supporters GUI."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support add <partner> &1- &2Start supporting a partner."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support remove <partner> &1- &2Stop supporting a partner."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin partner add &1- &2Add one player to the partners list."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin partner remove &1- &2Remove one player from the partners list."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support view <partner> &1- &2Use this to see the supports a partner has."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support top &1- &2Use this to see the top of partners with more supports."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support reload &1- &2Reload files [config.yml, lang.yml, menu.yml]."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin help &1- &2Shows this help message."));
			}else {
				sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support &1- &2Opens the supporters GUI."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support add <partner> &1- &2Start supporting a partner."));
		        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support remove <partner> &1- &2Stop supporting a partner."));
			}
		}else {
			sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRun &c/support admin help &ato see this message."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support &1- &2Opens the supporters GUI."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support add <partner> &1- &2Start supporting a partner."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support remove <partner> &1- &2Stop supporting a partner."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin partner add &1- &2Add one player to the partners list."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin partner remove &1- &2Remove one player from the partners list."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support view <partner> &1- &2Use this to see the supports a partner has."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support top &1- &2Use this to see the top of partners with more supports."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support reload &1- &2Reload files [config.yml, lang.yml, menu.yml]."));
	        sender.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/support admin help &1- &2Shows this help message."));
		}
	}
}