package me.larux.lsupport.storage.handler;

import me.larux.lsupport.PluginCore;
import me.larux.lsupport.storage.object.Partner;
import me.larux.lsupport.storage.object.User;
import org.bukkit.entity.Player;

import java.util.Map;

public class PartnerHandler {

    private final PluginCore core;

    public PartnerHandler(PluginCore core) {
        this.core = core;
    }

    public void addSupporterToPartner(Partner partner, Player player) {
        if (!player.hasPermission("lsupport.add") || !player.hasPermission("lsupport.user")) {
            player.sendMessage(core.getLang().getString("messages.no-perm")
                    .replace("%partner_name%", partner.getPlayerFromId().getName()));
            return;
        }

        Map<String, User> supporters = core.getUserStorage().get();
        Map<String, Integer> supporting = supporters.get(player.getUniqueId().toString()).getSupported();

        int value = 0;
        for (int i : supporting.values()) {
            value+=i;
        }
        if (value>=core.getConfig().getInt("config.max-supports-per-player")) {
            player.sendMessage(core.getLang().getString("messages.add.used-all-supports")
                    .replace("%partner_name%", partner.getPlayerFromId().getName()));
            return;
        }

        partner.setPartners(partner.getPartners() + 1);
        supporting.put(partner.getId(), supporting.containsKey(partner.getId()) ? supporting.get(partner.getId()) + 1 : 1);

        player.sendMessage(core.getLang().getString("messages.add.now-supporting")
                .replace("%partner_name%", partner.getPlayerFromId().getName())
                .replace("%times_supported%", String.valueOf(supporting.get(partner.getId()))));
    }

    public void removeSupportToPartner(Partner partner, Player player) {
        if (!core.getConfig().getBoolean("config.stop-supporting")) {
            player.sendMessage(core.getLang().getString("messages.disabled"));
            return;
        }

        if (!player.hasPermission("lsupport.remove") || !player.hasPermission("lsupport.user")) {
            player.sendMessage(core.getLang().getString("messages.no-perm")
                    .replace("%partner_name%", partner.getPlayerFromId().getName()));
        }

        Map<String, User> supporters = core.getUserStorage().get();
        Map<String, Integer> supporting = supporters.get(player.getUniqueId().toString()).getSupported();

        if (!supporting.containsKey(partner.getId())) {
            player.sendMessage(core.getLang().getString("messages.remove.not-supporting")
                    .replace("%partner_name%", partner.getPlayerFromId().getName()));
            return;
        }

        partner.setPartners(partner.getPartners() - 1);

        if (supporting.get(partner.getId())-1==0) {
            supporting.remove(partner.getId());
        } else {
            supporting.put(partner.getId(), supporting.get(partner.getId()) - 1);
        }

        player.sendMessage(core.getLang().getString("messages.remove.success")
                .replace("%partner_name%", partner.getPlayerFromId().getName()));

    }

}
