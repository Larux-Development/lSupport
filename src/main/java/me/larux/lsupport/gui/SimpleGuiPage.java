package me.larux.lsupport.gui;


import me.larux.lsupport.gui.item.GuiItem;
import me.larux.lsupport.gui.item.SimpleGuiItem;
import me.larux.lsupport.gui.item.buttom.Button;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class SimpleGuiPage implements GuiPage {

    private final String name;
    private final int slots;
    private final GuiItem[] guiItems;
    private final Inventory inventory;

    public SimpleGuiPage(String name, int slots, GuiItem[] guiItems, Inventory inventory) {
        this.name = name;
        this.slots = slots;
        this.guiItems = guiItems;
        this.inventory = inventory;
    }

    @Override
    public int getSlots() {
        return slots;
    }

    @Override
    public GuiItem[] getItems() {
        return guiItems;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GuiItem getItemFromSlot(int slot) {
        for (GuiItem guiItem : this.guiItems) {
            if (guiItem!=null && guiItem.getSlot() == slot) {
                return guiItem;
            }
        }
        return null;
    }

    @Override
    public void update() {
        for (GuiItem item : guiItems) {
            if (item!=null) {
                inventory.setItem(item.getSlot(), item.getItem());
            }
        }
    }

    public static class Builder implements GuiPage.Builder {

        private final String name;
        private int slots;
        private final GuiItem[] guiItems;
        private final Inventory inventory;

        public Builder(String name, int slots) {
            this.name=name;
            this.slots=slots;
            this.inventory= Bukkit.createInventory(null, slots, name);
            this.guiItems=new SimpleGuiItem[slots];
        }

        @Override
        public Builder slots(int slots) {
            this.slots=slots;
            return this;
        }

        @Override
        public Builder addItem(GuiItem item) {
            this.guiItems[item.getSlot()]=item;
            inventory.setItem(item.getSlot(), item.getItem());
            return this;
        }

        @Override
        public Builder addButton(Button button) {
            this.guiItems[button.getSlot()]=button;
            inventory.setItem(button.getSlot(), button.getItem());
            return this;
        }

        @Override
        public GuiPage build() {
            return new SimpleGuiPage(name, slots, guiItems, inventory);
        }
    }
}
