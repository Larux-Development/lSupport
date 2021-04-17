package me.larux.lsupport.storage.partner;

import me.raider.plib.commons.serializer.annotated.annotation.Serializable;
import me.raider.plib.commons.serializer.annotated.annotation.Serialize;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

@Serializable
public class Partner {

    @Serialize(path = "id")
    private final String id;

    @Serialize(path = "partners")
    private int partners;

    public Partner(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getPartners() {
        return partners;
    }

    public void setPartners(int partners) {
        this.partners = partners;
    }

    public OfflinePlayer getPlayerFromId() {
        return Bukkit.getOfflinePlayer(UUID.fromString(id));
    }
}
