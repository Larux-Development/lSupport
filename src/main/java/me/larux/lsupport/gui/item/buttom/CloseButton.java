package me.larux.lsupport.gui.item.buttom;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class CloseButton extends Button {

    public CloseButton(int slot, ItemStack item, boolean rightClick, boolean leftClick) {
        super(slot,
                (inventoryClickEvent, gui) -> {
            if (rightClick && inventoryClickEvent.isRightClick()) {
                gui.closeMenu((Player) inventoryClickEvent.getWhoClicked());
            }
            else if (leftClick && inventoryClickEvent.isLeftClick()) {
                gui.closeMenu((Player) inventoryClickEvent.getWhoClicked());
            }

        }, item);
    }
}
