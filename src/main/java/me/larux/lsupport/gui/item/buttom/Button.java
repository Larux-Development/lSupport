package me.larux.lsupport.gui.item.buttom;

import me.larux.lsupport.gui.item.SimpleGuiItem;
import me.larux.lsupport.gui.item.action.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button extends SimpleGuiItem {

    public Button(int slot, Action<InventoryClickEvent> action, ItemStack item) {
        super(slot, action, item);
    }
}
