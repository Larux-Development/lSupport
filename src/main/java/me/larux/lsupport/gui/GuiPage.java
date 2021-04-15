package me.larux.lsupport.gui;

import me.larux.lsupport.gui.item.GuiItem;
import me.larux.lsupport.gui.item.buttom.Button;
import me.raider.commons.utils.Nameable;
import org.bukkit.inventory.Inventory;

public interface GuiPage extends Nameable {

    int getSlots();

    GuiItem[] getItems();

    Inventory getInventory();

    GuiItem getItemFromSlot(int slot);

    interface Builder {

        Builder slots(int slots);

        Builder addItem(GuiItem item);

        Builder addButton(Button button);

        static Builder create(String name, int slots) {
            return new SimpleGuiPage.Builder(name, slots);
        }

        GuiPage build();

    }
}
