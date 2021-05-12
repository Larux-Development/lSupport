package me.larux.lsupport.command;

import me.larux.lsupport.util.Utils;
import me.raider.plib.bukkit.cmd.BukkitSender;
import me.raider.plib.commons.cmd.PLibCommand;
import me.raider.plib.commons.cmd.annotated.annotation.Command;
import me.raider.plib.commons.cmd.annotated.annotation.Default;
import me.raider.plib.commons.cmd.annotated.annotation.Injected;

@Command(name = "help lsupport")
public class HelpSupportCommand implements PLibCommand{

	@Default
	public void runDefaultClassCommand(@Injected BukkitSender sender) {
		if(sender.isPlayerSender()) {
			if(sender.getSender().hasPermission("lsupport.admin")) {
				Utils.helpMessage(sender.getSender());
			} else {
				sender.getSender().sendMessage(Utils.colorize("&6[&blSupport&6] &aby &cRaider &a& &ctheabdel572&a."));
		        sender.getSender().sendMessage(Utils.colorize("&a/support &1- &2Opens the supporters GUI."));
		        sender.getSender().sendMessage(Utils.colorize("&a/support add <partner> &1- &2Start supporting a partner."));
		        sender.getSender().sendMessage(Utils.colorize("&a/support remove <partner> &1- &2Stop supporting a partner."));
			}
		} else {
			Utils.helpMessage(sender.getSender());
		}
	}
}