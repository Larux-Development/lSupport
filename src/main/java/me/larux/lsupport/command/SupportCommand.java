package me.larux.lsupport.command;

import me.raider.plib.commons.cmd.PLibCommand;
import me.raider.plib.commons.cmd.annotated.annotation.Command;
import me.raider.plib.commons.cmd.annotated.annotation.Default;
import me.raider.plib.commons.cmd.annotated.annotation.Injected;
import org.bukkit.entity.Player;

@Command(name = "support")
public class SupportCommand implements PLibCommand {

    @Command(name = "admin partners")
    public void runAdminCommand(@Injected Player player) {
    }

    @Default
    public void runSupportCommand(@Injected Player player) {
    }

}
